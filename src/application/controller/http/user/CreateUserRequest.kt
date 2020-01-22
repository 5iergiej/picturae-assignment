package com.example.application.controller.http.user

class CreateUserRequest(var name: String, var email: String, var password: String) {
    init {
        this.validateName(name)
        this.validateEmail(email)
        this.validatePassword(password)
    }

    private fun validateName(name: String) {
        if (name.isEmpty()) throw AssertionError("User name can not be empty.")
        if (name.length > 100) throw AssertionError("User name can not be longer than 100 characters.")
    }

    private fun validateEmail(email: String) {
        if (!email.contains('@')) throw AssertionError("Invalid user email: $email.")
    }

    private fun validatePassword(password: String) {
        if (password.length < 10) throw AssertionError("Invalid user password: password is too short")
    }
}