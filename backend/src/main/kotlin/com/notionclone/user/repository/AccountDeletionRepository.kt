package com.notionclone.user.repository

import com.notionclone.user.entity.AccountDeletion
import com.notionclone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface AccountDeletionRepository : JpaRepository<AccountDeletion, UUID> {
    
    fun findByUser(user: User): AccountDeletion?
    
    @Query("SELECT ad FROM AccountDeletion ad WHERE ad.permanentDeletionAt <= :now")
    fun findReadyForPermanentDeletion(@Param("now") now: LocalDateTime = LocalDateTime.now()): List<AccountDeletion>
}
