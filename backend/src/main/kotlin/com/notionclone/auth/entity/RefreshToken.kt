package com.notionclone.auth.entity

import com.notionclone.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "refresh_tokens",
    indexes = [
        Index(name = "idx_refresh_token", columnList = "token"),
        Index(name = "idx_refresh_user", columnList = "user_id")
    ]
)
data class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, unique = true, length = 500)
    val token: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @CreatedDate
    @Column(nullable = false, name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "expires_at")
    val expiresAt: LocalDateTime,
    
    @Column(nullable = false)
    var revoked: Boolean = false,
    
    @Column(name = "revoked_at")
    var revokedAt: LocalDateTime? = null,
    
    @Column(length = 100, name = "device_info")
    val deviceInfo: String? = null,
    
    @Column(length = 50, name = "ip_address")
    val ipAddress: String? = null
)
