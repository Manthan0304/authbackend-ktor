package com.example.users

import com.example.models.user

interface userdatasource {
    suspend fun getuserbyusername(username : String) : user?
    suspend fun insertuser(user : user) : Boolean
}