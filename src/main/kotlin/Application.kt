package com.example

import com.example.models.user
import com.example.security.hashing.SHA256Hashingservice
import com.example.security.token.jwtTokenService
import com.example.security.token.tokenconfig 
import com.example.users.mongouserdatasource
import io.ktor.server.application.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val mongopw = System.getenv("MONGO_PW")
    val dbname = "ktorauth"
    val encodedPw = java.net.URLEncoder.encode(mongopw, "UTF-8")
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://Manthansingla:$encodedPw@ktorauth.mzaf0uw.mongodb.net/?retryWrites=true&w=majority&appName=$dbname"
    ).coroutine
        .getDatabase(dbname)

    val userdatasource = mongouserdatasource(db)
    val tokenservice = jwtTokenService()
    val tokenconfig = tokenconfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresin = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingservice = SHA256Hashingservice()

    // to test if we can access the database correctly and if we are able to store it
//    GlobalScope.launch {
//        val user = user(
//            username = "testuser",
//            password = "testpassword",
//            salt = "salt"
//        )
//        userdatasource.insertuser(
//            user
//        )
//    }

    configureSerialization()
    configureMonitoring()
    configureSecurity(config = tokenconfig)
    configureRouting(userdatasource, tokenconfig, tokenservice, hashingservice)
}
