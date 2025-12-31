package com.notionclone.auth.repository

import com.notionclone.auth.entity.RefreshToken
import com.notionclone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID> {
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token AND rt.revoked = false AND rt.expiresAt > :now")
    fun findValidToken(
        @Param("token") token: String,
        @Param("now") now: LocalDateTime = LocalDateTime.now()
    ): RefreshToken?
    
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.user = :user AND rt.revoked = false AND rt.expiresAt > :now")
    fun findActiveTokensByUser(
        @Param("user") user: User,
        @Param("now") now: LocalDateTime = LocalDateTime.now()
    ): List<RefreshToken>
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :now WHERE rt.user = :user AND rt.revoked = false")
    fun revokeAllByUser(@Param("user") user: User, @Param("now") now: LocalDateTime = LocalDateTime.now())
    
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :now WHERE rt.token = :token")
    fun revokeToken(@Param("token") token: String, @Param("now") now: LocalDateTime = LocalDateTime.now())
    
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now OR rt.revoked = true")
    fun deleteExpiredAndRevoked(@Param("now") now: LocalDateTime = LocalDateTime.now())
}
