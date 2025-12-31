package com.notionclone.common.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtil(
    @Value("\${app.jwt.secret}")
    private val secret: String,
    
    @Value("\${app.jwt.expiration}")
    private val accessTokenExpiration: Long,
    
    @Value("\${app.jwt.refresh-expiration}")
    private val refreshTokenExpiration: Long
) {
    
    private val key: Key = Keys.hmacShaKeyFor(secret.toByteArray())
    
    fun generateAccessToken(userId: UUID, email: String): String {
        val now = Date()
        val expiryDate = Date(now.time + accessTokenExpiration)
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("email", email)
            .claim("type", "access")
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun generateRefreshToken(userId: UUID): String {
        val now = Date()
        val expiryDate = Date(now.time + refreshTokenExpiration)
        
        return Jwts.builder()
            .setSubject(userId.toString())
            .claim("type", "refresh")
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun getUserIdFromToken(token: String): UUID? {
        return try {
            val claims = getAllClaims(token)
            UUID.fromString(claims.subject)
        } catch (e: Exception) {
            null
        }
    }
    
    fun validateToken(token: String): Boolean {
        return try {
            getAllClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = getAllClaims(token)
            claims.expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
    
    fun getTokenType(token: String): String? {
        return try {
            val claims = getAllClaims(token)
            claims["type"] as? String
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getAllClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }
    
    fun getAccessTokenExpiration(): Long = accessTokenExpiration
    fun getRefreshTokenExpiration(): Long = refreshTokenExpiration
}
