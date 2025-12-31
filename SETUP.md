# Notion Clone - Sistema Completo de Documentação e Colaboração

Um clone completo do Notion construído com **Kotlin + Spring Boot** no backend e **Next.js 14** no frontend, incluindo autenticação, gerenciamento de documentos, planos (Free/Pro/Team/Enterprise) e colaboração em tempo real.

## 📋 Sumário

- [Características](#-características)
- [Stack Tecnológico](#-stack-tecnológico)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Execução](#-execução)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [API Documentation](#-api-documentation)
- [Planos e Limites](#-planos-e-limites)
- [Segurança](#-segurança)
- [Testes](#-testes)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)

## ✨ Características

### Autenticação e Usuários
- ✅ Registro com validação robusta de senha
- ✅ Login com JWT (access + refresh tokens)
- ✅ Verificação de email com código de 6 dígitos
- ✅ Recuperação de senha
- ✅ Bloqueio temporário após 5 tentativas falhas
- ✅ Sugestões de username disponíveis (estilo Reddit)
- ✅ Exclusão de conta com período de recuperação (30 dias)

### Gerenciamento de Documentos
- ✅ Criação de páginas principais e sub-páginas (2 níveis)
- ✅ Editor WYSIWYG com suporte a markdown (Tiptap)
- ✅ Auto-save com debounce
- ✅ Upload de imagens (max 5MB)
- ✅ Ícones e capas personalizadas
- ✅ Favoritos e arquivamento
- ✅ Busca por título
- ✅ Páginas públicas com slug personalizado
- ✅ Callouts (info, warning, danger)

### Planos e Permissões
- **Free**: 1 página, 3 sub-páginas
- **Pro**: 100 páginas, 10 sub-páginas, exportar PDF
- **Team**: Ilimitado, colaboração em tempo real, compartilhamento
- **Enterprise**: Todos os recursos + SSO, auditoria de logs, SLA de suporte

### Segurança
- ✅ BCrypt com strength 10 para senhas
- ✅ JWT com tokens de curta duração (15min)
- ✅ Sanitização de HTML (OWASP)
- ✅ Proteção CSRF
- ✅ Rate limiting
- ✅ Security headers (HSTS, CSP, X-Frame-Options)
- ✅ Validação de entrada robusta

## 🛠 Stack Tecnológico

### Backend
- **Linguagem**: Kotlin 1.9.21
- **Framework**: Spring Boot 3.2.1
- **Database**: PostgreSQL 15+
- **ORM**: Spring Data JPA + Hibernate
- **Autenticação**: Spring Security + JWT (jjwt)
- **Migrations**: Flyway
- **Build**: Gradle 8+ (Kotlin DSL)
- **Segurança**: BCrypt, OWASP HTML Sanitizer
- **PDF**: OpenPDF
- **Email**: Spring Mail

### Frontend
- **Framework**: Next.js 14 (App Router)
- **Linguagem**: TypeScript 5
- **Styling**: Tailwind CSS
- **UI Components**: shadcn/ui
- **Editor**: Tiptap
- **State**: Zustand
- **Forms**: React Hook Form + Zod
- **HTTP**: Axios
- **Package Manager**: pnpm

## 📦 Pré-requisitos

- **Java**: JDK 17 ou superior
- **Node.js**: 18.x ou superior
- **pnpm**: 8.x ou superior
- **PostgreSQL**: 15 ou superior
- **Git**: Para versionamento

## 🚀 Instalação

### 1. Clone o repositório

\`\`\`bash
git clone <repository-url>
cd notion-clone
\`\`\`

### 2. Configure o PostgreSQL

\`\`\`bash
# Crie o database
createdb notionclone_dev

# Ou via psql
psql -U postgres
CREATE DATABASE notionclone_dev;
\q
\`\`\`

### 3. Backend Setup

\`\`\`bash
cd backend

# Configure as variáveis de ambiente
# Crie um arquivo .env ou configure no application.yml
\`\`\`

Crie o arquivo `backend/src/main/resources/application-local.yml`:

\`\`\`yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notionclone_dev
    username: postgres
    password: your_password_here
  
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email@gmail.com
    password: your_app_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

app:
  jwt:
    secret: your-super-secret-key-min-32-characters-long-for-production
    expiration: 900000      # 15 minutes
    refresh-expiration: 604800000  # 7 days
\`\`\`

\`\`\`bash
# Build e execute
./gradlew bootRun --args='--spring.profiles.active=local'

# Ou rode com IntelliJ IDEA selecionando o profile 'local'
\`\`\`

### 4. Frontend Setup

\`\`\`bash
cd frontend

# Instale as dependências
pnpm install

# Configure as variáveis de ambiente
cp .env.local.example .env.local
\`\`\`

Edite `frontend/.env.local`:

\`\`\`env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
\`\`\`

\`\`\`bash
# Execute o servidor de desenvolvimento
pnpm dev
\`\`\`

## ⚙️ Configuração

### Configurar Email (Gmail)

1. Ative a verificação em duas etapas na sua conta Google
2. Gere uma senha de app: https://myaccount.google.com/apppasswords
3. Use essa senha no `application-local.yml`

### JWT Secret

**IMPORTANTE**: Para produção, gere um secret forte:

\`\`\`bash
# Linux/Mac
openssl rand -base64 64

# Windows (PowerShell)
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 }))
\`\`\`

## 🏃 Execução

### Backend

\`\`\`bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=local'
\`\`\`

A API estará disponível em: http://localhost:8080

### Frontend

\`\`\`bash
cd frontend
pnpm dev
\`\`\`

A aplicação estará disponível em: http://localhost:3000

### Documentação da API (Swagger)

Acesse: http://localhost:8080/swagger-ui.html

## 📁 Estrutura do Projeto

\`\`\`
notion-clone/
├── backend/
│   ├── src/main/kotlin/com/notionclone/
│   │   ├── auth/           # Autenticação (JWT, filtros)
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   └── filter/
│   │   ├── user/           # Gerenciamento de usuários
│   │   ├── document/       # CRUD de documentos
│   │   ├── common/         # Código compartilhado
│   │   │   ├── exception/
│   │   │   ├── util/
│   │   │   └── config/
│   │   └── NotionCloneApplication.kt
│   ├── src/main/resources/
│   │   ├── db/migration/   # Migrações Flyway
│   │   └── application.yml
│   └── build.gradle.kts
│
├── frontend/
│   ├── src/
│   │   ├── app/            # Pages (App Router)
│   │   ├── components/     # Componentes React
│   │   │   ├── ui/        # shadcn/ui
│   │   │   ├── editor/    # Editor Tiptap
│   │   │   └── shared/    # Compartilhados
│   │   ├── services/       # API services
│   │   ├── store/          # Zustand stores
│   │   ├── lib/            # Utilitários
│   │   └── types/          # TypeScript types
│   ├── package.json
│   └── next.config.mjs
│
└── docs/                   # Documentação
    ├── ARCHITECTURE.md
    └── rules/
\`\`\`

## 📊 API Documentation

### Endpoints de Autenticação

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/auth/register` | Registrar novo usuário | ❌ |
| POST | `/api/auth/login` | Login | ❌ |
| POST | `/api/auth/verify-email` | Verificar email | ✅ |
| POST | `/api/auth/resend-code` | Reenviar código | ❌ |
| POST | `/api/auth/forgot-password` | Esqueci senha | ❌ |
| POST | `/api/auth/reset-password` | Resetar senha | ❌ |
| POST | `/api/auth/refresh-token` | Renovar token | ❌ |
| POST | `/api/auth/logout` | Logout | ✅ |

### Endpoints de Documentos

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/documents` | Criar documento | ✅ |
| GET | `/api/documents` | Listar meus documentos | ✅ |
| GET | `/api/documents/{id}` | Obter documento | ✅ |
| PATCH | `/api/documents/{id}` | Atualizar documento | ✅ |
| DELETE | `/api/documents/{id}` | Deletar documento | ✅ |
| GET | `/api/documents/{id}/sub-pages` | Listar sub-páginas | ✅ |
| GET | `/api/documents/favorites` | Listar favoritos | ✅ |
| GET | `/api/documents/archived` | Listar arquivados | ✅ |
| GET | `/api/documents/search?q={query}` | Buscar | ✅ |
| PATCH | `/api/documents/{id}/public` | Tornar público | ✅ |
| GET | `/api/public/documents/{slug}` | Ver público | ❌ |

## 🎯 Planos e Limites

| Feature | Free | Pro | Team | Enterprise |
|---------|------|-----|------|------------|
| Páginas principais | 1 | 100 | ∞ | ∞ |
| Sub-páginas por página | 3 | 10 | ∞ | ∞ |
| Exportar PDF | ❌ | ✅ | ✅ | ✅ |
| Colaboração em tempo real | ❌ | ❌ | ✅ | ✅ |
| Compartilhamento | ❌ | ❌ | ✅ | ✅ |
| SSO (Single Sign-On) | ❌ | ❌ | ❌ | ✅ |
| Auditoria de logs | ❌ | ❌ | ❌ | ✅ |
| SLA de suporte | ❌ | ❌ | ❌ | ✅ |
| Upload de imagens | ✅ | ✅ | ✅ | ✅ |
| Páginas públicas | ✅ | ✅ | ✅ | ✅ |

## 🔐 Segurança

### Validação de Senha

As senhas devem:
- Ter no mínimo 8 caracteres
- Conter pelo menos uma letra maiúscula
- Conter pelo menos uma letra minúscula
- Conter pelo menos um número
- Conter pelo menos um caractere especial
- Não conter números sequenciais repetidos (111, 222, etc.)
- Não conter o nome ou username do usuário

### Tokens JWT

- **Access Token**: 15 minutos de validade
- **Refresh Token**: 7 dias de validade
- Rotação automática de refresh tokens
- Revogação de tokens no logout

### Rate Limiting

- Login: 5 tentativas a cada 15 minutos
- Verificação de email: 3 tentativas por código
- Password recovery: Mesmo limite que login

### Sanitização de HTML

Todo conteúdo HTML é sanitizado usando OWASP Java HTML Sanitizer antes de ser salvo no banco.

## 🧪 Testes

### Backend

\`\`\`bash
cd backend
./gradlew test
./gradlew integrationTest
\`\`\`

### Frontend

\`\`\`bash
cd frontend
pnpm test
pnpm test:watch
\`\`\`

## 🚢 Deployment

### Backend (Production)

1. Configure as variáveis de ambiente:

\`\`\`yaml
# application-prod.yml
spring:
  datasource:
    url: \${DATABASE_URL}
    username: \${DATABASE_USER}
    password: \${DATABASE_PASSWORD}
  
  mail:
    host: \${SMTP_HOST}
    username: \${SMTP_USER}
    password: \${SMTP_PASSWORD}

app:
  jwt:
    secret: \${JWT_SECRET}
\`\`\`

2. Build:

\`\`\`bash
./gradlew clean build -Pprod
\`\`\`

3. Execute:

\`\`\`bash
java -jar -Dspring.profiles.active=prod build/libs/notion-clone-0.0.1-SNAPSHOT.jar
\`\`\`

### Frontend (Production)

1. Configure variáveis de ambiente:

\`\`\`env
NEXT_PUBLIC_API_URL=https://api.youromain.com/api
\`\`\`

2. Build:

\`\`\`bash
pnpm build
\`\`\`

3. Execute:

\`\`\`bash
pnpm start
\`\`\`

## 🔧 Troubleshooting

### Backend não inicia

**Problema**: Database connection error

**Solução**:
\`\`\`bash
# Verifique se o PostgreSQL está rodando
sudo systemctl status postgresql  # Linux
brew services list               # Mac

# Teste a conexão
psql -U postgres -d notionclone_dev
\`\`\`

### Frontend não conecta ao backend

**Problema**: CORS error

**Solução**: Verifique se a origem do frontend está configurada em `SecurityConfig.kt`:

\`\`\`kotlin
configuration.allowedOrigins = listOf(
    "http://localhost:3000",  // Adicione aqui
)
\`\`\`

### Email não está sendo enviado

**Problema**: Authentication failed

**Solução**: Use uma senha de app do Gmail, não sua senha normal.

### Migrations não rodam

**Problema**: Flyway validation error

**Solução**:
\`\`\`bash
# Limpe o histórico do Flyway (CUIDADO: apenas em dev)
psql -U postgres -d notionclone_dev -c "DROP SCHEMA IF EXISTS public CASCADE; CREATE SCHEMA public;"
\`\`\`

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👥 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (\`git checkout -b feature/MinhaFeature\`)
3. Commit suas mudanças (\`git commit -m 'Add: MinhaFeature'\`)
4. Push para a branch (\`git push origin feature/MinhaFeature\`)
5. Abra um Pull Request

## 📞 Suporte

Para suporte, abra uma issue no repositório ou entre em contato via email.

---

**Desenvolvido com ❤️ usando Kotlin + Spring Boot e Next.js**
