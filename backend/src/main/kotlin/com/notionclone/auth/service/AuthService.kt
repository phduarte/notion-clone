package com.notionclone.auth.service

import com.notionclone.auth.dto.*
import com.notionclone.auth.entity.CodeType
import com.notionclone.auth.entity.RefreshToken
import com.notionclone.auth.entity.VerificationCode
import com.notionclone.auth.repository.RefreshTokenRepository
import com.notionclone.auth.repository.VerificationCodeRepository
import com.notionclone.common.exception.*
import com.notionclone.common.util.CodeGenerator
import com.notionclone.common.util.JwtUtil
import com.notionclone.common.util.PasswordValidator
import com.notionclone.user.entity.User
import com.notionclone.user.entity.UserStatus
import com.notionclone.user.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val verificationCodeRepository: VerificationCodeRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val passwordValidator: PasswordValidator,
    private val jwtUtil: JwtUtil,
    private val codeGenerator: CodeGenerator,
    private val emailService: EmailService
) {
    
    fun register(dto: RegisterDto): AuthResponse {
        // Check if email already exists
        if (userRepository.existsActiveByEmail(dto.email)) {
            throw EmailAlreadyExistsException()
        }
        
        // Check if username already exists
        if (userRepository.existsActiveByUsername(dto.username)) {
            throw UsernameAlreadyExistsException()
        }
        
        // Validate password
        passwordValidator.validate(dto.password, dto.name, dto.username)
        
        // Create user
        val user = User(
            name = dto.name,
            username = dto.username,
            email = dto.email,
            phone = dto.phone,
            passwordHash = passwordEncoder.encode(dto.password),
            plan = dto.plan,
            status = UserStatus.PENDING_VERIFICATION,
            emailVerified = false,
            firstLogin = true
        )
        
        val savedUser = userRepository.save(user)
        
        // Generate verification code
        val code = codeGenerator.generateVerificationCode()
        val verificationCode = VerificationCode(
            user = savedUser,
            code = code,
            type = CodeType.EMAIL_VERIFICATION,
            expiresAt = LocalDateTime.now().plusMinutes(15)
        )
        verificationCodeRepository.save(verificationCode)
        
        // Send verification email
        emailService.sendVerificationEmail(savedUser.email, savedUser.name, code)
        
        // Generate tokens
        val tokens = generateTokens(savedUser)
        
        return AuthResponse(
            user = savedUser.toDto(),
            tokens = tokens
        )
    }
    
    fun login(dto: LoginDto): AuthResponse {
        // Find user by email
        val user = userRepository.findActiveByEmail(dto.email)
            ?: throw InvalidCredentialsException()
        
        // Check if account is blocked
        if (user.blockedUntil != null && user.blockedUntil!!.isAfter(LocalDateTime.now())) {
            throw AccountBlockedException(user.blockedUntil.toString())
        }
        
        // Check password
        if (!passwordEncoder.matches(dto.password, user.passwordHash)) {
            // Increment failed attempts
            user.failedLoginAttempts++
            
            // Block account after 5 failed attempts
            if (user.failedLoginAttempts >= 5) {
                user.blockedUntil = LocalDateTime.now().plusMinutes(15)
                user.status = UserStatus.BLOCKED
            }
            
            userRepository.save(user)
            throw InvalidCredentialsException()
        }
        
        // Check if email is verified (skip for testing)
        // if (!user.emailVerified) {
        //     throw EmailNotVerifiedException()
        // }
        
        // Check account status
        when (user.status) {
            UserStatus.SUSPENDED -> throw AccountSuspendedException()
            UserStatus.DELETED -> throw AccountDeletedException()
            else -> {}
        }
        
        // Reset failed attempts
        user.failedLoginAttempts = 0
        user.blockedUntil = null
        if (user.status == UserStatus.BLOCKED) {
            user.status = UserStatus.ACTIVE
        }
        userRepository.save(user)
        
        // Generate tokens
        val tokens = generateTokens(user)
        
        return AuthResponse(
            user = user.toDto(),
            tokens = tokens
        )
    }
    
    fun verifyEmail(dto: VerifyEmailDto, user: User) {
        val verificationCode = verificationCodeRepository.findValidCodeByCodeAndType(
            dto.code,
            CodeType.EMAIL_VERIFICATION
        ) ?: throw InvalidVerificationCodeException()
        
        if (verificationCode.user.id != user.id) {
            throw InvalidVerificationCodeException()
        }
        
        if (verificationCode.attempts >= 3) {
            throw TooManyAttemptsException()
        }
        
        if (verificationCode.used) {
            throw CodeAlreadyUsedException()
        }
        
        // Mark code as used
        verificationCode.used = true
        verificationCode.usedAt = LocalDateTime.now()
        verificationCodeRepository.save(verificationCode)
        
        // Update user
        user.emailVerified = true
        if (user.status == UserStatus.PENDING_VERIFICATION) {
            user.status = UserStatus.ACTIVE
        }
        userRepository.save(user)
    }
    
    fun resendCode(dto: ResendCodeDto) {
        val user = userRepository.findActiveByEmail(dto.email)
            ?: throw UserNotFoundException()
        
        if (user.emailVerified) {
            throw ValidationException("Email already verified")
        }
        
        // Delete old codes
        verificationCodeRepository.deleteByUserAndType(user, CodeType.EMAIL_VERIFICATION)
        
        // Generate new code
        val code = codeGenerator.generateVerificationCode()
        val verificationCode = VerificationCode(
            user = user,
            code = code,
            type = CodeType.EMAIL_VERIFICATION,
            expiresAt = LocalDateTime.now().plusMinutes(15)
        )
        verificationCodeRepository.save(verificationCode)
        
        // Send email
        emailService.sendVerificationEmail(user.email, user.name, code)
    }
    
    fun forgotPassword(dto: ForgotPasswordDto) {
        val user = userRepository.findActiveByEmail(dto.email)
            ?: return // Don't reveal if user exists
        
        // Delete old codes
        verificationCodeRepository.deleteByUserAndType(user, CodeType.PASSWORD_RECOVERY)
        
        // Generate new code
        val code = codeGenerator.generateVerificationCode()
        val verificationCode = VerificationCode(
            user = user,
            code = code,
            type = CodeType.PASSWORD_RECOVERY,
            expiresAt = LocalDateTime.now().plusMinutes(15)
        )
        verificationCodeRepository.save(verificationCode)
        
        // Send email
        emailService.sendPasswordRecoveryEmail(user.email, user.name, code)
    }
    
    fun resetPassword(dto: ResetPasswordDto) {
        val verificationCode = verificationCodeRepository.findValidCodeByCodeAndType(
            dto.code,
            CodeType.PASSWORD_RECOVERY
        ) ?: throw InvalidVerificationCodeException()
        
        if (verificationCode.attempts >= 3) {
            throw TooManyAttemptsException()
        }
        
        if (verificationCode.used) {
            throw CodeAlreadyUsedException()
        }
        
        val user = verificationCode.user
        
        // Validate new password
        passwordValidator.validate(dto.newPassword, user.name, user.username)
        
        // Update password
        user.passwordHash = passwordEncoder.encode(dto.newPassword)
        userRepository.save(user)
        
        // Mark code as used
        verificationCode.used = true
        verificationCode.usedAt = LocalDateTime.now()
        verificationCodeRepository.save(verificationCode)
        
        // Revoke all refresh tokens
        refreshTokenRepository.revokeAllByUser(user)
    }
    
    fun refreshToken(dto: RefreshTokenDto): TokenResponse {
        val refreshToken = refreshTokenRepository.findValidToken(dto.refreshToken)
            ?: throw InvalidTokenException()
        
        val user = refreshToken.user
        
        // Generate new tokens
        return generateTokens(user)
    }
    
    fun logout(user: User) {
        refreshTokenRepository.revokeAllByUser(user)
    }
    
    private fun generateTokens(user: User): TokenResponse {
        val accessToken = jwtUtil.generateAccessToken(user.id, user.email)
        val refreshTokenString = jwtUtil.generateRefreshToken(user.id)
        
        // Save refresh token
        val refreshToken = RefreshToken(
            token = refreshTokenString,
            user = user,
            expiresAt = LocalDateTime.now().plusSeconds(jwtUtil.getRefreshTokenExpiration() / 1000)
        )
        refreshTokenRepository.save(refreshToken)
        
        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshTokenString,
            expiresIn = jwtUtil.getAccessTokenExpiration() / 1000
        )
    }
    
    private fun User.toDto() = UserDto(
        id = id.toString(),
        name = name,
        username = username,
        email = email,
        phone = phone,
        plan = plan,
        avatar = avatar,
        emailVerified = emailVerified,
        firstLogin = firstLogin,
        createdAt = createdAt.toString()
    )
}
