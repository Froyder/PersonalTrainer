package com.froyder.personaltrainer.data.repository

import com.froyder.personaltrainer.data.model.User
import com.russhwolf.settings.PropertiesSettings
import java.util.Properties
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalRepositoryTest {

    // Fresh in-memory storage for every test
    private fun createRepo() = LocalRepository(PropertiesSettings(Properties()))

    private val testUser = User(
        id = "test_123",
        name = "John",
        age = 30,
        weightKg = 80f,
        heightCm = 180f
        // fill in other required fields
    )

    @Test
    fun `save and get a local user data`() {
        val localRepo = createRepo()
        val controlUserId = testUser.id
        localRepo.saveUser(testUser)
        val localUser = localRepo.getUser(controlUserId)
        assertEquals(testUser, localUser)
    }

    @Test
    fun `set and check isGuestMode value`() {
        val localRepo = createRepo()
        localRepo.setGuestMode(true)
        assertEquals(true, localRepo.isGuestMode())
    }

    @Test
    fun `clear and check isGuestMode value`() {
        val localRepo = createRepo()
        localRepo.setGuestMode(true)
        localRepo.clearGuestMode()
        assertEquals(false, localRepo.isGuestMode())
    }
}