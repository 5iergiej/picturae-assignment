package com.example.unit.domain.user

import com.example.domain.user.Email
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class UserEmailTest {
    @Test
    fun `user email is only valid if contains @ character`() {
        val email = Email("test@example.com")
        assertEquals("test@example.com", email.value)
    }

    @Test
    fun `user email is not valid if @ character is missing`() {
        assertFails { Email("test.at.example.com") }
    }
}