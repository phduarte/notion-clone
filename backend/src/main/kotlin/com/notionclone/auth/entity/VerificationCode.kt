package com.notionclone.auth.entity

import com.notionclone.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "verification_codes",
    indexes = [
        Index(name = "idx_verification_user", columnList = "user_id"),
        Index(name = "idx_verification_code", columnList = "code")
    ]
)
data class VerificationCode(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Column(nullable = false, length = 10)
    val code: String,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    val type: CodeType,
    
    @CreatedDate
    @Column(nullable = false, name = "created_at", updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "expires_at")
    val expiresAt: LocalDateTime,
    
    @Column(nullable = false)
    var used: Boolean = false,
    
    @Column(name = "used_at")
    var usedAt: LocalDateTime? = null,
    
    @Column(nullable = false)
    var attempts: Int = 0
)

enum class CodeType {
    EMAIL_VERIFICATION,
    PASSWORD_RECOVERY
}
