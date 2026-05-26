package com.froyder.personaltrainer.presentation

import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.FitnessGoal
import com.froyder.personaltrainer.data.model.FitnessLevel
import com.froyder.personaltrainer.data.model.Equipment
import com.froyder.personaltrainer.data.repository.FirestoreRepository
import com.froyder.personaltrainer.data.repository.GeminiRepository
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.russhwolf.settings.PropertiesSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.util.Properties
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    private val testScope = TestScope()
    private val mockGemini = mockk<GeminiRepository>()
    private val mockFirestore = mockk<FirestoreRepository>(relaxed = true)

    private lateinit var localRepo: LocalRepository
    private lateinit var viewModel: AppViewModel

    private val testUser = User(
        id = "test_id",
        name = "John",
        age = 30,
        weightKg = 80f,
        heightCm = 180f,
        goal = FitnessGoal.GAIN_MUSCLE,
        level = FitnessLevel.INTERMEDIATE,
        equipment = Equipment.FULL_GYM,
        daysPerWeek = 4
    )

    private val mockPlan = WorkoutPlan(
        id = "plan_test",
        userId = "test_id",
        weeklyDays = emptyList()
    )

    @BeforeTest
    fun setup() {
        localRepo = LocalRepository(PropertiesSettings(Properties()))
        viewModel = AppViewModel(
            geminiRepository = mockGemini,
            localRepository = localRepo,
            firestoreRepository = mockFirestore,
            externalScope = testScope
        )
    }

    @Test
    fun `generatePlan updates state to Success`() = testScope.runTest {
        coEvery { mockGemini.generateWorkoutPlan(any()) } returns mockPlan

        viewModel.generatePlan(testUser)
        advanceUntilIdle()

        assertTrue(viewModel.planState.value is PlanGenerationState.Success)
    }

    @Test
    fun `generatePlan fails and state changes to Error`() = testScope.runTest {
        coEvery { mockGemini.generateWorkoutPlan(any()) } throws Exception("fail")

        viewModel.generatePlan(testUser)
        advanceUntilIdle()

        println("DEBUG state: ${viewModel.planState.value}")
        assertTrue(viewModel.planState.value is PlanGenerationState.Error)
    }

    @Test
    fun `generatePlan saves plan to local storage`() = testScope.runTest {
        coEvery { mockGemini.generateWorkoutPlan(any()) } returns mockPlan

        viewModel.generatePlan(testUser)
        advanceUntilIdle()

        assertEquals(mockPlan,localRepo.getPlanForUser(testUser.id))
    }

    @Test
    fun `generatePlan calls Gemini exactly once`() = testScope.runTest {
        coEvery { mockGemini.generateWorkoutPlan(any()) } returns mockPlan

        viewModel.generatePlan(testUser)
        advanceUntilIdle()

        coVerify(exactly = 1) { mockGemini.generateWorkoutPlan(any()) }
    }
}