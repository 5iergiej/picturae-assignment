package com.example

import com.example.application.controller.http.user.CreateUserRequest
import com.example.application.cqrs.query.user.FindUsersByNameHandler
import com.example.application.cqrs.query.user.FindUsersByNameQuery
import com.example.application.service.UserService
import com.example.infrastructure.storage.user.PGUserReadModel
import com.example.infrastructure.storage.user.PGUserRepository
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.ConnectionPoolConfiguration
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import java.util.concurrent.TimeUnit

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val connectionPool = ConnectionPool(
        PostgreSQLConnectionFactory(
            Configuration(
                username = environment.config.property("ktor.database.user").getString(),
                password = environment.config.property("ktor.database.password").getString(),
                host = environment.config.property("ktor.database.host").getString(),
                port = environment.config.property("ktor.database.port").getString().toInt(),
                database = environment.config.property("ktor.database.dbname").getString()
            )
        ),
        ConnectionPoolConfiguration(
            maxActiveConnections = 100,
            maxIdleTime = TimeUnit.MINUTES.toMillis(15),
            maxPendingQueries = 10_000,
            connectionValidationInterval = TimeUnit.SECONDS.toMillis(30)
        )
    )

    routing {
        post("/users") {
            try {
                val createUserRequest = call.receive<CreateUserRequest>()
                val service = UserService(PGUserRepository(connectionPool))

                service.createUser(
                    createUserRequest.name,
                    createUserRequest.email,
                    createUserRequest.password
                )
                call.respond(HttpStatusCode.Accepted)

            } // catch the assertion errors for a request object
            catch (exception: JsonMappingException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to exception.cause?.message))
            } // catch the logic errors for a value object or service logic
            catch (exception: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to exception.message))
            } catch (exception: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to exception.message))
            }
        }

        get("/users/name-lookup") {
            try {
                val name = call.request.queryParameters["q"]
                val limit = call.request.queryParameters["n"]?.toInt() ?: 10
                val result =
                    FindUsersByNameHandler(PGUserReadModel(connectionPool)).handle(
                        FindUsersByNameQuery(
                            name,
                            limit
                        )
                    )

                call.respond(result)
            } catch (exception: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}