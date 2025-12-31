# 📝 Notion Clone

Um clone completo do Notion construído com tecnologias modernas e open source. Sistema de criação e gerenciamento de documentos com suporte a páginas, sub-páginas, edição WYSIWYG/Markdown e colaboração em tempo real.

## ✨ Features

### 📄 Gerenciamento de Documentos
- ✅ Criação de páginas e sub-páginas ilimitadas (conforme plano)
- ✅ Editor WYSIWYG rico com Tiptap
- ✅ Suporte completo a Markdown
- ✅ Auto-save inteligente (debounced)
- ✅ Anexar imagens (até 5MB)
- ✅ Blocos de comentários (info, warning, danger)
- ✅ Hierarquia de páginas com navegação
- ✅ Exportação para PDF (planos Pro, Team e Enterprise)

### ✏️ Editor de Texto Rico

#### Atalhos Markdown
- `#` + espaço → Título 1 (H1)
- `##` + espaço → Título 2 (H2)
- `###` + espaço → Título 3 (H3)
- `*texto*` ou `_texto_` → Itálico
- `**texto**` ou `__texto__` → Negrito
- `-` + espaço → Lista não ordenada
- `1.` + espaço → Lista ordenada
- `[ ]` + espaço → Checkbox
- `` `código` `` → Código inline
- ` ``` ` → Bloco de código
- `>` + espaço → Citação

#### Paleta de Comandos
Digite `/` para abrir a paleta de comandos:
- `/image` → Inserir imagem
- `/info` → Bloco de informação (azul)
- `/warning` → Bloco de aviso (amarelo)
- `/danger` → Bloco de perigo (vermelho)
- `/heading1`, `/heading2`, `/heading3` → Títulos
- `/page` → Criar sub-página

### 👥 Gestão de Usuários
- ✅ Registro com validação forte de senha
- ✅ Sugestão de username (estilo Reddit)
- ✅ Verificação de email com código
- ✅ Login seguro com JWT
- ✅ Recuperação de senha por email
- ✅ Edição de perfil e preferências
- ✅ Logout seguro
- ✅ Exclusão de conta com feedback

### 💎 Planos de Assinatura

#### 🆓 Free
- 1 página principal
- Até 3 sub-páginas
- Editor completo
- Auto-save

#### 💼 Pro (R$ 19,90/mês)
- Até 100 páginas principais
- Até 10 sub-páginas por página
- Todos os recursos do Free
- **Exportar páginas como PDF**

#### 👥 Team (R$ 39,90/usuário/mês)
- Páginas e sub-páginas ilimitadas
- Todos os recursos do Pro
- **Colaboração em tempo real**
- **Compartilhar páginas com outros usuários**
- **Edição simultânea**

#### 🏢 Enterprise (Preço personalizado)
- Todos os recursos do Team
- **SSO (Single Sign-On)**
- **Auditoria de logs**
- **SLA de suporte**
- **Contrato de confidencialidade**

### 🔐 Segurança
- ✅ Senha forte obrigatória:
  - Mínimo 8 caracteres
  - 1 letra maiúscula
  - 1 caractere especial
  - 1 número
  - Sem números sequenciais
  - Não pode conter o próprio nome
- ✅ JWT com access e refresh tokens
- ✅ Bcrypt para hash de senhas
- ✅ Rate limiting
- ✅ Validação de entrada (frontend e backend)
- ✅ Sanitização de HTML

### 🎯 UX
- ✅ Onboarding na primeira utilização
- ✅ Interface intuitiva e responsiva
- ✅ Indicador de salvamento (Salvando... / Salvo)
- ✅ Feedback visual em todas as ações
- ✅ Temas claro e escuro (opcional)

## 🛠️ Stack Tecnológica

### Frontend
- **Next.js 14+** - React framework com App Router
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling utility-first
- **shadcn/ui** - Componentes UI acessíveis
- **Tiptap** - Editor WYSIWYG
- **Zustand** - State management
- **React Hook Form + Zod** - Formulários e validação
- **Axios** - HTTP client
- **Socket.io-client** - WebSocket para colaboração

### Backend
- **Kotlin 1.9+** - Linguagem moderna e type-safe
- **Spring Boot 3.2+** - Framework robusto e maduro
- **PostgreSQL 15+** - Banco de dados relacional
- **Spring Data JPA + Hibernate** - ORM
- **Spring Security + JWT** - Autenticação
- **BCryptPasswordEncoder** - Hash de senhas
- **Spring Mail + Thymeleaf** - Envio de emails
- **OpenPDF / Flying Saucer** - Geração de PDFs
- **Spring WebSocket (STOMP)** - WebSocket server
- **MultipartFile** - Upload de arquivos

### DevOps & Tools
- **Gradle** - Build tool (Kotlin DSL)
- **pnpm** - Package manager (frontend)
- **Ktlint + Detekt** - Code quality (backend)
- **ESLint + Prettier** - Code quality (frontend)
- **JUnit 5 + MockK** - Testing (backend)
- **Jest** - Testing (frontend)
- **Husky** - Git hooks
- **SpringDoc OpenAPI** - API documentation

## 📦 Instalação

### Pré-requisitos
- Node.js 20+ ([Download](https://nodejs.org/))
- JDK 21+ ([Download](https://www.oracle.com/java/technologies/downloads/))
- Gradle 8+ (ou use o wrapper ./gradlew)
- pnpm 8+ (`npm install -g pnpm`)
- PostgreSQL 15+ ([Download](https://www.postgresql.org/download/))
- Git ([Download](https://git-scm.com/))

### 1. Clone o Repositório
```bash
git clone https://github.com/seu-usuario/notion-clone.git
cd notion-clone
```

### 2. Instale as Dependências
```bash
# Instalar dependências do workspace
pnpm install
```

### 3. Configure o Banco de Dados

#### Criar o banco de dados PostgreSQL
```bash
# Entre no PostgreSQL
psql -U postgres

# Crie o banco
CREATE DATABASE notion_clone;

# Saia
\q
```

### 4. Configure as Variáveis de Ambiente

#### Backend (application.yml)
Crie o arquivo `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notion_clone
    username: postgres
    password: senha
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
  flyway:
    enabled: true
  mail:
    host: smtp.gmail.com
    port: 587
    username: seu-email@gmail.com
    password: sua-senha-app
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

app:
  jwt:
    secret: sua-chave-secreta-super-segura-aqui
    refresh-secret: sua-chave-refresh-super-segura-aqui
    expiration: 900000  # 15 minutos em ms
    refresh-expiration: 604800000  # 7 dias em ms
  upload:
    dir: ./uploads
    max-file-size: 5242880  # 5MB

server:
  port: 8080
```

#### Frontend (.env.local)
Crie o arquivo `frontend/.env.local`:
```env
NEXT_PUBLIC_API_URL="http://localhost:8080"
NEXT_PUBLIC_WS_URL="http://localhost:8080"
```

### 5. Execute as Migrations do Banco
```bash
cd backend
./gradlew flywayMigrate
# Ou simplesmente rode a aplicação que o Flyway executará automaticamente
cd ..
```

### 6. (Opcional) Popular o Banco com Dados de Teste
```bash
cd backend
# Execute uma classe de seed Kotlin ou use um script SQL
cd ..
```

## 🚀 Executar o Projeto

### Modo Desenvolvimento

#### Opção 1: Executar Tudo de Uma Vez (Recomendado)
```bash
# Na raiz do projeto
pnpm dev
```

#### Opção 2: Executar Separadamente

**Terminal 1 - Backend:**
```bash
cd backend
./gradlew bootRun
# Backend rodando em http://localhost:8080
# API Docs em http://localhost:8080/swagger-ui.html
```

**Terminal 2 - Frontend:**
```bash
cd frontend
pnpm dev
# Frontend rodando em http://localhost:3000
```

### Modo Produção

```bash
# Build do backend
cd backend
./gradlew build
java -jar build/libs/notion-clone-0.0.1-SNAPSHOT.jar

# Build do frontend (em outro terminal)
cd frontend
pnpm build
pnpm start
```

## 📚 Documentação

- [Arquitetura](./docs/ARCHITECTURE.md) - Detalhes técnicos e decisões arquiteturais
- [Regras de Negócio](./docs/rules/) - Regras específicas de cada módulo
- [API Documentation](http://localhost:8080/swagger-ui.html) - Swagger UI (após iniciar o backend)
- [Copilot Instructions](./.github/copilot-instructions.md) - Guia para o GitHub Copilot

## 🧪 Testes

### Executar todos os testes
```bash
# Backend
cd backend
./gradlew test

# Frontend
cd frontend
pnpm test
```

### Testes com coverage
```bash
# Backend
./gradlew test jacocoTestReport

# Frontend
pnpm test:cov
```

### Testes de Integração
```bash
cd backend
./gradlew integrationTest
```

## 📁 Estrutura do Projeto

```
notion-clone/
├── frontend/               # Aplicação Next.js
│   ├── src/
│   │   ├── app/           # App Router (páginas)
│   │   ├── components/    # Componentes React
│   │   ├── hooks/         # Custom hooks
│   │   ├── lib/           # Utilitários
│   │   ├── services/      # API clients
│   │   ├── store/         # State management
│   │   └── types/         # TypeScript types
│   └── package.json
│
├── backend/               # Aplicação Spring Boot (Kotlin)
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/        # Código fonte Kotlin
│   │   │   └── resources/     # application.yml, migrations
│   │   └── test/          # Testes
│   ├── build.gradle.kts
│   └── settings.gradle.kts
│
├── docs/                 # Documentação
├── .github/             # Configurações do GitHub
├── README.md
└── pnpm-workspace.yaml
```

## 🔧 Scripts Úteis

```bash
# Instalar dependências
pnpm install                # Frontend
./gradlew build --refresh-dependencies  # Backend

# Desenvolvimento
pnpm dev                    # Inicia frontend e backend
pnpm dev:frontend          # Apenas frontend
./gradlew bootRun          # Apenas backend (do diretório backend/)

# Build
pnpm build                 # Build de tudo
pnpm build:frontend       # Apenas frontend
./gradlew build           # Apenas backend

# Testes
pnpm test                  # Frontend
./gradlew test            # Backend
pnpm test:watch           # Frontend watch mode
./gradlew test --continuous  # Backend watch mode

# Linting
pnpm lint                  # Frontend
./gradlew ktlintCheck     # Backend
pnpm lint:fix             # Frontend auto-fix
./gradlew ktlintFormat    # Backend auto-format

# Formatação
pnpm format               # Formata frontend com Prettier
./gradlew ktlintFormat    # Formata backend com Ktlint

# Flyway (Migrations)
./gradlew flywayMigrate   # Executa migrations
./gradlew flywayInfo      # Status das migrations
./gradlew flywayClean     # Limpa o banco (CUIDADO!)

# Database
./gradlew flywayClean flywayMigrate  # Reseta banco
```

## 🐛 Troubleshooting

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

### Erro ao enviar emails
- Verifique as credenciais SMTP no `.env`
- Para Gmail, use uma "Senha de App" ([Como criar](https://support.google.com/accounts/answer/185833))
- Ou use um serviço de email de desenvolvimento como [Mailhog](https://github.com/mailhog/MailHog)

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

### Flyway migration falhou
```bash
# Verifique o status das migrations
cd backend
./gradlew flywayInfo

# Se houver migration com falha, repare:
./gradlew flywayRepair

# Ou limpe tudo e reaplique (CUIDADO: apaga dados!)
./gradlew flywayClean flywayMigrate
```

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

### Erro "Access denied" ao acessar documentos
```bash
# Verifique se o token JWT está sendo enviado corretamente

# No frontend, em services/api.ts:
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

# No backend, verifique se JwtAuthenticationFilter está ativo
```

### Erro ao fazer upload de imagens
```bash
# 1. Verifique o limite de tamanho no application.yml:
spring:
  servlet:
    multipart:
      max-file-size: 5MB
      max-request-size: 5MB

# 2. Verifique se o diretório de upload existe:
mkdir -p ./uploads

# 3. Verifique permissões da pasta (Linux/Mac):
chmod 755 ./uploads

# 4. Windows: Verifique se o antivírus não está bloqueando
```

### BCrypt "Illegal Base64 character" ao fazer login
```bash
# Senhas no banco devem estar hasheadas corretamente
# Verifique se está usando BCryptPasswordEncoder com strength 10:

val encoder = BCryptPasswordEncoder(10)
val hashedPassword = encoder.encode("senha123")

# Se tiver senhas em texto plano, crie um migration:
# V999__hash_existing_passwords.sql
```

### Erro "Method Not Allowed" em requisições
```bash
# Verifique se o método HTTP está correto:
# POST /api/auth/login (não GET)
# PATCH /api/documents/:id (não PUT)

# Verifique se o @RequestMapping está correto no controller:
@PostMapping("/login")
fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<*>
```

### Erro de CORS "No 'Access-Control-Allow-Origin' header"
```bash
# Configure CORS globalmente no backend
# Em SecurityConfig.kt ou WebMvcConfig.kt:

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
```bash
# 1. Verifique se está usando WSL no Windows
# Adicione ao next.config.js:
module.exports = {
  webpack: (config) => {
    config.watchOptions = {
      poll: 1000,
      aggregateTimeout: 300,
    }
    return config
  },
}

# 2. Ou use Fast Refresh:
# Em next.config.js:
experimental: {
  reactRefresh: true,
}
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
```bash
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

### Testes falhando
```bash
# Backend - Use perfil de teste:
# Em src/test/resources/application-test.yml:
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop

# Execute com o perfil:
./gradlew test -Dspring.profiles.active=test

# Frontend - Limpe cache do Jest:
pnpm test --clearCache
pnpm test
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
```bash
# Configure o logback no backend
# Em src/main/resources/logback-spring.xml:
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

### Ainda com problemas?

1. **Limpe tudo e recomece:**
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
```

2. **Verifique os logs detalhados:**
```bash
# Backend com debug
./gradlew bootRun --debug

# Frontend com verbose
pnpm dev --verbose
```

3. **Consulte a documentação:**
   - [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
   - [Next.js Docs](https://nextjs.org/docs)
   - [PostgreSQL Docs](https://www.postgresql.org/docs/)

4. **Abra uma Issue:** Se o problema persistir, abra uma issue no GitHub com:
   - Descrição detalhada do erro
   - Logs completos
   - Versões (Java, Node, PostgreSQL, etc.)
   - Sistema operacional
   - Passos para reproduzir

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Commits Convencionais
- `feat:` Nova feature
- `fix:` Correção de bug
- `docs:` Mudanças na documentação
- `style:` Formatação, ponto e vírgula, etc.
- `refactor:` Refatoração de código
- `test:` Adição de testes
- `chore:` Atualização de dependências, configs, etc.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

Desenvolvido com ❤️ como um projeto educacional.

## 🙏 Agradecimentos

- [Notion](https://notion.so) - Pela inspiração
- [Next.js](https://nextjs.org)
- [Nest.js](https://nestjs.com)
- [Tiptap](https://tiptap.dev)
- [shadcn/ui](https://ui.shadcn.com)
- E toda a comunidade open source!

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas:
- Abra uma [Issue](https://github.com/seu-usuario/notion-clone/issues)
- Consulte a [Documentação](./docs/)
- Verifique o [Troubleshooting](#-troubleshooting)

## 🗺️ Roadmap

- [ ] Mobile app (React Native)
- [ ] Templates de páginas
- [ ] Versionamento de documentos
- [ ] Importar/exportar Markdown
- [ ] API pública
- [ ] Webhooks
- [ ] Integrações (Slack, Discord, etc.)
- [ ] Pesquisa full-text
- [ ] Comentários em páginas
- [ ] Tabelas avançadas
- [ ] Gráficos e visualizações
- [ ] Modo offline

---

**⭐ Se este projeto te ajudou, deixe uma estrela no GitHub!**
