package com.example.infrastructure.storage.user

import com.example.application.storage.user.IUserReadModel
import com.example.application.storage.user.UserModel
import com.example.application.storage.user.UsersCollection
import com.github.jasync.sql.db.Connection

class PGUserReadModel(private var connection: Connection) : IUserReadModel {
    override fun findByName(name: String?, limit: Int?): UsersCollection {
        val users = this.getUsersMatchingName(name, limit ?: 10)
        val count = this.countUsersMatchingName(name)

        return UsersCollection(users, count)
    }

    private fun getUsersMatchingName(name: String?, limit: Int = 10): List<UserModel> {
        var getUsersQuery = "select name, email from users"
        val getUsersQueryParams = mutableListOf<Any>()

        if (!name.isNullOrEmpty()) {
            getUsersQuery += " where name ilike ?"
            getUsersQueryParams.add("$name%")
        }

        getUsersQuery += " limit ?"
        getUsersQueryParams.add(limit)

        val result = this.connection.sendPreparedStatement(getUsersQuery, getUsersQueryParams)

        return result.get().rows.map {
            UserModel(
                it["name"].toString(),
                it["email"].toString()
            )
        }
    }

    private fun countUsersMatchingName(name: String?): Int {
        var countUsersQuery = "select count(email) from users"
        val countUsersQueryParams = mutableListOf<Any>()

        if (!name.isNullOrEmpty()) {
            countUsersQuery += " where name ilike ?"
            countUsersQueryParams.add("$name%")
        }

        val countUsersResult =
            this.connection.sendPreparedStatement(countUsersQuery, countUsersQueryParams).get().rows

        return (countUsersResult.first().first() as Long).toInt()
    }
}