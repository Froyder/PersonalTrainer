package com.froyder.personaltrainer.presentation.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.froyder.personaltrainer.data.TrainingDay
import com.froyder.personaltrainer.data.model.ExerciseLog
import com.froyder.personaltrainer.data.model.SetLog
import com.froyder.personaltrainer.data.model.WorkoutSession
import com.froyder.personaltrainer.utils.getCurrentTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val trainingDay: TrainingDay,
    private val planId: String
) : ViewModel() {

    private val _state = MutableStateFlow(WorkoutState(trainingDay = trainingDay))
    val state = _state.asStateFlow()

    private val _restSeconds = MutableStateFlow(0)
    val restSeconds = _restSeconds.asStateFlow()

    private val _isResting = MutableStateFlow(false)
    val isResting = _isResting.asStateFlow()

    private var timerJob: kotlinx.coroutines.Job? = null

    fun startRestTimer(seconds: Int) {
        timerJob?.cancel()
        _restSeconds.value = seconds
        _isResting.value = true
        timerJob = viewModelScope.launch {
            while (_restSeconds.value > 0) {
                delay(1000)
                _restSeconds.value -= 1
            }
            _isResting.value = false
        }
    }

    fun skipRestTimer() {
        timerJob?.cancel()
        _restSeconds.value = 0
        _isResting.value = false
    }

    fun logSet(exerciseIndex: Int, setLog: SetLog) {
        _state.update { current ->
            val updatedLogs = current.exerciseLogs.toMutableList()
            val existingLog = updatedLogs.getOrNull(exerciseIndex)

            if (existingLog != null) {
                updatedLogs[exerciseIndex] = existingLog.copy(
                    completedSets = existingLog.completedSets + setLog
                )
            } else {
                val exercise = trainingDay.exercises[exerciseIndex]
                updatedLogs.add(
                    ExerciseLog(
                        exerciseId = exercise.id,
                        exerciseName = exercise.name,
                        completedSets = listOf(setLog)
                    )
                )
            }
            current.copy(exerciseLogs = updatedLogs)
        }
    }

    fun setCurrentExercise(index: Int) {
        _state.update { it.copy(currentExerciseIndex = index) }
    }

    fun completeWorkout(userId: String): WorkoutSession {
        val session = WorkoutSession(
            id = "session_${getCurrentTimeMillis()}",
            planId = planId,
            userId = userId,
            trainingDayId = trainingDay.id,
            completedAt = getCurrentTimeMillis(),
            exerciseLogs = _state.value.exerciseLogs,
            isCompleted = true
        )
        _state.update { it.copy(isCompleted = true) }
        return session
    }
}

data class WorkoutState(
    val trainingDay: TrainingDay,
    val currentExerciseIndex: Int = 0,
    val exerciseLogs: List<ExerciseLog> = emptyList(),
    val isCompleted: Boolean = false
)