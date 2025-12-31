package com.notionclone.common.exception

// Base exceptions
open class BusinessException(message: String) : RuntimeException(message)
open class NotFoundException(message: String) : RuntimeException(message)
open class ValidationException(message: String) : RuntimeException(message)
open class UnauthorizedException(message: String) : RuntimeException(message)
open class ForbiddenException(message: String) : RuntimeException(message)

// Authentication exceptions
class InvalidCredentialsException : UnauthorizedException("Invalid email or password")
class EmailNotVerifiedException : UnauthorizedException("Please verify your email before logging in")
class AccountBlockedException(val blockedUntil: String) : UnauthorizedException("Account temporarily blocked due to multiple failed login attempts. Try again after $blockedUntil")
class AccountSuspendedException : UnauthorizedException("Account suspended. Please contact support")
class AccountDeletedException : UnauthorizedException("Account has been deleted")
class InvalidTokenException : UnauthorizedException("Invalid or expired token")
class TokenExpiredException : UnauthorizedException("Token has expired")

// User exceptions
class UserNotFoundException : NotFoundException("User not found")
class EmailAlreadyExistsException : ValidationException("Email already in use")
class UsernameAlreadyExistsException : ValidationException("Username already taken")
class WeakPasswordException(message: String) : ValidationException(message)

// Verification code exceptions
class InvalidVerificationCodeException : ValidationException("Invalid or expired verification code")
class TooManyAttemptsException : ValidationException("Too many attempts. Please request a new code")
class CodeAlreadyUsedException : ValidationException("This code has already been used")

// Document exceptions
class DocumentNotFoundException : NotFoundException("Document not found")
class DocumentAccessDeniedException : ForbiddenException("You don't have permission to access this document")
class MaxPagesExceededException(message: String) : BusinessException(message)
class MaxSubPagesExceededException(message: String) : BusinessException(message)
class InvalidParentException : ValidationException("Invalid parent document")
class CannotDeleteParentWithChildrenException : ValidationException("Cannot delete a document with sub-pages. Delete sub-pages first")
class SlugAlreadyInUseException : ValidationException("This slug is already in use")

// Plan exceptions
class PlanLimitExceededException(message: String) : BusinessException(message)
class FeatureNotAvailableException(feature: String) : BusinessException("Feature '$feature' is not available in your plan. Upgrade to access it")

// File upload exceptions
class FileTooLargeException : ValidationException("File size exceeds the maximum allowed")
class InvalidFileTypeException : ValidationException("File type not allowed")
class FileUploadException(message: String) : BusinessException(message)

// Rate limiting exception
class RateLimitExceededException : BusinessException("Too many requests. Please try again later")
