package com.froyder.personaltrainer.data.mock

import com.froyder.personaltrainer.data.Exercise
import com.froyder.personaltrainer.data.ExerciseDifficulty
import com.froyder.personaltrainer.data.MuscleGroup
import com.froyder.personaltrainer.data.TrainingDay
import com.froyder.personaltrainer.data.WorkoutPlan

object MockData {

    val samplePlan = WorkoutPlan(
        id = "plan_1",
        userId = "user_1",
        generatedAt = 0L,
        weeklyDays = listOf(
            TrainingDay(
                id = "day_1",
                dayLabel = "Monday",
                focusLabel = "Upper Body",
                estimatedMinutes = 45,
                exercises = listOf(
                    Exercise(
                        id = "ex_1",
                        name = "Push-ups",
                        muscleGroup = MuscleGroup.CHEST,
                        difficulty = ExerciseDifficulty.MEDIUM,
                        sets = 3,
                        reps = 12,
                        restSeconds = 60
                    ),
                    Exercise(
                        id = "ex_2",
                        name = "Dumbbell Rows",
                        muscleGroup = MuscleGroup.BACK,
                        difficulty = ExerciseDifficulty.MEDIUM,
                        sets = 3,
                        reps = 10,
                        restSeconds = 60
                    ),
                    Exercise(
                        id = "ex_3",
                        name = "Shoulder Press",
                        muscleGroup = MuscleGroup.SHOULDERS,
                        difficulty = ExerciseDifficulty.HARD,
                        sets = 3,
                        reps = 8,
                        restSeconds = 90
                    )
                )
            ),
            TrainingDay(
                id = "day_2",
                dayLabel = "Wednesday",
                focusLabel = "Lower Body",
                estimatedMinutes = 40,
                exercises = listOf(
                    Exercise(
                        id = "ex_4",
                        name = "Squats",
                        muscleGroup = MuscleGroup.LEGS,
                        difficulty = ExerciseDifficulty.MEDIUM,
                        sets = 4,
                        reps = 12,
                        restSeconds = 90
                    ),
                    Exercise(
                        id = "ex_5",
                        name = "Lunges",
                        muscleGroup = MuscleGroup.LEGS,
                        difficulty = ExerciseDifficulty.MEDIUM,
                        sets = 3,
                        reps = 10,
                        restSeconds = 60
                    ),
                    Exercise(
                        id = "ex_6",
                        name = "Plank",
                        muscleGroup = MuscleGroup.CORE,
                        difficulty = ExerciseDifficulty.EASY,
                        sets = 3,
                        reps = 1,
                        restSeconds = 45,
                        notes = "Hold for 30 seconds"
                    )
                )
            ),
            TrainingDay(
                id = "day_3",
                dayLabel = "Friday",
                focusLabel = "Full Body",
                estimatedMinutes = 50,
                exercises = listOf(
                    Exercise(
                        id = "ex_7",
                        name = "Burpees",
                        muscleGroup = MuscleGroup.FULL_BODY,
                        difficulty = ExerciseDifficulty.HARD,
                        sets = 3,
                        reps = 10,
                        restSeconds = 90
                    ),
                    Exercise(
                        id = "ex_8",
                        name = "Mountain Climbers",
                        muscleGroup = MuscleGroup.CORE,
                        difficulty = ExerciseDifficulty.MEDIUM,
                        sets = 3,
                        reps = 20,
                        restSeconds = 60
                    )
                )
            )
        )
    )
}