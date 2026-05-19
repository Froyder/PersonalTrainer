package com.froyder.personaltrainer.data.repository

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository {

    private val auth = Firebase.auth

    val currentUser: FirebaseUser? get() = auth.currentUser

    val authStateFlow: Flow<Boolean> = auth.authStateChanged.map { it != null }

    suspend fun register(email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password)
        return result.user ?: throw Exception("Registration failed")
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password)
        return result.user ?: throw Exception("Login failed")
    }

    suspend fun logout() {
        auth.signOut()
    }

    suspend fun deleteAccount() {
        auth.currentUser?.delete()
            ?: throw Exception("No user logged in")
    }
}