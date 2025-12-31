package com.notionclone.common.util

import org.springframework.stereotype.Component
import java.security.SecureRandom

@Component
class CodeGenerator {
    
    private val random = SecureRandom()
    
    fun generateVerificationCode(length: Int = 6): String {
        val code = StringBuilder()
        repeat(length) {
            code.append(random.nextInt(10))
        }
        return code.toString()
    }
    
    fun generateSlug(title: String): String {
        return title
            .lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "-")
            .replace(Regex("-+"), "-")
            .trim('-')
            .take(100)
    }
    
    fun generateUniqueSlug(baseSlug: String, existingSlugs: Set<String>): String {
        var slug = baseSlug
        var counter = 1
        
        while (slug in existingSlugs) {
            slug = "$baseSlug-$counter"
            counter++
        }
        
        return slug
    }
}
