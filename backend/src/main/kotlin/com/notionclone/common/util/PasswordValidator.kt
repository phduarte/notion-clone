package com.notionclone.common.util

import com.notionclone.common.exception.WeakPasswordException
import org.springframework.stereotype.Component

@Component
class PasswordValidator {
    
    companion object {
        private val SEQUENTIAL_NUMBERS = Regex("(\\d)\\1{2,}")
        private val UPPER_CASE = Regex("[A-Z]")
        private val LOWER_CASE = Regex("[a-z]")
        private val DIGIT = Regex("\\d")
        private val SPECIAL_CHAR = Regex("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")
        private const val MIN_LENGTH = 8
        private const val MAX_LENGTH = 100
    }
    
    fun validate(password: String, name: String? = null, username: String? = null) {
        val errors = mutableListOf<String>()
        
        // Length check
        if (password.length < MIN_LENGTH) {
            errors.add("Password must be at least $MIN_LENGTH characters long")
        }
        
        if (password.length > MAX_LENGTH) {
            errors.add("Password must not exceed $MAX_LENGTH characters")
        }
        
        // Character requirements
        if (!UPPER_CASE.containsMatchIn(password)) {
            errors.add("Password must contain at least one uppercase letter")
        }
        
        if (!LOWER_CASE.containsMatchIn(password)) {
            errors.add("Password must contain at least one lowercase letter")
        }
        
        if (!DIGIT.containsMatchIn(password)) {
            errors.add("Password must contain at least one number")
        }
        
        if (!SPECIAL_CHAR.containsMatchIn(password)) {
            errors.add("Password must contain at least one special character")
        }
        
        // Sequential numbers check
        if (SEQUENTIAL_NUMBERS.containsMatchIn(password)) {
            errors.add("Password must not contain sequential repeated numbers")
        }
        
        // Name check
        if (name != null && password.contains(name, ignoreCase = true)) {
            errors.add("Password must not contain your name")
        }
        
        // Username check
        if (username != null && password.contains(username, ignoreCase = true)) {
            errors.add("Password must not contain your username")
        }
        
        if (errors.isNotEmpty()) {
            throw WeakPasswordException(errors.joinToString("; "))
        }
    }
    
    fun generateStrongPassword(): String {
        val upperCase = ('A'..'Z').toList()
        val lowerCase = ('a'..'z').toList()
        val digits = ('0'..'9').toList()
        val special = "!@#\$%^&*()_+-=[]{}".toList()
        
        val password = StringBuilder()
        password.append(upperCase.random())
        password.append(lowerCase.random())
        password.append(digits.random())
        password.append(special.random())
        
        val allChars = upperCase + lowerCase + digits + special
        repeat(8) {
            password.append(allChars.random())
        }
        
        return password.toString().toList().shuffled().joinToString("")
    }
}
