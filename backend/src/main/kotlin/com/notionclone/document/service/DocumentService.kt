package com.notionclone.document.service

import com.notionclone.common.exception.*
import com.notionclone.common.util.CodeGenerator
import com.notionclone.common.util.HtmlSanitizer
import com.notionclone.document.dto.*
import com.notionclone.document.entity.Document
import com.notionclone.document.entity.SharePermission
import com.notionclone.document.repository.DocumentRepository
import com.notionclone.document.repository.DocumentShareRepository
import com.notionclone.user.entity.PlanType
import com.notionclone.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val documentShareRepository: DocumentShareRepository,
    private val htmlSanitizer: HtmlSanitizer,
    private val codeGenerator: CodeGenerator
) {
    
    fun create(dto: CreateDocumentDto, user: User): DocumentResponse {
        // Sanitize content
        val sanitizedContent = htmlSanitizer.sanitize(dto.content)
        
        // Validate parent if provided
        val parent = if (dto.parentId != null) {
            val parentDoc = documentRepository.findActiveById(dto.parentId)
                ?: throw InvalidParentException()
            
            // Check if user owns the parent
            if (parentDoc.owner.id != user.id) {
                throw DocumentAccessDeniedException()
            }
            
            // Check if parent itself has a parent (only 2 levels allowed)
            if (parentDoc.parent != null) {
                throw ValidationException("Cannot create sub-pages more than 2 levels deep")
            }
            
            parentDoc
        } else {
            null
        }
        
        // Check plan limits
        if (parent == null) {
            // Main page
            val mainPagesCount = documentRepository.countMainPagesByOwner(user)
            val maxMainPages = when (user.plan) {
                PlanType.FREE -> 1
                PlanType.PRO -> 100
                PlanType.TEAM -> Long.MAX_VALUE
                PlanType.ENTERPRISE -> Long.MAX_VALUE
            }
            
            if (mainPagesCount >= maxMainPages) {
                throw MaxPagesExceededException("You have reached the maximum number of main pages for your ${user.plan} plan")
            }
        } else {
            // Sub-page
            val subPagesCount = documentRepository.countSubPagesByParent(parent.id)
            val maxSubPages = when (user.plan) {
                PlanType.FREE -> 3
                PlanType.PRO -> 10
                PlanType.TEAM -> Long.MAX_VALUE
                PlanType.ENTERPRISE -> Long.MAX_VALUE
            }
            
            if (subPagesCount >= maxSubPages) {
                throw MaxSubPagesExceededException("You have reached the maximum number of sub-pages for your ${user.plan} plan")
            }
        }
        
        // Create document
        val document = Document(
            title = dto.title,
            content = sanitizedContent,
            icon = dto.icon,
            cover = dto.cover,
            owner = user,
            parent = parent
        )
        
        val saved = documentRepository.save(document)
        return saved.toResponse()
    }
    
    fun update(id: UUID, dto: UpdateDocumentDto, user: User): DocumentResponse {
        val document = getDocumentWithWriteAccess(id, user)
        
        dto.title?.let { document.title = it }
        dto.content?.let { document.content = htmlSanitizer.sanitize(it) }
        dto.icon?.let { document.icon = it }
        dto.cover?.let { document.cover = it }
        dto.isFavorite?.let { document.isFavorite = it }
        dto.isArchived?.let { document.isArchived = it }
        
        document.lastEditedById = user.id
        
        val saved = documentRepository.save(document)
        return saved.toResponse()
    }
    
    fun getById(id: UUID, user: User): DocumentResponse {
        val document = getDocumentWithReadAccess(id, user)
        return document.toResponse()
    }
    
    fun getMyDocuments(user: User): List<DocumentSummaryResponse> {
        val documents = documentRepository.findMainPagesByOwner(user)
        return documents.map { it.toSummary() }
    }
    
    fun getSubPages(parentId: UUID, user: User): List<DocumentSummaryResponse> {
        // Verify user has access to parent
        getDocumentWithReadAccess(parentId, user)
        
        val subPages = documentRepository.findSubPagesByParent(parentId)
        return subPages.map { it.toSummary() }
    }
    
    fun getFavorites(user: User): List<DocumentSummaryResponse> {
        val documents = documentRepository.findFavoritesByOwner(user)
        return documents.map { it.toSummary() }
    }
    
    fun getArchived(user: User): List<DocumentSummaryResponse> {
        val documents = documentRepository.findArchivedByOwner(user)
        return documents.map { it.toSummary() }
    }
    
    fun search(searchTerm: String, user: User): List<DocumentSummaryResponse> {
        val documents = documentRepository.searchByTitle(user, searchTerm)
        return documents.map { it.toSummary() }
    }
    
    fun delete(id: UUID, user: User) {
        val document = getDocumentWithWriteAccess(id, user)
        
        // Check if document has sub-pages
        val subPagesCount = documentRepository.countSubPagesByParent(id)
        if (subPagesCount > 0) {
            throw CannotDeleteParentWithChildrenException()
        }
        
        // Soft delete
        document.deletedAt = LocalDateTime.now()
        documentRepository.save(document)
    }
    
    fun makePublic(id: UUID, dto: PublicDocumentDto, user: User): DocumentResponse {
        val document = getDocumentWithWriteAccess(id, user)
        
        // Check if slug is already in use
        val existing = documentRepository.findByPublicSlug(dto.slug)
        if (existing != null && existing.id != id) {
            throw SlugAlreadyInUseException()
        }
        
        document.isPublic = dto.isPublic
        document.publicSlug = if (dto.isPublic) dto.slug else null
        
        val saved = documentRepository.save(document)
        return saved.toResponse()
    }
    
    fun getByPublicSlug(slug: String): DocumentResponse {
        val document = documentRepository.findByPublicSlug(slug)
            ?: throw DocumentNotFoundException()
        
        return document.toResponse()
    }
    
    private fun getDocumentWithReadAccess(id: UUID, user: User): Document {
        val document = documentRepository.findActiveById(id)
            ?: throw DocumentNotFoundException()
        
        // Check if user is owner
        if (document.owner.id == user.id) {
            return document
        }
        
        // Check if document is shared with user
        val hasAccess = documentShareRepository.existsByDocumentAndUser(id, user.id)
        if (!hasAccess) {
            throw DocumentAccessDeniedException()
        }
        
        return document
    }
    
    private fun getDocumentWithWriteAccess(id: UUID, user: User): Document {
        val document = documentRepository.findActiveById(id)
            ?: throw DocumentNotFoundException()
        
        // Check if user is owner
        if (document.owner.id == user.id) {
            return document
        }
        
        // Check if document is shared with EDIT permission
        val share = documentShareRepository.findByDocumentAndUser(id, user.id)
        if (share == null || share.permission != SharePermission.EDIT) {
            throw DocumentAccessDeniedException()
        }
        
        return document
    }
    
    private fun Document.toResponse(): DocumentResponse {
        val subPagesCount = documentRepository.countSubPagesByParent(id).toInt()
        return DocumentResponse(
            id = id.toString(),
            title = title,
            content = content,
            icon = icon,
            cover = cover,
            ownerId = owner.id.toString(),
            ownerName = owner.name,
            parentId = parent?.id?.toString(),
            isFavorite = isFavorite,
            isArchived = isArchived,
            isPublic = isPublic,
            publicSlug = publicSlug,
            allowComments = allowComments,
            subPagesCount = subPagesCount,
            createdAt = createdAt.toString(),
            updatedAt = updatedAt.toString(),
            lastEditedByName = null // TODO: fetch from user repository if needed
        )
    }
    
    private fun Document.toSummary(): DocumentSummaryResponse {
        val subPagesCount = documentRepository.countSubPagesByParent(id).toInt()
        return DocumentSummaryResponse(
            id = id.toString(),
            title = title,
            icon = icon,
            isFavorite = isFavorite,
            isArchived = isArchived,
            subPagesCount = subPagesCount,
            updatedAt = updatedAt.toString()
        )
    }
}
