package com.froyder.personaltrainer.data

import kotlinx.serialization.Serializable

@Serializable
enum class MuscleGroup {
    CHEST, BACK, SHOULDERS, ARMS, LEGS, CORE, FULL_BODY, CARDIO
}

@Serializable
enum class ExerciseDifficulty {
    EASY, MEDIUM, HARD
}

@Serializable
data class Exercise(
    val id: String,
    val name: String,
    val muscleGroup: MuscleGroup,
    val difficulty: ExerciseDifficulty,
    val sets: Int,
    val reps: Int,
    val restSeconds: Int,
    val notes: String = "",
    val description: String = "",
    val youtubeQuery: String = ""
)

@Serializable
data class TrainingDay(
    val id: String,
    val dayLabel: String,          // e.g. "Monday", "Day 1"
    val focusLabel: String,        // e.g. "Upper Body", "Cardio"
    val exercises: List<Exercise>,
    val estimatedMinutes: Int,
    val isCompleted: Boolean = false
)

@Serializable
data class WorkoutPlan(
    val id: String,
    val userId: String,
    val weeklyDays: List<TrainingDay>,
    val generatedAt: Long = 0L     // timestamp, for later
)

@Serializable
data class WorkoutPlanResponse(
    val weeklyDays: List<TrainingDay>
)