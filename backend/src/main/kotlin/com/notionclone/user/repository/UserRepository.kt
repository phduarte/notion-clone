package com.notionclone.user.repository

import com.notionclone.user.entity.User
import com.notionclone.user.entity.UserStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    
    fun findByEmail(email: String): User?
    
    fun findByUsername(username: String): User?
    
    fun existsByEmail(email: String): Boolean
    
    fun existsByUsername(username: String): Boolean
    
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    fun findAllActive(): List<User>
    
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    fun findActiveById(@Param("id") id: UUID): User?
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    fun findActiveByEmail(@Param("email") email: String): User?
    
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deletedAt IS NULL")
    fun findActiveByUsername(@Param("username") username: String): User?
    
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.deletedAt IS NULL")
    fun findByStatus(@Param("status") status: UserStatus): List<User>
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    fun existsActiveByEmail(@Param("email") email: String): Boolean
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.deletedAt IS NULL")
    fun existsActiveByUsername(@Param("username") username: String): Boolean
}
