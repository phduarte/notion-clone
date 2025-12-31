package com.notionclone.auth.dto

import com.notionclone.user.entity.PlanType
import jakarta.validation.constraints.*

data class RegisterDto(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,
    
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, hyphens and underscores")
    val username: String,
    
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    @field:Size(max = 255, message = "Email must not exceed 255 characters")
    val email: String,
    
    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be a valid international number")
    val phone: String? = null,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val password: String,
    
    @field:NotNull(message = "Plan is required")
    val plan: PlanType
)

data class LoginDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String
)

data class VerifyEmailDto(
    @field:NotBlank(message = "Code is required")
    @field:Size(min = 6, max = 6, message = "Code must be 6 digits")
    val code: String
)

data class ResendCodeDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String
)

data class ForgotPasswordDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String
)

data class ResetPasswordDto(
    @field:NotBlank(message = "Code is required")
    @field:Size(min = 6, max = 6, message = "Code must be 6 digits")
    val code: String,
    
    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val newPassword: String
)

data class ChangePasswordDto(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,
    
    @field:NotBlank(message = "New password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    val newPassword: String
)

data class RefreshTokenDto(
    @field:NotBlank(message = "Refresh token is required")
    val refreshToken: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val type: String = "Bearer",
    val expiresIn: Long
)

data class AuthResponse(
    val user: UserDto,
    val tokens: TokenResponse
)

data class UserDto(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String?,
    val plan: PlanType,
    val avatar: String?,
    val emailVerified: Boolean,
    val firstLogin: Boolean,
    val createdAt: String
)
