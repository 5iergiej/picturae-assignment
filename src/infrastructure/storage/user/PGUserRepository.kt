package com.example.infrastructure.storage.user

import com.example.application.storage.user.IUserRepository
import com.example.domain.user.User
import com.github.jasync.sql.db.Connection

class PGUserRepository(var connection: Connection) :
    IUserRepository {
    override fun save(user: User) {
        connection.sendPreparedStatement(
            "insert into users (email, name, password_hash) values(?,?,?)",
            listOf(user.email.value, user.name.value, user.password)
        )
    }

    override fun userExists(user: User): Boolean {
        val result = connection.sendPreparedStatement(
            "select * from users where email = ?",
            listOf(user.email.value)
        ).get()

        return result.rows.isNotEmpty()
    }
}