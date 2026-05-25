package com.froyder.personaltrainer.presentation.workout

import com.froyder.personaltrainer.data.Exercise
import com.froyder.personaltrainer.data.ExerciseDifficulty
import com.froyder.personaltrainer.data.MuscleGroup
import com.froyder.personaltrainer.data.TrainingDay
import com.froyder.personaltrainer.data.model.SetLog
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorkoutViewModelTest {

    private val testExercises = listOf(
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
            name = "Squats",
            muscleGroup = MuscleGroup.LEGS,
            difficulty = ExerciseDifficulty.MEDIUM,
            sets = 4,
            reps = 10,
            restSeconds = 90
        ),
        Exercise(
            id = "ex_3",
            name = "Plank",
            muscleGroup = MuscleGroup.CORE,
            difficulty = ExerciseDifficulty.EASY,
            sets = 3,
            reps = 1,
            restSeconds = 45
        )
    )

    private val testTrainingDay = TrainingDay(
        id = "day_1",
        dayLabel = "Monday",
        focusLabel = "Upper Body",
        exercises = testExercises,
        estimatedMinutes = 45
    )

    private fun createViewModel() = WorkoutViewModel(testTrainingDay, "plan_1")

    @Test
    fun `initial state contains all exercises from training day`() {
        val controlExercisesCount = testTrainingDay.exercises.count()
        val viewModel = createViewModel()
        assertEquals(controlExercisesCount, viewModel.state.value.trainingDay.exercises.count())
    }

    @Test
    fun `exercises match expected training day data`() {
        val viewModel = createViewModel()
        val exercises = viewModel.state.value.trainingDay.exercises
        assertEquals("Push-ups", exercises[0].name)
        assertEquals("Squats", exercises[1].name)
        assertEquals("Plank", exercises[2].name)
    }

    @Test
    fun `setCurrentExercise clamps out of bounds index to last exercise`() {
        val viewModel = createViewModel()
        viewModel.setCurrentExercise(999)
        assertEquals(2, viewModel.state.value.currentExerciseIndex)
    }

    @Test
    fun `setCurrentExercise clamps negative index to first exercise`() {
        val viewModel = createViewModel()
        viewModel.setCurrentExercise(-1)
        assertEquals(0, viewModel.state.value.currentExerciseIndex)
    }

    @Test
    fun `initial state has first exercise selected`() {
        val viewModel = createViewModel()
        assertEquals(0, viewModel.state.value.currentExerciseIndex)
    }

    @Test
    fun `initial state is not completed`() {
        val viewModel = createViewModel()
        assertFalse(viewModel.state.value.isCompleted)
    }

    @Test
    fun `initial state has no exercise logs`() {
        val viewModel = createViewModel()
        assertTrue(viewModel.state.value.exerciseLogs.isEmpty())
    }

    @Test
    fun `setCurrentExercise updates index`() {
        val viewModel = createViewModel()
        viewModel.setCurrentExercise(1)
        assertEquals(1, viewModel.state.value.currentExerciseIndex)
    }

    @Test
    fun `setCurrentExercise to last exercise`() {
        val viewModel = createViewModel()
        viewModel.setCurrentExercise(2)
        assertEquals(2, viewModel.state.value.currentExerciseIndex)
    }

    @Test
    fun `logSet creates new exercise log`() {
        val viewModel = createViewModel()
        val setLog = SetLog(setNumber = 1, reps = 12, weightKg = 0f, completed = true)
        viewModel.logSet(0, setLog)
        assertEquals(1, viewModel.state.value.exerciseLogs.size)
    }

    @Test
    fun `logSet adds to existing exercise log`() {
        val viewModel = createViewModel()
        val set1 = SetLog(setNumber = 1, reps = 12, weightKg = 0f, completed = true)
        val set2 = SetLog(setNumber = 2, reps = 10, weightKg = 0f, completed = true)
        viewModel.logSet(0, set1)
        viewModel.logSet(0, set2)
        assertEquals(2, viewModel.state.value.exerciseLogs[0].completedSets.size)
    }

    @Test
    fun `logSet stores correct reps`() {
        val viewModel = createViewModel()
        val setLog = SetLog(setNumber = 1, reps = 15, weightKg = 20f, completed = true)
        viewModel.logSet(0, setLog)
        assertEquals(15, viewModel.state.value.exerciseLogs[0].completedSets[0].reps)
    }

    @Test
    fun `logSet stores correct weight`() {
        val viewModel = createViewModel()
        val setLog = SetLog(setNumber = 1, reps = 10, weightKg = 50f, completed = true)
        viewModel.logSet(0, setLog)
        assertEquals(50f, viewModel.state.value.exerciseLogs[0].completedSets[0].weightKg)
    }

    @Test
    fun `logSet for different exercises creates separate logs`() {
        val viewModel = createViewModel()
        val set1 = SetLog(setNumber = 1, reps = 12, weightKg = 0f, completed = true)
        val set2 = SetLog(setNumber = 1, reps = 10, weightKg = 60f, completed = true)
        viewModel.logSet(0, set1)
        viewModel.logSet(1, set2)
        assertEquals(2, viewModel.state.value.exerciseLogs.size)
    }

    @Test
    fun `completeWorkout sets isCompleted to true`() {
        val viewModel = createViewModel()
        viewModel.completeWorkout("user_1")
        assertTrue(viewModel.state.value.isCompleted)
    }

    @Test
    fun `completeWorkout returns session with correct planId`() {
        val viewModel = createViewModel()
        val session = viewModel.completeWorkout("user_1")
        assertEquals("plan_1", session.planId)
    }

    @Test
    fun `completeWorkout returns session with correct userId`() {
        val viewModel = createViewModel()
        val session = viewModel.completeWorkout("user_1")
        assertEquals("user_1", session.userId)
    }

    @Test
    fun `completeWorkout returns session with correct trainingDayId`() {
        val viewModel = createViewModel()
        val session = viewModel.completeWorkout("user_1")
        assertEquals("day_1", session.trainingDayId)
    }

    @Test
    fun `completeWorkout includes logged sets in session`() {
        val viewModel = createViewModel()
        val setLog = SetLog(setNumber = 1, reps = 12, weightKg = 0f, completed = true)
        viewModel.logSet(0, setLog)
        val session = viewModel.completeWorkout("user_1")
        assertEquals(1, session.exerciseLogs.size)
        assertEquals(1, session.exerciseLogs[0].completedSets.size)
    }

    @Test
    fun `rest timer starts at correct seconds`() = runTest {
        val viewModel = createViewModel()
        viewModel.startRestTimer(60)
        assertEquals(60, viewModel.restSeconds.value)
        assertTrue(viewModel.isResting.value)
    }

    @Test
    fun `skipRestTimer stops resting`() = runTest {
        val viewModel = createViewModel()
        viewModel.startRestTimer(60)
        viewModel.skipRestTimer()
        assertFalse(viewModel.isResting.value)
        assertEquals(0, viewModel.restSeconds.value)
    }
}