package com.example.security.token

data class tokenconfig (
    val issuer : String,
    val audience : String,
    val expiresin : Long,
    val secret : String
)