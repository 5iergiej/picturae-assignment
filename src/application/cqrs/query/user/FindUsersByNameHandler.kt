package com.example.application.cqrs.query.user

import com.example.application.storage.user.IUserReadModel
import com.example.application.storage.user.UsersCollection

class FindUsersByNameHandler(private var readModel: IUserReadModel) {
    fun handle(query: FindUsersByNameQuery): UsersCollection {
        return readModel.findByName(query.name, query.limit)
    }
}