package com.froyder.personaltrainer.data.model

import com.froyder.personaltrainer.utils.getCurrentTimeMillis
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseLog(
    val exerciseId: String,
    val exerciseName: String,
    val completedSets: List<SetLog>
)

@Serializable
data class SetLog(
    val setNumber: Int,
    val reps: Int,
    val weightKg: Float = 0f,  // 0 for bodyweight exercises
    val completed: Boolean = false
)

@Serializable
data class WorkoutSession(
    val id: String,
    val planId: String,
    val userId: String = "",
    val trainingDayId: String,
    val startedAt: Long = getCurrentTimeMillis(),
    val completedAt: Long? = null,
    val exerciseLogs: List<ExerciseLog> = emptyList(),
    val isCompleted: Boolean = false
)