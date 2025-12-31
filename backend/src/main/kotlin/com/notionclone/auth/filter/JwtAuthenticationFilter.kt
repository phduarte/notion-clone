package com.notionclone.auth.filter

import com.notionclone.common.exception.InvalidTokenException
import com.notionclone.common.util.JwtUtil
import com.notionclone.user.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository
) : OncePerRequestFilter() {
    
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractTokenFromRequest(request)
            
            if (token != null && jwtUtil.validateToken(token)) {
                val tokenType = jwtUtil.getTokenType(token)
                
                if (tokenType == "access") {
                    val userId = jwtUtil.getUserIdFromToken(token)
                    
                    if (userId != null) {
                        val user = userRepository.findActiveById(userId)
                        
                        if (user != null) {
                            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                            val authentication = UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                authorities
                            )
                            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authentication
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // Log error but don't throw - let request continue without authentication
            logger.error("JWT authentication error: ${ex.message}")
        }
        
        filterChain.doFilter(request, response)
    }
    
    private fun extractTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}
