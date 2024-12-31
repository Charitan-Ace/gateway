package com.charitan.gateway.jwt.internal

import com.charitan.gateway.jwt.JwtExternalService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.PrivateKey

@Service
internal class JwtServiceImpl : JwtExternalService {
    lateinit var encPrivateKey: PrivateKey

    override fun parseJweClaims(jwe: String): Claims =
        Jwts
            .parser()
            .decryptWith(encPrivateKey)
            .build()
            .parseEncryptedClaims(jwe)
            .payload

    override fun parseJweContent(jwe: String): String {
        val payload =
            Jwts
                .parser()
                .decryptWith(encPrivateKey)
                .build()
                .parseEncryptedContent(jwe)
                .payload

        return String(
            payload,
            StandardCharsets.UTF_8,
        )
    }
}
