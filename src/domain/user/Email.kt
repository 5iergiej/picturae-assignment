package com.example.domain.user

class Email(var value: String) {
    init {
        this.validate(value)
    }

    private fun validate(email: String) {
        if (!email.contains('@')) throw IllegalArgumentException("Invalid user email: $email")
    }
}