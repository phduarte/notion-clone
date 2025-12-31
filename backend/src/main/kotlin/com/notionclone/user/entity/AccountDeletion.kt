package com.notionclone.user.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "account_deletions",
    indexes = [
        Index(name = "idx_deletion_user", columnList = "user_id")
    ]
)
data class AccountDeletion(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Column(nullable = false, length = 500)
    val reason: String,
    
    @Column(columnDefinition = "TEXT")
    val feedback: String? = null,
    
    @CreatedDate
    @Column(nullable = false, name = "deleted_at", updatable = false)
    val deletedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "permanent_deletion_at")
    val permanentDeletionAt: LocalDateTime
)
