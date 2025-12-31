package com.notionclone.common.util

import org.owasp.html.PolicyFactory
import org.owasp.html.Sanitizers
import org.springframework.stereotype.Component

@Component
class HtmlSanitizer {
    
    private val policy: PolicyFactory = Sanitizers.FORMATTING
        .and(Sanitizers.BLOCKS)
        .and(Sanitizers.LINKS)
        .and(Sanitizers.IMAGES)
        .and(Sanitizers.STYLES)
        .and(Sanitizers.TABLES)
    
    fun sanitize(html: String?): String? {
        if (html.isNullOrBlank()) return html
        return policy.sanitize(html)
    }
    
    fun sanitizeStrict(html: String?): String? {
        if (html.isNullOrBlank()) return html
        return Sanitizers.FORMATTING.sanitize(html)
    }
}
