package com.notionclone.document.controller

import com.notionclone.common.util.SecurityHelper
import com.notionclone.document.dto.*
import com.notionclone.document.service.DocumentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/documents")
class DocumentController(
    private val documentService: DocumentService,
    private val securityHelper: SecurityHelper
) {
    
    @PostMapping
    fun create(@Valid @RequestBody dto: CreateDocumentDto): ResponseEntity<DocumentResponse> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.create(dto, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    
    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<DocumentResponse> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.getById(id, user)
        return ResponseEntity.ok(response)
    }
    
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: UpdateDocumentDto
    ): ResponseEntity<DocumentResponse> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.update(id, dto, user)
        return ResponseEntity.ok(response)
    }
    
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Map<String, String>> {
        val user = securityHelper.getCurrentUser()
        documentService.delete(id, user)
        return ResponseEntity.ok(mapOf("message" to "Document deleted successfully"))
    }
    
    @GetMapping
    fun getMyDocuments(): ResponseEntity<List<DocumentSummaryResponse>> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.getMyDocuments(user)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/{parentId}/sub-pages")
    fun getSubPages(@PathVariable parentId: UUID): ResponseEntity<List<DocumentSummaryResponse>> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.getSubPages(parentId, user)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/favorites")
    fun getFavorites(): ResponseEntity<List<DocumentSummaryResponse>> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.getFavorites(user)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/archived")
    fun getArchived(): ResponseEntity<List<DocumentSummaryResponse>> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.getArchived(user)
        return ResponseEntity.ok(response)
    }
    
    @GetMapping("/search")
    fun search(@RequestParam q: String): ResponseEntity<List<DocumentSummaryResponse>> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.search(q, user)
        return ResponseEntity.ok(response)
    }
    
    @PatchMapping("/{id}/public")
    fun makePublic(
        @PathVariable id: UUID,
        @Valid @RequestBody dto: PublicDocumentDto
    ): ResponseEntity<DocumentResponse> {
        val user = securityHelper.getCurrentUser()
        val response = documentService.makePublic(id, dto, user)
        return ResponseEntity.ok(response)
    }
}

@RestController
@RequestMapping("/api/public")
class PublicDocumentController(
    private val documentService: DocumentService
) {
    
    @GetMapping("/documents/{slug}")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<DocumentResponse> {
        val response = documentService.getByPublicSlug(slug)
        return ResponseEntity.ok(response)
    }
}
