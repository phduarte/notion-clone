package com.notionclone.common.util

import com.notionclone.common.exception.UnauthorizedException
import com.notionclone.user.entity.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityHelper {
    
    fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw UnauthorizedException("Not authenticated")
        
        return authentication.principal as? User
            ?: throw UnauthorizedException("Invalid authentication")
    }
    
    fun getCurrentUserOrNull(): User? {
        return try {
            getCurrentUser()
        } catch (e: Exception) {
            null
        }
    }
}
