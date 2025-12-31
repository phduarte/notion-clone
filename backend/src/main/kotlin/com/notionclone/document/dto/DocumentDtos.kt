package com.notionclone.document.dto

import com.notionclone.document.entity.SharePermission
import jakarta.validation.constraints.*
import java.util.*

data class CreateDocumentDto(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String,
    
    val content: String? = null,
    
    val icon: String? = null,
    
    val cover: String? = null,
    
    val parentId: UUID? = null
)

data class UpdateDocumentDto(
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String?,
    
    val content: String?,
    
    val icon: String?,
    
    val cover: String?,
    
    val isFavorite: Boolean?,
    
    val isArchived: Boolean?
)

data class MoveDocumentDto(
    val parentId: UUID?
)

data class ShareDocumentDto(
    @field:NotNull(message = "User ID is required")
    val userId: UUID,
    
    @field:NotNull(message = "Permission is required")
    val permission: SharePermission
)

data class UpdateSharePermissionDto(
    @field:NotNull(message = "Permission is required")
    val permission: SharePermission
)

data class PublicDocumentDto(
    @field:NotBlank(message = "Slug is required")
    @field:Size(min = 3, max = 100, message = "Slug must be between 3 and 100 characters")
    @field:Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers and hyphens")
    val slug: String,
    
    @field:NotNull(message = "Public flag is required")
    val isPublic: Boolean
)

data class DocumentResponse(
    val id: String,
    val title: String,
    val content: String?,
    val icon: String?,
    val cover: String?,
    val ownerId: String,
    val ownerName: String,
    val parentId: String?,
    val isFavorite: Boolean,
    val isArchived: Boolean,
    val isPublic: Boolean,
    val publicSlug: String?,
    val allowComments: Boolean,
    val subPagesCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val lastEditedByName: String?
)

data class DocumentSummaryResponse(
    val id: String,
    val title: String,
    val icon: String?,
    val isFavorite: Boolean,
    val isArchived: Boolean,
    val subPagesCount: Int,
    val updatedAt: String
)

data class ShareResponse(
    val id: String,
    val documentId: String,
    val documentTitle: String,
    val sharedWithId: String,
    val sharedWithName: String,
    val sharedWithEmail: String,
    val sharedById: String,
    val sharedByName: String,
    val permission: SharePermission,
    val sharedAt: String
)
