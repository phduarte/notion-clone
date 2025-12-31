package com.notionclone.user.dto

import com.notionclone.user.entity.PlanType
import jakarta.validation.constraints.*

data class UpdateProfileDto(
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String?,
    
    @field:Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, hyphens and underscores")
    val username: String?,
    
    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be a valid international number")
    val phone: String?,
    
    val avatar: String?,
    
    val emailNotifications: Boolean?,
    
    val marketingEmails: Boolean?
)

data class UpdatePlanDto(
    @field:NotNull(message = "Plan is required")
    val plan: PlanType
)

data class DeleteAccountDto(
    @field:NotBlank(message = "Reason is required")
    @field:Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    val reason: String,
    
    @field:Size(max = 2000, message = "Feedback must not exceed 2000 characters")
    val feedback: String?
)

data class UserResponse(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String?,
    val plan: PlanType,
    val avatar: String?,
    val status: String,
    val emailVerified: Boolean,
    val firstLogin: Boolean,
    val emailNotifications: Boolean,
    val marketingEmails: Boolean,
    val createdAt: String,
    val updatedAt: String
)

data class UserSuggestionDto(
    val username: String,
    val available: Boolean
)
