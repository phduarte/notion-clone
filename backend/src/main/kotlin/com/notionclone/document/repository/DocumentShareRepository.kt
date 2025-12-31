package com.notionclone.document.repository

import com.notionclone.document.entity.Document
import com.notionclone.document.entity.DocumentShare
import com.notionclone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DocumentShareRepository : JpaRepository<DocumentShare, UUID> {
    
    @Query("SELECT ds FROM DocumentShare ds WHERE ds.document.id = :documentId")
    fun findByDocument(@Param("documentId") documentId: UUID): List<DocumentShare>
    
    @Query("SELECT ds FROM DocumentShare ds WHERE ds.sharedWith = :user")
    fun findBySharedWith(@Param("user") user: User): List<DocumentShare>
    
    @Query("SELECT ds FROM DocumentShare ds WHERE ds.document.id = :documentId AND ds.sharedWith.id = :userId")
    fun findByDocumentAndUser(@Param("documentId") documentId: UUID, @Param("userId") userId: UUID): DocumentShare?
    
    @Query("SELECT CASE WHEN COUNT(ds) > 0 THEN true ELSE false END FROM DocumentShare ds WHERE ds.document.id = :documentId AND ds.sharedWith.id = :userId")
    fun existsByDocumentAndUser(@Param("documentId") documentId: UUID, @Param("userId") userId: UUID): Boolean
    
    @Query("SELECT ds.document FROM DocumentShare ds WHERE ds.sharedWith = :user ORDER BY ds.sharedAt DESC")
    fun findSharedDocumentsByUser(@Param("user") user: User): List<Document>
    
    fun deleteByDocumentAndSharedWith(document: Document, sharedWith: User)
}
