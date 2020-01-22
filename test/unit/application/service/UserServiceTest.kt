package com.example.unit.application.service

import com.example.application.service.UserService
import com.example.application.storage.user.IUserRepository
import io.mockk.*
import org.junit.Test
import kotlin.test.assertFails

class UserServiceTest {

    @Test
    fun `user can be created if does not exist`() {
        val repository = mockk<IUserRepository>()
        every { repository.userExists(any()) } returns false
        every { repository.save(any()) } just Runs

        UserService(repository).createUser("name", "email@example.com", "password12345")

        // at this point we do not care about the exact value of the user
        verify (exactly = 1){ repository.save(any()) }
    }

    @Test
    fun `user can not be created if exists`() {
        val repository = mockk<IUserRepository>()
        every { repository.userExists(any()) } returns true

        assertFails() {
            UserService(repository).createUser("name", "email@example.com", "password12345")
        }
    }
}