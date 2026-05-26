package com.froyder.personaltrainer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.WorkoutSession
import com.froyder.personaltrainer.data.repository.FirestoreRepository
import com.froyder.personaltrainer.data.repository.GeminiRepository
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.froyder.personaltrainer.presentation.auth.AuthViewModel
import com.froyder.personaltrainer.utils.CrashReporter
import com.froyder.personaltrainer.utils.getCurrentTimeMillis
import com.froyder.personaltrainer.utils.notifications.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val geminiRepository: GeminiRepository,
    private val localRepository: LocalRepository,
    private val firestoreRepository: FirestoreRepository = FirestoreRepository(),
    private val authViewModel: AuthViewModel? = null,
    private val externalScope: CoroutineScope? = null
) : ViewModel() {

    private val isGuestMode get() = authViewModel?.isGuestMode?.value ?: false
    private val scope get() = externalScope ?: viewModelScope

    private val notificationScheduler = NotificationScheduler()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _planState = MutableStateFlow<PlanGenerationState>(PlanGenerationState.Idle)
    val planState = _planState.asStateFlow()

    private val _selectedDayIndex = MutableStateFlow(0)
    val selectedDayIndex = _selectedDayIndex.asStateFlow()

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState.asStateFlow()

    fun initForUser(userId: String) {
        if (userId.isBlank()) return
        CrashReporter.setUserId(userId)
        scope.launch {
            _syncState.value = SyncState.Syncing

            val cachedUser = localRepository.getUser(userId)
            val cachedPlan = localRepository.getPlanForUser(userId)

            if (cachedUser != null) _currentUser.value = cachedUser
            if (cachedPlan != null) _planState.value = PlanGenerationState.Success(cachedPlan)

            if (userId == "guest_local") {
                if (cachedPlan != null && cachedPlan.weeklyDays.all { it.isCompleted }) {
                    regeneratePlan()
                }
                _syncState.value = SyncState.Done
                return@launch
            }

            syncFromFirestore(userId)
            _syncState.value = SyncState.Done
        }
    }

    private suspend fun syncFromFirestore(userId: String) {
        try {
            val remoteUser = firestoreRepository.getUser(userId)
            val remotePlan = firestoreRepository.getPlan(userId)
            val remoteSessions = firestoreRepository.getSessions(userId)

            if (remoteUser != null) {
                _currentUser.value = remoteUser
                localRepository.saveUser(remoteUser)
            }

            // Sync sessions — merge remote into local
            if (remoteSessions.isNotEmpty()) {
                val localSessions = localRepository.getCompletedSessions()
                val localIds = localSessions.map { it.id }.toSet()
                remoteSessions
                    .filter { it.id !in localIds }  // only save ones not already local
                    .forEach { localRepository.saveSession(it) }
            }

            if (remotePlan != null) {
                when {
                    remotePlan.weeklyDays.all { it.isCompleted } -> {
                        regeneratePlan()
                    }
                    else -> {
                        _planState.value = PlanGenerationState.Success(remotePlan)
                        localRepository.savePlan(remotePlan)
                    }
                }
            } else if (remoteUser != null && _planState.value !is PlanGenerationState.Success) {
                regeneratePlan()
            }

            if (remoteUser != null && remoteUser.notificationsEnabled) {
                notificationScheduler.requestPermission()
                notificationScheduler.scheduleForDays(
                    days = _planState.value
                        .let { it as? PlanGenerationState.Success }
                        ?.plan
                        ?.weeklyDays
                        ?.map { it.dayLabel }
                        ?: emptyList(),
                    hour = remoteUser.reminderHour,
                    minute = remoteUser.reminderMinute,
                    title = "Personal Trainer",
                    message = "Time to work out! Your session is ready 💪"
                )
            }
        } catch (e: Exception) {
            CrashReporter.recordException(e)
            println("DEBUG: Firestore sync failed: ${e.message}")
        }
    }

    fun saveUser(user: User) {
        _currentUser.value = user
        localRepository.saveUser(user)
        if (!isGuestMode) {
            scope.launch {
                try { firestoreRepository.saveUser(user) }
                catch (e: Exception) { CrashReporter.recordException(e) }
            }
        }
    }

    fun generatePlan(user: User) {
        scope.launch {
            _planState.value = PlanGenerationState.Loading
            try {
                val plan = geminiRepository.generateWorkoutPlan(user)
                localRepository.savePlan(plan)
                _planState.value = PlanGenerationState.Success(plan)
            } catch (e: Exception) {
                CrashReporter.recordException(e)
                _planState.value = PlanGenerationState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun regeneratePlan() {
        val user = _currentUser.value ?: return
        scope.launch {
            _planState.value = PlanGenerationState.Loading
            try {
                val newPlan = geminiRepository.generateWorkoutPlan(user)
                localRepository.savePlan(newPlan)
                if (!isGuestMode) firestoreRepository.savePlan(newPlan)
                _planState.value = PlanGenerationState.Success(newPlan)
            } catch (e: Exception) {
                CrashReporter.recordException(e)
                _planState.value = PlanGenerationState.Error(e.message ?: "Failed")
            }
        }
    }

    fun retryPlanGeneration() {
        val user = _currentUser.value ?: return
        generatePlan(user)
    }

    fun loadCachedPlan(userId: String) {
        scope.launch {
            val cached = localRepository.getPlanForUser(userId)
            if (cached != null) {
                _planState.value = PlanGenerationState.Success(cached)
            }
        }
    }

    fun hasPlan(userId: String): Boolean {
        return localRepository.getPlanForUser(userId) != null
    }

    fun selectDay(index: Int) {
        _selectedDayIndex.value = index
    }

    fun markDayCompleted(trainingDayId: String) {
        val currentPlan = (_planState.value as? PlanGenerationState.Success)?.plan ?: return
        val updatedPlan = currentPlan.copy(
            weeklyDays = currentPlan.weeklyDays.map { day ->
                if (day.id == trainingDayId) day.copy(isCompleted = true) else day
            }
        )
        localRepository.savePlan(updatedPlan)
        if (!isGuestMode) {
            scope.launch {
                try { firestoreRepository.savePlan(updatedPlan) }
                catch (e: Exception) { CrashReporter.recordException(e) }
            }
        }
        _planState.value = PlanGenerationState.Success(updatedPlan)
        if (updatedPlan.weeklyDays.all { it.isCompleted }) regeneratePlan()
    }

    fun saveSession(session: WorkoutSession) {
        scope.launch {
            localRepository.saveSession(session)
            if (!isGuestMode) {
                try { firestoreRepository.saveSession(session) }
                catch (e: Exception) { CrashReporter.recordException(e) }
            }
        }
    }

    fun getCompletedSessions(): List<WorkoutSession> {
        return localRepository.getCompletedSessions()
    }

    fun clearInMemoryState() {
        _currentUser.value = null
        _planState.value = PlanGenerationState.Idle
        _syncState.value = SyncState.Idle
        _selectedDayIndex.value = 0
    }

    fun deleteAllUserData(userId: String) {
        localRepository.clearAllData(userId)
        _currentUser.value = null
        _planState.value = PlanGenerationState.Idle
        scope.launch {
            try { firestoreRepository.deleteUserData(userId) }
            catch (e: Exception) { println("DEBUG: Firestore delete failed: ${e.message}") }
        }
    }

    fun calculateStreak(): Int {
        val sessions = localRepository.getCompletedSessions()
        if (sessions.isEmpty()) return 0

        val today = getCurrentTimeMillis()
        val oneDayMs = 24 * 60 * 60 * 1000L

        // Get unique workout days sorted descending
        val workoutDays = sessions
            .map { (it.startedAt / oneDayMs) * oneDayMs }
            .distinct()
            .sortedDescending()

        // Check if worked out today or yesterday
        val todayDay = (today / oneDayMs) * oneDayMs
        val yesterdayDay = todayDay - oneDayMs

        if (workoutDays.first() != todayDay && workoutDays.first() != yesterdayDay) return 0

        // Count consecutive days
        var streak = 1
        for (i in 0 until workoutDays.size - 1) {
            if (workoutDays[i] - workoutDays[i + 1] == oneDayMs) {
                streak++
            } else break
        }
        return streak
    }
}

sealed class PlanGenerationState {
    object Idle : PlanGenerationState()
    object Loading : PlanGenerationState()
    data class Success(val plan: WorkoutPlan) : PlanGenerationState()
    data class Error(val message: String) : PlanGenerationState()
}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    object Done : SyncState()
}