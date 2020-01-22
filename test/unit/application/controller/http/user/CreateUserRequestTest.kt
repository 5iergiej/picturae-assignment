package com.example.unit.application.controller.http.user

import com.example.application.controller.http.user.CreateUserRequest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CreateUserRequestTest {
    @Test
    fun `valid request can be created`() {
        val request = CreateUserRequest("name", "email@example.com", "password12345")
        assertEquals("name", request.name)
        assertEquals("email@example.com", request.email)
        assertEquals("password12345", request.password)
    }

    @Test
    fun `valid request can not be created with empty name`() {
        assertFails {
            CreateUserRequest("", "email@example.com", "password12345")
        }
    }

    @Test
    fun `valid request can not be created with too long name`() {
        assertFails {
            CreateUserRequest("a".repeat(101), "email@example.com", "password12345")
        }
    }

    @Test
    fun `valid request can not be created with invalid email`() {
        assertFails {
            CreateUserRequest("name", "email.at.example.com", "password12345")
        }
    }

    @Test
    fun `valid request can not be created with too short password`() {
        assertFails {
            CreateUserRequest("name", "email@example.com", "12345")
        }
    }
}