package com.notionclone.user.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_email", columnList = "email"),
        Index(name = "idx_user_username", columnList = "username")
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, length = 100)
    var name: String,
    
    @Column(nullable = false, unique = true, length = 20)
    var username: String,
    
    @Column(nullable = false, unique = true, length = 255)
    var email: String,
    
    @Column(nullable = false, length = 20)
    var phone: String? = null,
    
    @Column(nullable = false, name = "password_hash")
    var passwordHash: String,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var plan: PlanType = PlanType.FREE,
    
    @Column(length = 500)
    var avatar: String? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var status: UserStatus = UserStatus.PENDING_VERIFICATION,
    
    @Column(nullable = false, name = "email_verified")
    var emailVerified: Boolean = false,
    
    @Column(nullable = false, name = "first_login")
    var firstLogin: Boolean = true,
    
    @Column(nullable = false, name = "failed_login_attempts")
    var failedLoginAttempts: Int = 0,
    
    @Column(name = "blocked_until")
    var blockedUntil: LocalDateTime? = null,
    
    @Column(nullable = false, name = "email_notifications")
    var emailNotifications: Boolean = true,
    
    @Column(nullable = false, name = "marketing_emails")
    var marketingEmails: Boolean = false,
    
    @CreatedDate
    @Column(nullable = false, name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
)

enum class PlanType {
    FREE,
    PRO,
    TEAM,
    ENTERPRISE
}

enum class UserStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    BLOCKED,
    SUSPENDED,
    DELETED
}
