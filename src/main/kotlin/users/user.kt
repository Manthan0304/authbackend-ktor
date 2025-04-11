package com.example.models
import org.bson.types.ObjectId
import org.litote.kmongo.id.StringId

data class user(
    val username: String,
    val password: String,
    val salt: String,
    val id: ObjectId = ObjectId()
)
