package com.example.security.token

interface tokenservice {
    fun generate(
        config: tokenconfig,
        vararg claims: tokenclaim,
    ): String
}
