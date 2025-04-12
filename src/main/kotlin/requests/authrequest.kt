package com.example.requests

import kotlinx.serialization.Serializable

@Serializable
data class authrequest(
    val username : String,
    val password : String
)