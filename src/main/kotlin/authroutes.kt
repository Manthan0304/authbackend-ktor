package com.example


import com.example.models.user
import com.example.requests.authrequest
import com.example.responses.Authresponse
import com.example.security.hashing.hashingservice
import com.example.security.hashing.saltedhash
import com.example.security.token.tokenclaim
import com.example.security.token.tokenconfig
import com.example.security.token.tokenservice
import com.example.users.userdatasource
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.signup(
    hashingService: hashingservice,
    userdatasource: userdatasource,
) {
    post("signup") {
        val request = call.receiveNullable<authrequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val arefieldsblank = request.username.isBlank() || request.password.isBlank()
        val ispwtooshort = request.password.length < 8

        if (arefieldsblank || ispwtooshort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedhash = hashingService.generatesaltedhash(request.password)
        val user = user(
            username = request.username,
            password = saltedhash.hash,
            salt = saltedhash.salt
        )
        val wasacknowledged = userdatasource.insertuser(user)
        if (!wasacknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        call.respond(HttpStatusCode.OK)

    }
}

fun Route.signin(
    hashingService: hashingservice,
    userdatasource: userdatasource,
    tokenService: tokenservice,
    tokenconfig: tokenconfig,
) {
    post("signin") {
        val request = call.receiveNullable<authrequest>() ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userdatasource.getuserbyusername(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "incorrect usrname of pasword")
            return@post
        }

        val isvalidpassword = hashingService.verify(
            value = request.password,
            saltedhash = saltedhash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isvalidpassword) {
            call.respond(HttpStatusCode.Conflict, "incorrect usrname of pasword")
            return@post
        }
        val token = tokenService.generate(
            config = tokenconfig,
            tokenclaim(
                name = "userid",
                value = user.id.toString()
            )
        )
        call.respond(
            status = HttpStatusCode.OK,
            message = Authresponse(
                token = token
            )
        )
    }
}

fun Route.authenticate()
{
    authenticate{
        get("authenticate"){
            call.respond(HttpStatusCode.OK)
        }
    }
}