# 🐛 Guia de Troubleshooting

Este guia contém soluções para problemas comuns que você pode encontrar ao desenvolver, executar ou fazer deploy do Notion Clone.

## 📑 Índice

- [Banco de Dados](#banco-de-dados)
- [Backend (Kotlin/Spring Boot)](#backend-kotlinspring-boot)
- [Frontend (Next.js)](#frontend-nextjs)
- [Docker](#docker)
- [Rede e Conectividade](#rede-e-conectividade)
- [Segurança e Autenticação](#segurança-e-autenticação)
- [Performance e Recursos](#performance-e-recursos)
- [Limpeza e Reset](#limpeza-e-reset)

---

## Banco de Dados

### Erro de conexão com o banco de dados

```bash
# Verifique se o PostgreSQL está rodando
# Windows:
services.msc  # Procure por PostgreSQL

# Linux/Mac:
sudo systemctl status postgresql

# Teste a conexão
psql -U postgres -d notion_clone
```

**Possíveis causas:**
- PostgreSQL não está instalado ou não está rodando
- Credenciais incorretas no `application.yml`
- Firewall bloqueando a porta 5432
- Banco de dados não foi criado

### Flyway migration falhou

```bash
# Verifique o status das migrations
cd backend
./gradlew flywayInfo

# Se houver migration com falha, repare:
./gradlew flywayRepair

# Ou limpe tudo e reaplique (⚠️ CUIDADO: apaga dados!)
./gradlew flywayClean flywayMigrate
```

**Dicas:**
- Sempre faça backup antes de rodar `flywayClean`
- Verifique se há erros de sintaxe SQL nos arquivos de migration
- Certifique-se de que as migrations estão na ordem correta (V1, V2, V3...)

### Erro "Table already exists" ao iniciar o backend

```bash
# O Hibernate está tentando criar tabelas que já existem
# Configure o application.yml para usar Flyway ao invés do Hibernate DDL:

# Em application.yml, certifique-se de ter:
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Deve ser 'validate', não 'create' ou 'update'
  flyway:
    enabled: true
```

### Banco de dados não persiste dados (Docker)

```bash
# Verifique os volumes
docker volume ls

# O volume deve aparecer como: notion-clone_postgres_data
# NUNCA use: docker-compose down -v (apaga os dados!)
```

**Para forçar persistência:**
```yaml
# No docker-compose.yml, certifique-se de ter:
volumes:
  postgres_data:
    driver: local
```

---

## Backend (Kotlin/Spring Boot)

### Erro ao compilar código Kotlin

```bash
cd backend
./gradlew clean build --refresh-dependencies
```

### JDK não encontrado ou versão incorreta

```bash
# Verifique a versão do Java instalada
java -version

# Deve ser Java 21 ou superior
# Se não tiver, baixe em: https://www.oracle.com/java/technologies/downloads/

# Windows - Configure JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-21"
setx PATH "%PATH%;%JAVA_HOME%\bin"

# Linux/Mac - Adicione ao ~/.bashrc ou ~/.zshrc
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Erro "Could not resolve dependencies" no Gradle

```bash
# Limpe o cache do Gradle
cd backend
./gradlew clean --refresh-dependencies

# Se o problema persistir, delete o cache manualmente
# Windows:
rmdir /s /q %USERPROFILE%\.gradle\caches

# Linux/Mac:
rm -rf ~/.gradle/caches

# Execute o build novamente
./gradlew build
```

### Erro de permissão no gradlew (Linux/Mac)

```bash
# Dê permissão de execução ao gradlew
cd backend
chmod +x gradlew
./gradlew bootRun
```

### Prisma/Hibernate não encontra entidades

```bash
# Certifique-se de que as entidades estão anotadas:
@Entity
@Table(name = "users")
data class User(...)

# E que o pacote está sendo escaneado:
@SpringBootApplication
@EntityScan("com.notionclone.*.entity")
class NotionCloneApplication
```

### Erro "Cannot deserialize value" em DTOs

```kotlin
# Verifique se os DTOs têm valores default ou são nullable:
data class CreateDocumentDto(
    val title: String,
    val parentId: UUID? = null,  // Nullable com default
    val icon: String = "📄"       // Default
)

# E que tem anotações Jackson se necessário:
@JsonProperty("parent_id")
val parentId: UUID? = null
```

### Swagger UI não abre

```bash
# Verifique se SpringDoc está configurado:
# build.gradle.kts:
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

# Acesse: http://localhost:8080/swagger-ui.html
# Ou: http://localhost:8080/v3/api-docs

# Se não funcionar, verifique SecurityConfig para permitir acesso:
.authorizeHttpRequests { auth ->
    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
}
```

### Erro de memória ao buildar (OutOfMemoryError)

```bash
# Aumente a memória do Gradle
# Em gradle.properties:
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m

# Ou via linha de comando:
./gradlew build -Dorg.gradle.jvmargs="-Xmx2048m"
```

### Logs não aparecem

```xml
<!-- Configure o logback no backend -->
<!-- Em src/main/resources/logback-spring.xml: -->
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
    </root>
    
    <logger name="com.notionclone" level="DEBUG" />
</configuration>
```

---

## Frontend (Next.js)

### Next.js "Error: EADDRINUSE: address already in use :::3000"

```bash
# Porta 3000 já está em uso

# Encontre e mate o processo (Windows):
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/Mac:
lsof -ti:3000 | xargs kill -9

# Ou use outra porta:
PORT=3001 pnpm dev
```

### Hot reload não funciona no Next.js

```javascript
// 1. Verifique se está usando WSL no Windows
// Adicione ao next.config.js:
module.exports = {
  webpack: (config) => {
    config.watchOptions = {
      poll: 1000,
      aggregateTimeout: 300,
    }
    return config
  },
}

// 2. Ou use Fast Refresh:
// Em next.config.js:
experimental: {
  reactRefresh: true,
}
```

### Erro ao fazer build do frontend

```bash
# Limpe o cache e reinstale
cd frontend
rm -rf .next/
rm -rf node_modules/
pnpm install
pnpm build
```

---

## Docker

### Porta já está em uso

```bash
# Windows
netstat -ano | findstr :3000
netstat -ano | findstr :8080
netstat -ano | findstr :5432
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :3000
lsof -i :8080
lsof -i :5432
kill -9 <PID>

# Ou altere as portas no docker-compose.yml
```

### Contêiner reiniciando constantemente

```bash
# Veja os logs para identificar o erro
docker-compose logs backend

# Erros comuns:
# - Variáveis de ambiente faltando (verifique .env)
# - Banco de dados não está pronto (aguarde 10-15 segundos)
# - Erro nas migrations (verifique SQL)
```

### Build muito lento

```bash
# Use o cache do Docker
docker-compose build --parallel

# Se persistir, limpe o cache
docker builder prune -a
docker-compose build --no-cache
```

### Erro "Cannot connect to Docker daemon"

```bash
# Windows/Mac: Verifique se o Docker Desktop está rodando

# Linux: Inicie o serviço
sudo systemctl start docker
sudo systemctl enable docker

# Adicione seu usuário ao grupo docker
sudo usermod -aG docker $USER
# Faça logout e login novamente
```

### Erro ao fazer build do backend (Docker)

```bash
# Limpe o cache do Gradle dentro do container
docker-compose exec backend ./gradlew clean

# Ou reconstrua do zero
docker-compose down
docker-compose build --no-cache backend
docker-compose up -d
```

### Migrations não executam (Docker)

```bash
# Execute manualmente
docker-compose exec backend ./gradlew flywayMigrate

# Se falhar, verifique o status
docker-compose exec backend ./gradlew flywayInfo

# Repare se necessário
docker-compose exec backend ./gradlew flywayRepair
```

### Espaço em disco esgotado

```bash
# Veja o uso de espaço
docker system df

# Limpe recursos não utilizados
docker system prune -a --volumes

# ⚠️ Cuidado: Isso remove TUDO que não está em uso
```

---

## Rede e Conectividade

### Erro de porta em uso

```bash
# Verifique quais portas estão em uso
# Windows:
netstat -ano | findstr :3000
netstat -ano | findstr :8080

# Linux/Mac:
lsof -i :3000
lsof -i :8080

# Mate o processo ou mude a porta no application.yml
```

### Frontend não consegue conectar ao backend

```bash
# 1. Verifique se o backend está rodando
curl http://localhost:8080/actuator/health

# 2. Verifique o arquivo .env.local do frontend
# Deve ter:
NEXT_PUBLIC_API_URL="http://localhost:8080"

# 3. Verifique CORS no backend
# Em SecurityConfig.kt, deve permitir origem do frontend:
.cors().configurationSource { request ->
    val corsConfiguration = CorsConfiguration()
    corsConfiguration.allowedOrigins = listOf("http://localhost:3000")
    corsConfiguration.allowedMethods = listOf("*")
    corsConfiguration.allowedHeaders = listOf("*")
    corsConfiguration.allowCredentials = true
    corsConfiguration
}
```

### Erro de CORS "No 'Access-Control-Allow-Origin' header"

```kotlin
// Configure CORS globalmente no backend
// Em SecurityConfig.kt ou WebMvcConfig.kt:

@Bean
fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("http://localhost:3000")
    configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    configuration.allowedHeaders = listOf("*")
    configuration.allowCredentials = true
    
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
}
```

### WebSocket não conecta (colaboração em tempo real)

```bash
# 1. Verifique se o SockJS está configurado no backend
# Em WebSocketConfig.kt:
registry.addEndpoint("/ws/connect")
    .setAllowedOrigins("http://localhost:3000")
    .withSockJS()

# 2. No frontend, use a URL correta:
const socket = new SockJS('http://localhost:8080/ws/connect')

# 3. Verifique firewall/antivírus que podem bloquear WebSocket
```

### Frontend não conecta ao backend (Docker)

```bash
# Verifique as variáveis de ambiente
docker-compose exec frontend printenv | grep NEXT_PUBLIC

# Deve mostrar:
# NEXT_PUBLIC_API_URL=http://localhost:8080
# NEXT_PUBLIC_WS_URL=http://localhost:8080

# Se estiver errado, atualize docker-compose.yml e reinicie
docker-compose restart frontend
```

---

## Segurança e Autenticação

### Erro "Access denied" ao acessar documentos

```typescript
// Verifique se o token JWT está sendo enviado corretamente

// No frontend, em services/api.ts:
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// No backend, verifique se JwtAuthenticationFilter está ativo
```

### BCrypt "Illegal Base64 character" ao fazer login

```kotlin
// Senhas no banco devem estar hasheadas corretamente
// Verifique se está usando BCryptPasswordEncoder com strength 10:

val encoder = BCryptPasswordEncoder(10)
val hashedPassword = encoder.encode("senha123")

// Se tiver senhas em texto plano, crie um migration:
// V999__hash_existing_passwords.sql
```

### Erro ao enviar emails

**Soluções:**
- Verifique as credenciais SMTP no `.env` ou `application.yml`
- Para Gmail, use uma "Senha de App" ([Como criar](https://support.google.com/accounts/answer/185833))
- Ou use um serviço de email de desenvolvimento como [Mailhog](https://github.com/mailhog/MailHog)

**Teste de conexão SMTP:**
```bash
# Use telnet para testar
telnet smtp.gmail.com 587
```

---

## Performance e Recursos

### Erro ao fazer upload de imagens

```yaml
# 1. Verifique o limite de tamanho no application.yml:
spring:
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB
```

```bash
# 2. Verifique se o diretório de upload existe:
mkdir -p ./uploads

# 3. Verifique permissões da pasta (Linux/Mac):
chmod 755 ./uploads

# 4. Windows: Verifique se o antivírus não está bloqueando
```

---

## Testes

### Testes falhando

```yaml
# Backend - Use perfil de teste:
# Em src/test/resources/application-test.yml:
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
```

```bash
# Execute com o perfil:
./gradlew test -Dspring.profiles.active=test

# Frontend - Limpe cache do Jest:
pnpm test --clearCache
pnpm test
```

---

## HTTP e APIs

### Erro "Method Not Allowed" em requisições

```bash
# Verifique se o método HTTP está correto:
# POST /api/auth/login (não GET)
# PATCH /api/documents/:id (não PUT)

# Verifique se o @RequestMapping está correto no controller:
@PostMapping("/login")
fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<*>
```

---

## Limpeza e Reset

### Limpar tudo e recomeçar

```bash
# Backend
cd backend
./gradlew clean
rm -rf build/
./gradlew build

# Frontend
cd frontend
rm -rf .next/
rm -rf node_modules/
pnpm install
pnpm dev

# Docker
docker-compose down
docker system prune -a
docker-compose up -d --build
```

### Verificar logs detalhados

```bash
# Backend com debug
./gradlew bootRun --debug

# Frontend com verbose
pnpm dev --verbose

# Docker
docker-compose logs -f --tail=100
```

---

## 📞 Ainda com problemas?

Se nenhuma das soluções acima funcionou:

1. **Consulte a documentação oficial:**
   - [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
   - [Next.js Docs](https://nextjs.org/docs)
   - [PostgreSQL Docs](https://www.postgresql.org/docs/)
   - [Docker Docs](https://docs.docker.com/)

2. **Verifique os logs completos:**
   - Backend: `./gradlew bootRun --debug`
   - Frontend: `pnpm dev --verbose`
   - Docker: `docker-compose logs -f`

3. **Abra uma Issue no GitHub:**
   - Descrição detalhada do erro
   - Logs completos (sem informações sensíveis)
   - Versões (Java, Node, PostgreSQL, Docker, etc.)
   - Sistema operacional
   - Passos para reproduzir

4. **Informações úteis para debug:**
   ```bash
   # Versões
   java -version
   node -v
   pnpm -v
   docker -v
   psql --version
   
   # Sistema
   # Windows: systeminfo
   # Linux/Mac: uname -a
   ```

---

**Última atualização:** 31 de Dezembro de 2025
