package com.example.domain.user

class Name(var value: String) {
    init {
        this.validate(value)
    }

    private fun validate(name: String) {
        if (name.isEmpty()) throw IllegalArgumentException("Invalid user name: $name")
        if (!name.matches(Regex("^[A-Za-z ,.'-_0-9]+\$"))) throw IllegalArgumentException("Invalid user name: $name")
    }
}