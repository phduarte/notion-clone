package com.notionclone.auth.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    
    fun sendVerificationEmail(to: String, name: String, code: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Verify your email - Notion Clone"
        message.text = """
            Hello $name,
            
            Thank you for registering! Please use the following code to verify your email:
            
            $code
            
            This code will expire in 15 minutes.
            
            If you didn't create an account, please ignore this email.
            
            Best regards,
            Notion Clone Team
        """.trimIndent()
        
        mailSender.send(message)
    }
    
    fun sendPasswordRecoveryEmail(to: String, name: String, code: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Password Recovery - Notion Clone"
        message.text = """
            Hello $name,
            
            We received a request to reset your password. Please use the following code:
            
            $code
            
            This code will expire in 15 minutes.
            
            If you didn't request a password reset, please ignore this email and ensure your account is secure.
            
            Best regards,
            Notion Clone Team
        """.trimIndent()
        
        mailSender.send(message)
    }
    
    fun sendAccountDeletionConfirmation(to: String, name: String, deletionDate: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Account Deletion Scheduled - Notion Clone"
        message.text = """
            Hello $name,
            
            Your account has been scheduled for deletion.
            
            Your data will be permanently deleted on: $deletionDate
            
            If you change your mind, you can cancel the deletion by logging in before this date.
            
            Best regards,
            Notion Clone Team
        """.trimIndent()
        
        mailSender.send(message)
    }
}
