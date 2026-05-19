package com.froyder.personaltrainer.data.repository

import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.WorkoutPlanResponse
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.remote.ApiConfig
import com.froyder.personaltrainer.data.remote.GeminiContent
import com.froyder.personaltrainer.data.remote.GeminiPart
import com.froyder.personaltrainer.data.remote.GeminiRequest
import com.froyder.personaltrainer.data.remote.GeminiResponse
import com.froyder.personaltrainer.data.remote.buildPlanPrompt
import com.froyder.personaltrainer.utils.NetworkMonitor
import com.froyder.personaltrainer.utils.getCurrentTimeMillis
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

class GeminiRepository(
    private val client: HttpClient,
    private val networkMonitor: NetworkMonitor = NetworkMonitor()
) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun generateWorkoutPlan(user: User): WorkoutPlan {
        if (!networkMonitor.isConnected()) {
            throw Exception("No internet connection. Please check your network and try again.")
        }
        return doGenerateWorkoutPlan(user)
    }

    private suspend fun doGenerateWorkoutPlan(user: User): WorkoutPlan {
        val prompt = buildPlanPrompt(user)

        val response: GeminiResponse = client.post(
            "${ApiConfig.GEMINI_BASE_URL}/models/${ApiConfig.GEMINI_MODEL}:generateContent"
        ) {
            parameter("key", ApiConfig.GEMINI_API_KEY)
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    contents = listOf(
                        GeminiContent(parts = listOf(GeminiPart(text = prompt)))
                    )
                )
            )
        }.body()

        val rawJson = response.candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: throw Exception("Empty response from Gemini")

        return parseWorkoutPlan(rawJson, user.id)
    }

    private fun parseWorkoutPlan(rawJson: String, userId: String): WorkoutPlan {
        // Strip markdown code fences if Gemini adds them anyway
        val cleaned = rawJson
            .replace("```json", "")
            .replace("```", "")
            .trim()

        val parsed = json.decodeFromString<WorkoutPlanResponse>(cleaned)

        return WorkoutPlan(
            id = "plan_${userId}",
            userId = userId,
            weeklyDays = parsed.weeklyDays,
            generatedAt = getCurrentTimeMillis()
        )
    }
}