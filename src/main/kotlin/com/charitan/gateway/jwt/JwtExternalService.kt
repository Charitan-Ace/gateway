package com.charitan.gateway.jwt

import io.jsonwebtoken.Claims

interface JwtExternalService {
    fun parseJweClaims(jwe: String): Claims

    fun parseJweContent(jwe: String): String
}
