package com.notionclone.document.entity

import com.notionclone.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "document_shares",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_document_user", columnNames = ["document_id", "shared_with_id"])
    ],
    indexes = [
        Index(name = "idx_share_document", columnList = "document_id"),
        Index(name = "idx_share_user", columnList = "shared_with_id")
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class DocumentShare(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_id", nullable = false)
    val sharedWith: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by_id", nullable = false)
    val sharedBy: User,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    var permission: SharePermission = SharePermission.VIEW,
    
    @CreatedDate
    @Column(nullable = false, name = "shared_at", updatable = false)
    var sharedAt: LocalDateTime = LocalDateTime.now()
)

enum class SharePermission {
    VIEW,
    EDIT
}
