package com.example.security.hashing

interface hashingservice {
    fun generatesaltedhash(value: String,saltedlength : Int = 32): saltedhash
    fun verify(value: String,saltedhash: saltedhash): Boolean

}