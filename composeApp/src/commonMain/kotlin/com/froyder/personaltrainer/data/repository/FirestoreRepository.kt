package com.froyder.personaltrainer.data.repository

import com.froyder.personaltrainer.data.WorkoutPlan
import com.froyder.personaltrainer.data.model.User
import com.froyder.personaltrainer.data.model.WorkoutSession
import com.froyder.personaltrainer.utils.NetworkMonitor
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.json.Json

class FirestoreRepository(
    private val networkMonitor: NetworkMonitor = NetworkMonitor()
) {

    private val firestore = Firebase.firestore
    private val json = Json { ignoreUnknownKeys = true }

    private fun checkConnection() {
        if (!networkMonitor.isConnected()) {
            throw Exception("No internet connection")
        }
    }

    // --- User ---
    suspend fun saveUser(user: User) {
        checkConnection()
        try {
            firestore
                .collection("users")
                .document(user.id)
                .set(mapOf(
                    "userData" to json.encodeToString(user)
                ))
        } catch (e: Exception) {
            println("DEBUG: Firestore saveUser failed: ${e.message}")
        }
    }

    suspend fun getUser(userId: String): User? {
        checkConnection()
        return try {
            val snapshot = firestore
                .collection("users")
                .document(userId)
                .get()
            val userData = snapshot.get<String>("userData")
            json.decodeFromString<User>(userData)
        } catch (e: Exception) {
            println("DEBUG: getUser failed: ${e.message}")
            null
        }
    }

    // --- Plan ---
    suspend fun savePlan(plan: WorkoutPlan) {
        checkConnection()
        firestore
            .collection("plans")
            .document(plan.userId)
            .set(mapOf(
                "planData" to json.encodeToString(plan)
            ))
    }

    suspend fun getPlan(userId: String): WorkoutPlan? {
        checkConnection()
        return try {
            val snapshot = firestore
                .collection("plans")
                .document(userId)
                .get()
            val planData = snapshot.get<String>("planData")
            json.decodeFromString<WorkoutPlan>(planData)
        } catch (e: Exception) {
            null
        }
    }

    // --- Sessions ---
    suspend fun saveSession(session: WorkoutSession) {
        if (session.id.isBlank()) return
        checkConnection()
        firestore
            .collection("sessions")
            .document(session.id)
            .set(mapOf(
                "sessionData" to json.encodeToString(session),
                "userId" to session.userId
            ))
    }

    suspend fun getSessions(userId: String): List<WorkoutSession> {
        checkConnection()
        return try {
            firestore
                .collection("sessions")
                .where { "userId" equalTo userId }
                .get()
                .documents
                .mapNotNull { doc ->
                    try {
                        json.decodeFromString<WorkoutSession>(
                            doc.get<String>("sessionData")
                        )
                    } catch (e: Exception) { null }
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteUserData(userId: String) {
        checkConnection()
        firestore.collection("users").document(userId).delete()
        firestore.collection("plans").document(userId).delete()
    }
}