package com.example.application.cqrs.query.user

class FindUsersByNameQuery(var name: String?, var limit: Int?) {
}