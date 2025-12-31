package com.notionclone.auth.controller

import com.notionclone.auth.dto.*
import com.notionclone.auth.service.AuthService
import com.notionclone.common.util.SecurityHelper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val securityHelper: SecurityHelper
) {
    
    @PostMapping("/register")
    fun register(@Valid @RequestBody dto: RegisterDto): ResponseEntity<AuthResponse> {
        val response = authService.register(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: LoginDto): ResponseEntity<AuthResponse> {
        val response = authService.login(dto)
        return ResponseEntity.ok(response)
    }
    
    @PostMapping("/verify-email")
    fun verifyEmail(@Valid @RequestBody dto: VerifyEmailDto): ResponseEntity<Map<String, String>> {
        val user = securityHelper.getCurrentUser()
        authService.verifyEmail(dto, user)
        return ResponseEntity.ok(mapOf("message" to "Email verified successfully"))
    }
    
    @PostMapping("/resend-code")
    fun resendCode(@Valid @RequestBody dto: ResendCodeDto): ResponseEntity<Map<String, String>> {
        authService.resendCode(dto)
        return ResponseEntity.ok(mapOf("message" to "Verification code sent"))
    }
    
    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody dto: ForgotPasswordDto): ResponseEntity<Map<String, String>> {
        authService.forgotPassword(dto)
        return ResponseEntity.ok(mapOf("message" to "If the email exists, a recovery code will be sent"))
    }
    
    @PostMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody dto: ResetPasswordDto): ResponseEntity<Map<String, String>> {
        authService.resetPassword(dto)
        return ResponseEntity.ok(mapOf("message" to "Password reset successfully"))
    }
    
    @PostMapping("/refresh-token")
    fun refreshToken(@Valid @RequestBody dto: RefreshTokenDto): ResponseEntity<TokenResponse> {
        val response = authService.refreshToken(dto)
        return ResponseEntity.ok(response)
    }
    
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Map<String, String>> {
        val user = securityHelper.getCurrentUser()
        authService.logout(user)
        return ResponseEntity.ok(mapOf("message" to "Logged out successfully"))
    }
}
