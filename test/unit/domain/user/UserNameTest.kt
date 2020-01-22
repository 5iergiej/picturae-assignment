package com.example.unit.domain.user

import com.example.domain.user.Name
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class UserNameTest {
    @Test
    fun `user name is valid for letters, spaces, dots, dashes and commas`() {
        val name = Name("Test User  ,.'-")
        assertEquals("Test User  ,.'-", name.value)
    }

    @Test
    fun `user email is not valid if contains @`() {
        assertFails { Name("@") }
    }

    @Test
    fun `user email is not valid if contains !`() {
        assertFails { Name("!") }
    }
}