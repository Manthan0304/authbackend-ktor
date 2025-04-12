package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.user
import com.example.security.hashing.SHA256Hashingservice
import com.example.security.token.jwtTokenService
import com.example.security.token.tokenconfig
import com.example.users.userdatasource
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*

fun Application.configureRouting(
    userdatasource: userdatasource,
    tokenconfig: tokenconfig,
    tokenService: jwtTokenService,
    hashingService: SHA256Hashingservice,
) {
    routing {
        signin(hashingService, userdatasource, tokenService, tokenconfig)
        signup(hashingService, userdatasource)
        authenticate()
        getsecretinfo()
    }
}
