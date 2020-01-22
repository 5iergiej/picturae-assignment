package com.example.application.service

import com.example.application.storage.user.IUserRepository
import com.example.domain.user.Email
import com.example.domain.user.Name
import com.example.domain.user.User
import org.springframework.security.crypto.bcrypt.BCrypt

class UserService (var repository: IUserRepository){
    fun createUser(name: String, email: String, password: String) {
        val user = User(
            Name(name),
            Email(email),
            this.hashPassword(password)
        )

        if (repository.userExists(user)) {
            throw IllegalArgumentException("Email duplicate: $email")
        }

        repository.save(user)
    }

    /*
     * this should be moved outside to avoid the dependency on password encryptor
     * currently can not be unit-tested
     */
    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(10))
    }
}