# GitHub Copilot Instructions - Notion Clone

## Project Overview
Este é um clone do Notion - um sistema de criação e gerenciamento de documentos com suporte a páginas, sub-páginas, edição markdown/WYSIWYG e colaboração em tempo real.

## Tech Stack

### Frontend
- **Framework**: Next.js 14+ (App Router)
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **UI Components**: shadcn/ui
- **Editor**: Tiptap (WYSIWYG editor com suporte markdown)
- **State Management**: Zustand ou React Context
- **Forms**: React Hook Form + Zod
- **HTTP Client**: Axios
- **Real-time**: Socket.io-client

### Backend
- **Language**: Kotlin 1.9+
- **Framework**: Spring Boot 3.2+ (with WebFlux for reactive support)
- **Database**: PostgreSQL 15+
- **ORM**: Spring Data JPA + Hibernate
- **Authentication**: Spring Security + JWT (jjwt library)
- **Validation**: Jakarta Bean Validation (javax.validation)
- **Email**: Spring Mail + Thymeleaf (templates)
- **PDF Generation**: OpenPDF or Flying Saucer
- **Real-time**: Spring WebSocket (STOMP)
- **File Upload**: MultipartFile (Spring Web)

### DevOps & Tools
- **Build Tool**: Gradle (Kotlin DSL)
- **Package Manager**: pnpm (frontend)
- **Linting**: Ktlint + Detekt (backend), ESLint + Prettier (frontend)
- **Testing**: JUnit 5 + MockK (backend), Jest + React Testing Library (frontend)
- **Git Hooks**: Husky
- **API Documentation**: SpringDoc OpenAPI (Swagger)

## Code Standards

### Kotlin
- Use null safety (avoid `!!` operator)
- Prefer data classes for DTOs
- Use sealed classes for state representation
- Prefer `val` over `var` (immutability)
- Use extension functions for utility methods
- Leverage coroutines for async operations
- Use meaningful names (no abbreviations)

### TypeScript
- Use strict mode
- Prefer interfaces over types for object shapes
- Use explicit return types for functions
- Avoid `any` - use `unknown` if type is truly unknown
- Use enums for constants with multiple values

### React/Next.js
- Use functional components with hooks
- Prefer server components when possible (Next.js App Router)
- Use client components only when necessary ('use client')
- Follow the Next.js file-based routing structure
- Keep components small and focused (single responsibility)
- Use custom hooks for reusable logic
- Prefer composition over prop drilling

### Backend/Spring Boot
- Use dependency injection (@Autowired, constructor injection preferred)
- Follow controller-service-repository pattern
- Use DTOs for all input/output (data classes)
- Use @PreAuthorize for authorization
- Use @Valid for validation
- Use @ControllerAdvice for global exception handling
- Use AOP for cross-cutting concerns
- Keep business logic in services, not controllers
- Use reactive programming (WebFlux) for real-time features

### Database/JPA
- Use meaningful entity names (PascalCase)
- Add indexes for frequently queried fields (@Index)
- Use proper JPA relationships (@OneToMany, @ManyToOne, etc.)
- Use Kotlin enums for fixed values
- Use @CreatedDate and @LastModifiedDate with auditing
- Use soft delete with @SQLDelete and @Where annotations
- Always specify fetch types explicitly

### Naming Conventions

#### Frontend (TypeScript)
- **Files**: kebab-case (user-profile.tsx, auth.service.ts)
- **Components**: PascalCase (UserProfile, DocumentEditor)
- **Functions/Variables**: camelCase (getUserById, isAuthenticated)
- **Constants**: UPPER_SNAKE_CASE (MAX_FILE_SIZE, API_URL)
- **Interfaces**: PascalCase with I prefix (IUser, IDocument)
- **Types**: PascalCase (UserRole, PlanType)
- **Enums**: PascalCase (UserStatus, PlanType)

#### Backend (Kotlin)
- **Files**: PascalCase (UserController.kt, AuthService.kt)
- **Classes**: PascalCase (UserService, DocumentRepository)
- **Functions/Variables**: camelCase (getUserById, isAuthenticated)
- **Constants**: UPPER_SNAKE_CASE (companion object)
- **Packages**: lowercase (com.notionclone.auth)
- **Interfaces**: PascalCase (UserRepository, DocumentService)
- **Data Classes**: PascalCase (UserDto, CreateDocumentRequest)
- **Enums**: PascalCase (UserStatus, PlanType)

### File Structure
```
frontend/
├── src/
│   ├── app/              # Next.js App Router pages
│   ├── components/       # Reusable components
│   │   ├── ui/          # shadcn/ui components
│   │   ├── editor/      # Editor components
│   │   ├── auth/        # Auth-related components
│   │   └── shared/      # Shared components
│   ├── hooks/           # Custom hooks
│   ├── lib/             # Utility functions
│   ├── services/        # API services
│   ├── store/           # State management
│   ├── types/           # TypeScript types
│   └── constants/       # Constants and configs

backend/
├── src/main/kotlin/com/notionclone/
│   ├── auth/            # Authentication module
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   ├── filter/
│   │   └── config/
│   ├── user/            # User management
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   └── dto/
│   ├── document/        # Document CRUD
│   ├── plan/            # Plan logic
│   ├── collaboration/   # Real-time features
│   ├── common/          # Shared code
│   │   ├── exception/
│   │   ├── config/
│   │   ├── util/
│   │   └── annotation/
│   └── NotionCloneApplication.kt
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/    # Flyway migrations
```

## Business Rules

### User Management
1. **Registration**:
   - Required fields: name, username, email, password, plan
   - Optional: phone
   - Username must be unique (suggest alternatives like Reddit)
   - Email must be unique
   - Strong password validation (min 8 chars, 1 uppercase, 1 special, 1 number, no sequential numbers, cannot contain name)
   - Use @Valid and custom validators for validation
   - Send email verification code after registration

2. **Authentication**:
   - Use Spring Security with JWT tokens (access + refresh)
   - Access token expires in 15 minutes
   - Refresh token expires in 7 days
   - Store tokens securely (httpOnly cookies)
   - Use BCryptPasswordEncoder for password hashing

3. **Password Recovery**:
   - Send recovery code via email
   - Code expires in 15 minutes
   - Limit attempts to prevent abuse

4. **Account Deletion**:
   - Show confirmation modal
   - Require reason for deletion
   - Soft delete (keep data for 30 days)
   - Log deletion reason in database

### Plans & Permissions
1. **Free Plan**:
   - 1 main page
   - Up to 3 sub-pages
   - No PDF export
   - No collaboration

2. **Pro Plan**:
   - Up to 100 main pages
   - Up to 10 sub-pages per main page
   - PDF export enabled
   - No collaboration

3. **Premium Plan**:
   - Unlimited pages and sub-pages
   - PDF export enabled
   - Real-time collaboration enabled
   - Share pages with other users

### Document Management
1. **Editor Features**:
   - Support markdown shortcuts:
     - `#` + space → H1
     - `##` + space → H2
     - `###` + space → H3
     - `*text*` or `_text_` → italic
     - `**text**` or `__text__` → bold
     - `-` + space → bullet list
     - `1.` + space → numbered list
     - `[ ]` + space → checkbox
     - `` `code` `` → inline code
     - ` ``` ` → code block
     - `>` + space → blockquote

2. **Special Commands**:
   - `/` → Open command palette
   - `/image` → Insert image
   - `/info` → Insert info callout
   - `/warning` → Insert warning callout
   - `/danger` → Insert danger callout
   - `/heading1` → Insert H1
   - `/heading2` → Insert H2
   - `/heading3` → Insert H3
   - `/page` → Create sub-page

3. **Auto-save**:
   - Debounce saves (wait 1 second after last change)
   - Show "Saving..." indicator
   - Show "Saved" when complete
   - Handle conflicts in collaboration mode

4. **Images**:
   - Max size: 5MB
   - Allowed formats: jpg, jpeg, png, gif, webp
   - Store in cloud storage (or local for development)
   - Generate thumbnails for performance

### Onboarding
- Show on first login only
- Dismissable
- Highlight key features:
  - How to create pages
  - Markdown shortcuts
  - Command palette (/)
  - Plan limits

## Error Handling
- Use try-catch blocks
- Return meaningful error messages
- Use appropriate HTTP status codes
- Log errors for debugging
- Never expose sensitive information in errors

## Security

### 🔒 CRITICAL: Security-First Development

**NEVER compromise security for convenience. Security vulnerabilities can lead to data breaches, financial loss, and legal issues.**

### Authentication & Authorization

**✅ DO:**
- Use Spring Security with JWT (jjwt library)
- Hash passwords with BCryptPasswordEncoder (strength 10 minimum)
- Implement refresh token rotation
- Use httpOnly, secure, and SameSite cookies for tokens
- Validate JWT signature and expiration on every request
- Use @PreAuthorize and @Secured for method-level security
- Implement proper role-based access control (RBAC)
- Verify ownership before allowing resource access

**❌ DON'T:**
- Store passwords in plain text or use weak hashing (MD5, SHA1)
- Put tokens in localStorage (vulnerable to XSS)
- Use default or hardcoded secrets
- Allow access without proper authorization checks
- Trust client-side validation alone
- Expose user enumeration (e.g., "email exists" vs "email/password incorrect")

**Example (Secure):**
```kotlin
@Service
class AuthService(
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(email: String, password: String): TokenResponse {
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException() // Generic message
        
        if (!passwordEncoder.matches(password, user.passwordHash)) {
            throw InvalidCredentialsException() // Same generic message
        }
        
        // Generate tokens securely
        return jwtService.generateTokens(user)
    }
}
```

### Input Validation & Sanitization

**✅ DO:**
- Validate ALL inputs on the backend with @Valid and custom validators
- Use Jakarta Bean Validation annotations (@NotNull, @Email, @Size, etc.)
- Sanitize HTML content using OWASP Java HTML Sanitizer
- Whitelist allowed characters/formats instead of blacklisting
- Validate file uploads (type, size, content)
- Check magic bytes for file type validation, not just extension
- Validate and sanitize path parameters to prevent directory traversal
- Use Zod schemas on frontend for early validation

**❌ DON'T:**
- Trust any user input without validation
- Rely solely on frontend validation
- Use blacklists (always incomplete)
- Allow unrestricted file uploads
- Trust file extensions alone
- Use eval() or similar dynamic code execution
- Directly interpolate user input into HTML

**Example (Secure):**
```kotlin
@RestController
class DocumentController {
    
    @PostMapping("/documents")
    fun create(@Valid @RequestBody request: CreateDocumentDto): Document {
        // Validation happens automatically via @Valid
        
        // Additional sanitization for HTML content
        val sanitizedContent = sanitizeHtml(request.content)
        
        // Verify ownership/permissions
        verifyUserCanCreateDocument(getCurrentUser())
        
        return documentService.create(request.copy(content = sanitizedContent))
    }
}

data class CreateDocumentDto(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    val title: String,
    
    @field:Valid
    val content: String,
    
    @field:UUID
    val parentId: UUID? = null
)
```

### SQL Injection Prevention

**✅ DO:**
- Use JPA/Hibernate with parameterized queries
- Use Spring Data JPA query methods
- Use @Query with named parameters (:param)
- Use Criteria API for dynamic queries
- Always validate and sanitize inputs

**❌ DON'T:**
- Build SQL queries with string concatenation
- Use native queries with unsanitized input
- Trust any user input in SQL context

**Example (Secure):**
```kotlin
interface UserRepository : JpaRepository<User, UUID> {
    // ✅ Safe - parameterized
    @Query("SELECT u FROM User u WHERE u.email = :email")
    fun findByEmail(@Param("email") email: String): User?
    
    // ✅ Safe - JPA method
    fun findByUsernameIgnoreCase(username: String): User?
}

// ❌ NEVER DO THIS:
// val query = "SELECT * FROM users WHERE email = '$email'" // SQL INJECTION!
```

### XSS (Cross-Site Scripting) Prevention

**✅ DO:**
- Sanitize HTML content on backend (OWASP Java HTML Sanitizer)
- Use DOMPurify on frontend before rendering user HTML
- Escape output in templates (Thymeleaf does this by default)
- Set Content-Security-Policy headers
- Use textContent instead of innerHTML when possible
- Validate and sanitize rich text editor content

**❌ DON'T:**
- Render unsanitized user input as HTML
- Use dangerouslySetInnerHTML without sanitization
- Trust content from external sources
- Disable XSS protection headers

**Example (Secure):**
```kotlin
import org.owasp.html.PolicyFactory
import org.owasp.html.Sanitizers

@Service
class ContentSanitizer {
    private val policy: PolicyFactory = Sanitizers.FORMATTING
        .and(Sanitizers.BLOCKS)
        .and(Sanitizers.LINKS)
        .and(Sanitizers.IMAGES)
    
    fun sanitize(html: String): String {
        return policy.sanitize(html)
    }
}

// Frontend (React/Next.js)
import DOMPurify from 'dompurify'

function DocumentViewer({ content }: { content: string }) {
    const sanitized = DOMPurify.sanitize(content)
    return <div dangerouslySetInnerHTML={{ __html: sanitized }} />
}
```

### CSRF Protection

**✅ DO:**
- Enable CSRF protection in Spring Security
- Use CSRF tokens for state-changing operations
- Validate tokens on backend
- Use SameSite cookie attribute
- Implement custom CSRF for SPAs if needed

**❌ DON'T:**
- Disable CSRF protection without good reason
- Use GET for state-changing operations
- Trust Origin/Referer headers alone

**Example (Secure):**
```kotlin
@Configuration
@EnableWebSecurity
class SecurityConfig {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringRequestMatchers("/api/auth/login", "/api/auth/register")
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/public/**").permitAll()
                    .anyRequest().authenticated()
            }
        
        return http.build()
    }
}
```

### CORS Configuration

**✅ DO:**
- Explicitly whitelist allowed origins
- Restrict to specific domains (not "*" in production)
- Allow only necessary HTTP methods
- Set allowCredentials carefully
- Review CORS config regularly

**❌ DON'T:**
- Use "*" wildcard for origins in production
- Allow all origins without validation
- Allow credentials with wildcard origins

**Example (Secure):**
```kotlin
@Configuration
class WebConfig : WebMvcConfigurer {
    
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:3000", // Development
                "https://notion-clone.com" // Production
            )
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}
```

### Rate Limiting

**✅ DO:**
- Implement rate limiting for all public endpoints
- Stricter limits for authentication endpoints
- Use Bucket4j or custom filters
- Rate limit by IP and user
- Return 429 Too Many Requests
- Log rate limit violations

**❌ DON'T:**
- Leave endpoints unprotected from brute force
- Use the same limits for all endpoints
- Forget about distributed rate limiting (Redis)

**Example (Secure):**
```kotlin
@Configuration
class RateLimitConfig {
    
    @Bean
    fun loginRateLimiter(): RateLimiter {
        return RateLimiter.create(
            maxAttempts = 5,
            duration = Duration.ofMinutes(15),
            endpoints = listOf("/api/auth/login", "/api/auth/register")
        )
    }
}

// Custom annotation
@RateLimit(maxRequests = 10, window = "1m")
@GetMapping("/api/documents")
fun listDocuments(): List<Document> {
    // ...
}
```

### Secrets Management

**✅ DO:**
- Use environment variables for secrets
- Use Spring profiles (dev, prod)
- Use secret management services (AWS Secrets Manager, HashiCorp Vault)
- Rotate secrets regularly
- Use different secrets per environment
- Never commit secrets to git
- Use strong, random secrets (min 32 characters)

**❌ DON'T:**
- Hardcode secrets in code
- Commit .env files or application-prod.yml
- Share secrets via chat/email
- Use weak or default secrets
- Store secrets in client-side code
- Log secrets

**Example (Secure):**
```yaml
# application.yml (versioned - no secrets)
app:
  jwt:
    secret: ${JWT_SECRET}  # From environment variable
    expiration: 900000

# application-prod.yml (NOT versioned, .gitignore)
# Contains actual secrets, never commit
```

### File Upload Security

**✅ DO:**
- Validate file type by magic bytes, not extension
- Enforce file size limits
- Store uploads outside web root
- Generate unique filenames (UUID)
- Scan uploads for malware (ClamAV)
- Validate image dimensions
- Strip metadata (EXIF)
- Use presigned URLs for cloud storage

**❌ DON'T:**
- Trust file extensions
- Allow unlimited file sizes
- Store files in publicly accessible directories
- Use original filenames
- Execute uploaded files
- Allow executable types (exe, sh, bat)

**Example (Secure):**
```kotlin
@Service
class FileUploadService {
    
    private val allowedMimeTypes = setOf(
        "image/jpeg",
        "image/png",
        "image/gif",
        "image/webp"
    )
    
    fun upload(file: MultipartFile): String {
        // Validate size
        if (file.size > 5_242_880) { // 5MB
            throw FileTooLargeException()
        }
        
        // Validate MIME type by content, not extension
        val mimeType = detectMimeType(file.inputStream)
        if (mimeType !in allowedMimeTypes) {
            throw InvalidFileTypeException()
        }
        
        // Generate secure filename
        val extension = file.originalFilename?.substringAfterLast('.') ?: "jpg"
        val filename = "${UUID.randomUUID()}.$extension"
        
        // Store outside web root
        val path = Paths.get(uploadDir, filename)
        Files.copy(file.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
        
        return filename
    }
    
    private fun detectMimeType(input: InputStream): String {
        return URLConnection.guessContentTypeFromStream(input) ?: "application/octet-stream"
    }
}
```

### Session Management

**✅ DO:**
- Implement session timeout
- Regenerate session ID after login
- Invalidate sessions on logout
- Use secure session storage (Redis)
- Track active sessions
- Implement concurrent session control
- Log session activities

**❌ DON'T:**
- Use predictable session IDs
- Store sensitive data in sessions
- Allow infinite session duration
- Forget to clean up expired sessions

### Logging & Monitoring

**✅ DO:**
- Log security events (login, failed attempts, privilege escalation)
- Log errors without exposing sensitive data
- Use structured logging
- Monitor for suspicious patterns
- Implement alerting for security events
- Rotate logs regularly
- Secure log storage

**❌ DON'T:**
- Log passwords, tokens, or PII
- Expose stack traces to users
- Log sensitive request/response data
- Store logs indefinitely without rotation

**Example (Secure):**
```kotlin
@Service
class SecurityLogger {
    
    private val logger = LoggerFactory.getLogger(javaClass)
    
    fun logLoginAttempt(email: String, success: Boolean, ip: String) {
        if (success) {
            logger.info("Login successful for user: $email from IP: $ip")
        } else {
            logger.warn("Login failed for email: ${maskEmail(email)} from IP: $ip")
        }
    }
    
    fun logAccessDenied(userId: UUID, resource: String, action: String) {
        logger.warn("Access denied for user: $userId to $action $resource")
    }
    
    private fun maskEmail(email: String): String {
        val parts = email.split("@")
        return "${parts[0].take(2)}***@${parts[1]}"
    }
}
```

### API Security

**✅ DO:**
- Use HTTPS in production (TLS 1.2+)
- Implement API versioning
- Document API with OpenAPI/Swagger
- Use API keys for external integrations
- Implement request signing for critical operations
- Validate Content-Type headers
- Return appropriate HTTP status codes

**❌ DON'T:**
- Expose internal error details in responses
- Return stack traces to clients
- Use HTTP in production
- Expose unnecessary endpoints
- Leak information through different error messages

### Dependency Security

**✅ DO:**
- Keep dependencies up to date
- Use Dependabot or similar
- Review security advisories
- Use verified libraries
- Scan for vulnerabilities (OWASP Dependency-Check)
- Lock dependency versions
- Audit new dependencies

**❌ DON'T:**
- Use outdated or unmaintained libraries
- Blindly update without testing
- Use libraries with known vulnerabilities
- Trust all npm/Maven packages

### Security Headers

**✅ DO:**
- Set security headers (Spring Security does most)
  - X-Content-Type-Options: nosniff
  - X-Frame-Options: DENY
  - X-XSS-Protection: 1; mode=block
  - Content-Security-Policy
  - Strict-Transport-Security (HSTS)
- Test headers with securityheaders.com

**❌ DON'T:**
- Omit security headers
- Allow clickjacking
- Trust browser defaults

**Example (Secure):**
```kotlin
@Configuration
class SecurityHeadersConfig {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .headers { headers ->
                headers
                    .contentSecurityPolicy { csp ->
                        csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; img-src 'self' data: https:;")
                    }
                    .frameOptions { frame -> frame.deny() }
                    .httpStrictTransportSecurity { hsts ->
                        hsts.maxAgeInSeconds(31536000)
                            .includeSubDomains(true)
                    }
            }
        
        return http.build()
    }
}
```

### Security Checklist for Every Feature

Before merging ANY code, verify:
- [ ] All inputs are validated on backend
- [ ] HTML content is sanitized
- [ ] SQL queries are parameterized
- [ ] Authorization is checked (@PreAuthorize or manual)
- [ ] Sensitive data is not logged
- [ ] Error messages don't leak information
- [ ] Rate limiting is applied
- [ ] CORS is configured correctly
- [ ] CSRF tokens are validated
- [ ] Secrets are in environment variables
- [ ] File uploads are validated securely
- [ ] Security headers are set
- [ ] Tests include security scenarios

### Common Vulnerabilities to Avoid

1. **SQL Injection** - Use JPA parameterized queries
2. **XSS** - Sanitize all user-generated HTML
3. **CSRF** - Use CSRF tokens for state changes
4. **Broken Authentication** - Strong passwords, JWT best practices
5. **Sensitive Data Exposure** - Encrypt at rest and in transit
6. **Broken Access Control** - Verify ownership/permissions
7. **Security Misconfiguration** - Review all configs
8. **XXE** - Disable external entity processing in XML
9. **Insecure Deserialization** - Validate deserialized objects
10. **Insufficient Logging** - Log security events

### Security Resources

- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [OWASP Cheat Sheet Series](https://cheatsheetseries.owasp.org/)
- [CWE Top 25](https://cwe.mitre.org/top25/)

**Remember: Security is not a feature, it's a requirement. Always code defensively.**

## Performance
- Lazy load components
- Implement pagination for lists
- Use React.memo for expensive components
- Optimize images
- Use database indexes
- Cache frequently accessed data
- Debounce auto-save and search

## Accessibility
- Use semantic HTML
- Add ARIA labels where needed
- Support keyboard navigation
- Ensure proper color contrast
- Add alt text to images

## Testing
- Write unit tests for business logic
- Write integration tests for API endpoints
- Write E2E tests for critical user flows
- Aim for 80%+ code coverage

## Git Commit Messages
Follow conventional commits:
- `feat: add user registration`
- `fix: resolve auto-save bug`
- `docs: update README`
- `style: format code`
- `refactor: improve auth service`
- `test: add user tests`
- `chore: update dependencies`

## Comments
- Write self-documenting code
- Add comments only when logic is complex
- Use JSDoc for public APIs
- Explain "why", not "what"

## When Suggesting Code
- Follow all the conventions above
- Provide complete, working examples
- Include error handling
- Add TypeScript types
- Consider edge cases
- Think about performance and security
