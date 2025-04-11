package com.example.users

import com.example.models.user
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class mongouserdatasource(
    private val db : CoroutineDatabase
    
) : userdatasource {

    private val users = db.getCollection<user>()
    override suspend fun getuserbyusername(username: String): user? {
        return users.findOne(user::username eq username)
    }

    override suspend fun insertuser(user: user): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}