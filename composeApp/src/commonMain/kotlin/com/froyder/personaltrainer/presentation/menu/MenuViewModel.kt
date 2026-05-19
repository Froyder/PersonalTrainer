package com.froyder.personaltrainer.presentation.menu

import androidx.lifecycle.ViewModel
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.froyder.personaltrainer.presentation.AppViewModel
import com.froyder.personaltrainer.presentation.PlanGenerationState
import com.froyder.personaltrainer.utils.notifications.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel(
    private val localRepository: LocalRepository,
    private val appViewModel: AppViewModel
) : ViewModel() {

    val user = appViewModel.currentUser

    fun loadUser(userId: String) {
        appViewModel.initForUser(userId)
    }

    fun updateName(name: String) = updateUser() { it.copy(name = name) }
    fun updateAge(age: Int) = updateUser() { it.copy(age = age) }
    fun updateWeight(weight: Float) = updateUser() { it.copy(weightKg = weight) }
    fun updateHeight(height: Float) = updateUser() { it.copy(heightCm = height) }
    fun toggleUnits() = updateUser() { it.copy(useImperialUnits = !it.useImperialUnits) }

    private fun updateUser(transform: (User) -> User) {
        val current = appViewModel.currentUser.value ?: return
        val updated = transform(current)
        appViewModel.saveUser(updated)
        localRepository.saveUser(updated)
    }

    fun updateNotificationSettings(enabled: Boolean, hour: Int, minute: Int) {
        val scheduler = NotificationScheduler()
        if (enabled) {
            scheduler.requestPermission()
            // Get workout days from current plan
            val workoutDays = appViewModel.planState.value
                .let { it as? PlanGenerationState.Success }
                ?.plan
                ?.weeklyDays
                ?.map { it.dayLabel }
                ?: listOf("Monday", "Wednesday", "Friday") // fallback

            scheduler.scheduleForDays(
                days = workoutDays,
                hour = hour,
                minute = minute,
                title = "Personal Trainer",
                message = "Time to work out! Your session is ready 💪"
            )
        } else {
            scheduler.cancelAll()
        }
        updateUser { it.copy(
            notificationsEnabled = enabled,
            reminderHour = hour,
            reminderMinute = minute
        )}
    }
}