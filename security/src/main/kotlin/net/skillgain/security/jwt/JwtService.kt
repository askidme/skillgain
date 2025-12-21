package net.skillgain.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import net.skillgain.domain.entity.user.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

// security/jwt/JwtService.kt
@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expiration-ms}") private val expirationMs: Long
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(user: User): String = Jwts.builder()
        .setSubject(user.email)
        .claim("roles", user.roles.map { it.name })
        .setIssuedAt(Date())
        .setExpiration(Date(System.currentTimeMillis() + expirationMs))
        .signWith(key)
        .compact()

    fun extractUsername(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).body.subject
}