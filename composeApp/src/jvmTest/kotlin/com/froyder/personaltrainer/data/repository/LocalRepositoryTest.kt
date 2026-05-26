package com.froyder.personaltrainer.data.repository

import com.froyder.personaltrainer.data.model.User
import com.russhwolf.settings.PropertiesSettings
import java.util.Properties
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalRepositoryTest {

    private val testUser = User(
        id = "test_123",
        name = "John",
        age = 30,
        weightKg = 80f,
        heightCm = 180f
        // fill in other required fields
    )

    private lateinit var repo: LocalRepository

    @BeforeTest
    fun setup() {
        repo = LocalRepository(PropertiesSettings(Properties()))
    }

    @Test
    fun `save and get a local user data`() {
        val controlUserId = testUser.id
        repo.saveUser(testUser)
        val localUser = repo.getUser(controlUserId)
        assertEquals(testUser, localUser)
    }

    @Test
    fun `set and check isGuestMode value`() {
        repo.setGuestMode(true)
        assertEquals(true, repo.isGuestMode())
    }

    @Test
    fun `clear and check isGuestMode value`() {
        repo.setGuestMode(true)
        repo.clearGuestMode()
        assertEquals(false, repo.isGuestMode())
    }
}