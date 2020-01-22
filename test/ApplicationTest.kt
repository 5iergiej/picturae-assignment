package com.example

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `test get users with no parameters provided returns 200`() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/users/name-lookup").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(false, response.content?.isEmpty())
            }
        }
    }

    @Test
    fun `test post users with valid parameters provided returns 200`() {
        withTestApplication({ module(testing = true) }) {
            with(handleRequest(HttpMethod.Post, "/users") {
                val random = Random.nextInt()
                addHeader("Content-Type", "application/json")
                setBody("{\"name\":\"new name\", \"email\": \"test+$random@example.com\", \"password\":\"password12345\"}")
            }) {
                assertEquals(HttpStatusCode.Accepted, response.status())
            }
        }
    }

    @Test
    fun `test post users with invalid email provided returns 400`() {
        withTestApplication({ module(testing = true) }) {
            with(handleRequest(HttpMethod.Post, "/users") {
                val random = Random.nextInt()
                addHeader("Content-Type", "application/json")
                setBody("{\"name\":\"new name\", \"email\": \"invalid.email.com\", \"password\":\"password12345\"}")
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }

    @Test
    fun `test post users with invalid password provided returns 400`() {
        withTestApplication({ module(testing = true) }) {
            with(handleRequest(HttpMethod.Post, "/users") {
                val random = Random.nextInt()
                addHeader("Content-Type", "application/json")
                setBody("{\"name\":\"new name\", \"email\": \"email@example.com\", \"password\":\"short\"}")
            }) {
                assertEquals(HttpStatusCode.BadRequest, response.status())
            }
        }
    }
}