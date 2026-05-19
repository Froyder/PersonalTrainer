package com.froyder.personaltrainer.data.model
import kotlinx.serialization.Serializable

@Serializable
enum class FitnessGoal {
    LOSE_WEIGHT,
    GAIN_MUSCLE,
    IMPROVE_ENDURANCE,
    STAY_ACTIVE
}

@Serializable
enum class FitnessLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

@Serializable
enum class Equipment {
    NO_EQUIPMENT,
    HOME_BASICS,    // dumbbells, resistance bands
    FULL_GYM
}

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val age: Int = 0,
    val weightKg: Float = 0f,
    val heightCm: Float = 0f,
    val goal: FitnessGoal = FitnessGoal.STAY_ACTIVE,
    val level: FitnessLevel = FitnessLevel.BEGINNER,
    val equipment: Equipment = Equipment.NO_EQUIPMENT,
    val daysPerWeek: Int = 3,
    val useImperialUnits: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val reminderHour: Int = 9,
    val reminderMinute: Int = 0
)