package com.example.responses

import kotlinx.serialization.Serializable

@Serializable
data class Authresponse(
    val token : String
)
