package com.notionclone.document.repository

import com.notionclone.document.entity.Document
import com.notionclone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DocumentRepository : JpaRepository<Document, UUID> {
    
    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.deletedAt IS NULL")
    fun findByOwner(@Param("owner") owner: User): List<Document>
    
    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.parent IS NULL AND d.deletedAt IS NULL ORDER BY d.updatedAt DESC")
    fun findMainPagesByOwner(@Param("owner") owner: User): List<Document>
    
    @Query("SELECT d FROM Document d WHERE d.parent.id = :parentId AND d.deletedAt IS NULL ORDER BY d.createdAt ASC")
    fun findSubPagesByParent(@Param("parentId") parentId: UUID): List<Document>
    
    @Query("SELECT d FROM Document d WHERE d.id = :id AND d.deletedAt IS NULL")
    fun findActiveById(@Param("id") id: UUID): Document?
    
    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.isFavorite = true AND d.deletedAt IS NULL ORDER BY d.updatedAt DESC")
    fun findFavoritesByOwner(@Param("owner") owner: User): List<Document>
    
    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND d.isArchived = true AND d.deletedAt IS NULL ORDER BY d.updatedAt DESC")
    fun findArchivedByOwner(@Param("owner") owner: User): List<Document>
    
    @Query("SELECT d FROM Document d WHERE d.publicSlug = :slug AND d.isPublic = true AND d.deletedAt IS NULL")
    fun findByPublicSlug(@Param("slug") slug: String): Document?
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.owner = :owner AND d.parent IS NULL AND d.deletedAt IS NULL")
    fun countMainPagesByOwner(@Param("owner") owner: User): Long
    
    @Query("SELECT COUNT(d) FROM Document d WHERE d.parent.id = :parentId AND d.deletedAt IS NULL")
    fun countSubPagesByParent(@Param("parentId") parentId: UUID): Long
    
    @Query("SELECT d FROM Document d WHERE d.owner = :owner AND LOWER(d.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND d.deletedAt IS NULL")
    fun searchByTitle(@Param("owner") owner: User, @Param("searchTerm") searchTerm: String): List<Document>
}
