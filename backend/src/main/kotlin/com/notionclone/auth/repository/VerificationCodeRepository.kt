package com.notionclone.auth.repository

import com.notionclone.auth.entity.CodeType
import com.notionclone.auth.entity.VerificationCode
import com.notionclone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface VerificationCodeRepository : JpaRepository<VerificationCode, UUID> {
    
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.user = :user AND vc.type = :type AND vc.used = false AND vc.expiresAt > :now ORDER BY vc.createdAt DESC")
    fun findValidCodeByUserAndType(
        @Param("user") user: User,
        @Param("type") type: CodeType,
        @Param("now") now: LocalDateTime = LocalDateTime.now()
    ): VerificationCode?
    
    @Query("SELECT vc FROM VerificationCode vc WHERE vc.code = :code AND vc.type = :type AND vc.used = false AND vc.expiresAt > :now")
    fun findValidCodeByCodeAndType(
        @Param("code") code: String,
        @Param("type") type: CodeType,
        @Param("now") now: LocalDateTime = LocalDateTime.now()
    ): VerificationCode?
    
    @Query("DELETE FROM VerificationCode vc WHERE vc.user = :user AND vc.type = :type")
    fun deleteByUserAndType(@Param("user") user: User, @Param("type") type: CodeType)
    
    @Query("DELETE FROM VerificationCode vc WHERE vc.expiresAt < :now OR vc.used = true")
    fun deleteExpiredAndUsed(@Param("now") now: LocalDateTime = LocalDateTime.now())
}
