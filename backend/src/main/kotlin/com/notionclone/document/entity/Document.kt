package com.notionclone.document.entity

import com.notionclone.user.entity.User
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "documents",
    indexes = [
        Index(name = "idx_document_owner", columnList = "owner_id"),
        Index(name = "idx_document_parent", columnList = "parent_id"),
        Index(name = "idx_document_deleted", columnList = "deleted_at")
    ]
)
@EntityListeners(AuditingEntityListener::class)
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, length = 255)
    var title: String,
    
    @Column(columnDefinition = "TEXT")
    var content: String? = null,
    
    @Column(length = 500)
    var icon: String? = null,
    
    @Column(length = 1000)
    var cover: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Document? = null,
    
    @Column(nullable = false, name = "is_favorite")
    var isFavorite: Boolean = false,
    
    @Column(nullable = false, name = "is_archived")
    var isArchived: Boolean = false,
    
    @Column(nullable = false, name = "is_public")
    var isPublic: Boolean = false,
    
    @Column(unique = true, length = 100, name = "public_slug")
    var publicSlug: String? = null,
    
    @Column(nullable = false, name = "allow_comments")
    var allowComments: Boolean = true,
    
    @CreatedDate
    @Column(nullable = false, name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "last_edited_by_id")
    var lastEditedById: UUID? = null,
    
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
)
