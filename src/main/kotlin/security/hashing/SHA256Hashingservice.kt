package com.example.security.hashing

import java.security.SecureRandom
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils


class SHA256Hashingservice : hashingservice {
    override fun generatesaltedhash(value: String, saltlength: Int): saltedhash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltlength)
        val saltashex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltashex$value")
        return saltedhash(
            hash = hash,
            salt = saltashex
        )
    }

    override fun verify(
        value: String,
        saltedhash: saltedhash,
    ): Boolean {
        return DigestUtils.sha256Hex(saltedhash.salt + value) == saltedhash.hash
    }
}