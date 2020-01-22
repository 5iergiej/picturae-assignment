package com.example.application.storage.user

interface IUserReadModel {
    fun findByName(name: String?, limit: Int?): UsersCollection
}