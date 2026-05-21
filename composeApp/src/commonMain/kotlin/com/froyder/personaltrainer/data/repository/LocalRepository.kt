package com.froyder.personaltrainer.data.repository

import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.WorkoutSession
import kotlinx.serialization.json.Json
import com.russhwolf.settings.Settings

class LocalRepository(val settings: Settings) {

    private val json = Json { ignoreUnknownKeys = true }

    // --- User ---
    fun saveUser(user: User) {
        settings.putString("user_${user.id}", json.encodeToString(user))
    }

    fun getUser(userId: String): User? {
        val stored = settings.getStringOrNull("user_$userId") ?: return null
        return try { json.decodeFromString<User>(stored) } catch (e: Exception) { null }
    }

    // --- Plan ---
    fun savePlan(plan: WorkoutPlan) {
        settings.putString("plan_${plan.userId}", json.encodeToString(plan))
    }

    fun getPlanForUser(userId: String): WorkoutPlan? {
        val stored = settings.getStringOrNull("plan_$userId") ?: return null
        return try { json.decodeFromString<WorkoutPlan>(stored) } catch (e: Exception) { null }
    }

    fun deletePlan(userId: String) {
        settings.remove("plan_$userId")
    }

    fun clearAllData(userId: String) {
        settings.remove("user_$userId")
        settings.remove("plan_$userId")
        settings.remove("sessions")
        settings.clear()
    }

    // --- Sessions ---
    fun saveSession(session: WorkoutSession) {
        val sessions = getCompletedSessions().toMutableList()
        sessions.removeAll { it.id == session.id }
        sessions.add(session)
        settings.putString("sessions", json.encodeToString(sessions))
    }

    fun getCompletedSessions(): List<WorkoutSession> {
        val stored = settings.getStringOrNull("sessions") ?: return emptyList()
        return try { json.decodeFromString<List<WorkoutSession>>(stored) } catch (e: Exception) { emptyList() }
    }

    fun getSessionsForPlan(planId: String): List<WorkoutSession> {
        return getCompletedSessions().filter { it.planId == planId }
    }

    fun setGuestMode(isGuest: Boolean) {
        settings.putBoolean("is_guest_mode", isGuest)
    }

    fun isGuestMode(): Boolean {
        return settings.getBooleanOrNull("is_guest_mode") ?: false
    }

    fun clearGuestMode() {
        settings.remove("is_guest_mode")
    }
}