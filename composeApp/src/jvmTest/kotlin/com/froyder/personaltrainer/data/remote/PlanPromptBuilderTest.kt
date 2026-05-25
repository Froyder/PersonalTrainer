package com.froyder.personaltrainer.data.remote

import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.FitnessGoal
import com.froyder.personaltrainer.data.model.FitnessLevel
import com.froyder.personaltrainer.data.model.Equipment
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class PlanPromptBuilderTest {

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

    @Test
    fun `prompt contains user name`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "John")
    }

    @Test
    fun `prompt contains user goal`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "GAIN_MUSCLE")
    }

    @Test
    fun `prompt contains correct days per week`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "4")
    }

    @Test
    fun `prompt contains user fitness level`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "INTERMEDIATE")
    }

    @Test
    fun `prompt contains equipment info`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "FULL_GYM")
    }

    @Test
    fun `prompt contains JSON schema instruction`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "weeklyDays")
        assertContains(prompt, "exercises")
    }

    @Test
    fun `prompt contains description instruction`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "description")
    }

    @Test
    fun `prompt contains youtube query instruction`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "youtubeQuery")
    }

    @Test
    fun `prompt is not empty`() {
        val prompt = buildPlanPrompt(testUser)
        assertTrue(prompt.isNotBlank())
    }

    @Test
    fun `prompt contains user body metrics`() {
        val prompt = buildPlanPrompt(testUser)
        assertContains(prompt, "80")   // weight
        assertContains(prompt, "180")  // height
        assertContains(prompt, "30")   // age
    }

    @Test
    fun `prompt changes when user goal changes`() {
        val userLoseWeight = testUser.copy(goal = FitnessGoal.LOSE_WEIGHT)
        val prompt1 = buildPlanPrompt(testUser)
        val prompt2 = buildPlanPrompt(userLoseWeight)
        assertTrue(prompt1 != prompt2)
    }

    @Test
    fun `prompt changes when days per week changes`() {
        val user3Days = testUser.copy(daysPerWeek = 3)
        val user5Days = testUser.copy(daysPerWeek = 5)
        val prompt1 = buildPlanPrompt(user3Days)
        val prompt2 = buildPlanPrompt(user5Days)
        assertTrue(prompt1 != prompt2)
    }
}