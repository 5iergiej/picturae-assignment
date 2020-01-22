package com.example.application.storage.user

import com.example.domain.user.User

interface IUserRepository {
    fun save(user: User)
    fun userExists(user: User): Boolean
}