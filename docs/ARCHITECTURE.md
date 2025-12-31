# Arquitetura do Sistema - Notion Clone

## Visão Geral

Este documento descreve a arquitetura e as decisões técnicas do clone do Notion.

## Stack Tecnológica

### Frontend

#### Next.js 14+ (App Router)
- **Por quê?** Framework React com renderização híbrida (SSR/SSG/CSR), excelente DX, otimizações automáticas
- **Recursos usados**: App Router, Server Components, API Routes, Image Optimization
- **Documentação**: https://nextjs.org/docs

#### TypeScript
- **Por quê?** Type safety, melhor IntelliSense, reduz bugs em produção
- **Versão**: 5.0+
- **Configuração**: strict mode habilitado

#### Tailwind CSS
- **Por quê?** Utility-first, alto desempenho, fácil customização, ótima DX
- **Plugins**: typography, forms, aspect-ratio
- **Documentação**: https://tailwindcss.com/docs

#### shadcn/ui
- **Por quê?** Componentes acessíveis, customizáveis, baseados em Radix UI
- **Componentes principais**: Button, Dialog, Dropdown, Input, Toast, etc.
- **Documentação**: https://ui.shadcn.com/

#### Tiptap
- **Por quê?** Editor WYSIWYG extensível, suporte markdown, ótima API
- **Extensões**: StarterKit, Placeholder, Markdown, Image, Link, Collaboration
- **Documentação**: https://tiptap.dev/

#### Zustand
- **Por quê?** State management simples, pequeno (< 2KB), sem boilerplate
- **Alternativa**: React Context para estados simples
- **Documentação**: https://zustand-demo.pmnd.rs/

#### React Hook Form + Zod
- **Por quê?** Performance, validação type-safe, ótima DX
- **Uso**: Formulários de registro, login, perfil, etc.
- **Documentação**: https://react-hook-form.com/

#### Axios
- **Por quê?** Cliente HTTP robusto, interceptors, boa API
- **Configuração**: Instância configurada com base URL, auth interceptor
- **Documentação**: https://axios-http.com/

#### Socket.io-client
- **Por quê?** WebSocket com fallbacks, room support, reconexão automática
- **Uso**: Colaboração em tempo real (plano Premium)
- **Documentação**: https://socket.io/docs/v4/client-api/

### Backend

#### Kotlin 1.9+
- **Por quê?** Linguagem moderna, null-safe, interoperável com Java, concisa
- **Features usadas**: Coroutines, data classes, sealed classes, extension functions
- **Documentação**: https://kotlinlang.org/docs/

#### Spring Boot 3.2+
- **Por quê?** Framework maduro, produção-ready, grande comunidade, fácil de testar
- **Recursos usados**: Spring Web, Spring Security, Spring Data JPA, Spring WebSocket, Spring Mail
- **Documentação**: https://spring.io/projects/spring-boot

#### PostgreSQL 15+
- **Por quê?** Banco relacional robusto, open source, ACID, JSON support
- **Features**: Foreign keys, indexes, full-text search, triggers
- **Documentação**: https://www.postgresql.org/docs/

#### Spring Data JPA + Hibernate
- **Por quê?** ORM padrão Java/Kotlin, maduro, feature-rich, ótima performance
- **Features**: Relations, cascade operations, lazy/eager loading, JPQL, Criteria API
- **Documentação**: https://spring.io/projects/spring-data-jpa

#### Spring Security + JWT (jjwt)
- **Por quê?** Framework de segurança completo, padrão da indústria
- **Estratégia**: Access token (15min) + Refresh token (7 dias)
- **Segurança**: BCryptPasswordEncoder (strength 10), secret em application.yml
- **Documentação**: https://spring.io/projects/spring-security

#### Jakarta Bean Validation
- **Por quê?** Validação declarativa com annotations, padrão Java EE
- **Uso**: DTOs de entrada com @Valid, @NotNull, @Email, @Size, etc.
- **Documentação**: https://beanvalidation.org/

#### Spring Mail + Thymeleaf
- **Por quê?** Envio de emails integrado, templates HTML com Thymeleaf
- **Uso**: Verificação de email, recuperação de senha, notificações
- **Documentação**: https://spring.io/guides/gs/sending-email/

#### OpenPDF / Flying Saucer
- **Por quê?** Gera PDFs de alta qualidade a partir de HTML/CSS
- **Uso**: Exportação de documentos (planos Pro e Premium)
- **Alternativa**: iText (comercial) ou Apache PDFBox
- **Documentação**: https://github.com/LibrePDF/OpenPDF

#### Spring WebSocket (STOMP)
- **Por quê?** WebSocket nativo do Spring, protocolo STOMP para messaging
- **Uso**: Colaboração em tempo real, notificações
- **Features**: Topic/Queue support, session management, message broker
- **Documentação**: https://spring.io/guides/gs/messaging-stomp-websocket/

#### MultipartFile (Spring Web)
- **Por quê?** API nativa do Spring para upload de arquivos
- **Uso**: Upload de imagens nos documentos
- **Configuração**: Limite 5MB, tipos permitidos (jpg, png, gif, webp)
- **Documentação**: https://spring.io/guides/gs/uploading-files/

### DevOps & Tools

#### Gradle (Kotlin DSL)
- **Por quê?** Build tool moderno, Kotlin DSL, fast builds, dependency management
- **Versão**: 8.0+
- **Documentação**: https://gradle.org/

#### pnpm
- **Por quê?** Gerenciador de pacotes rápido, eficiente, workspace support (frontend)
- **Versão**: 8.0+
- **Documentação**: https://pnpm.io/

#### Ktlint + Detekt / ESLint + Prettier
- **Por quê?** Qualidade de código, formatação consistente
- **Configuração**: Ktlint/Detekt para Kotlin, ESLint/Prettier para TypeScript
- **Documentação**: https://ktlint.github.io/, https://detekt.dev/

#### JUnit 5 + MockK / Jest + React Testing Library
- **Por quê?** Testing frameworks populares e robustos
- **Uso**: Unit tests, integration tests
- **Documentação**: https://junit.org/junit5/, https://mockk.io/

#### Husky
- **Por quê?** Git hooks, garante qualidade antes de commits
- **Hooks**: pre-commit (lint-staged), pre-push (tests)
- **Documentação**: https://typicode.github.io/husky/

#### SpringDoc OpenAPI (Swagger)
- **Por quê?** Documentação automática da API usando OpenAPI 3.0
- **Integração**: springdoc-openapi-starter-webmvc-ui
- **Acesso**: /swagger-ui.html
- **Documentação**: https://springdoc.org/

## Estrutura de Pastas

```
notion-clone/
├── frontend/                 # Aplicação Next.js
│   ├── public/              # Arquivos estáticos
│   ├── src/
│   │   ├── app/            # App Router (páginas)
│   │   │   ├── (auth)/    # Grupo de rotas: login, registro
│   │   │   ├── (dashboard)/ # Grupo de rotas: editor, perfil
│   │   │   ├── api/       # API routes (proxy, webhooks)
│   │   │   ├── layout.tsx
│   │   │   └── page.tsx
│   │   ├── components/    # Componentes React
│   │   │   ├── ui/       # shadcn/ui components
│   │   │   ├── editor/   # Editor e relacionados
│   │   │   ├── auth/     # Login, registro
│   │   │   └── shared/   # Header, sidebar, etc.
│   │   ├── hooks/        # Custom hooks
│   │   ├── lib/          # Utilitários
│   │   ├── services/     # API clients
│   │   ├── store/        # Zustand stores
│   │   ├── types/        # TypeScript types
│   │   └── constants/    # Constantes
│   ├── .env.local
│   ├── next.config.js
│   ├── tailwind.config.ts
│   └── package.json
│
├── backend/                 # Aplicação Spring Boot (Kotlin)
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/notionclone/
│   │   │   │   ├── auth/     # Autenticação
│   │   │   │   │   ├── controller/
│   │   │   │   │   ├── service/
│   │   │   │   │   ├── dto/
│   │   │   │   │   ├── filter/
│   │   │   │   │   └── config/
│   │   │   │   ├── user/     # Gestão de usuários
│   │   │   │   │   ├── controller/
│   │   │   │   │   ├── service/
│   │   │   │   │   ├── repository/
│   │   │   │   │   ├── entity/
│   │   │   │   │   └── dto/
│   │   │   │   ├── document/ # CRUD de documentos
│   │   │   │   ├── plan/    # Lógica de planos
│   │   │   │   ├── collaboration/ # Real-time
│   │   │   │   ├── email/   # Envio de emails
│   │   │   │   ├── pdf/     # Geração de PDFs
│   │   │   │   ├── common/  # Código compartilhado
│   │   │   │   │   ├── exception/
│   │   │   │   │   ├── config/
│   │   │   │   │   ├── util/
│   │   │   │   │   └── annotation/
│   │   │   │   └── NotionCloneApplication.kt
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml
│   │   │       ├── application-prod.yml
│   │   │       └── db/migration/  # Flyway migrations
│   │   └── test/            # Testes
│   ├── build.gradle.kts
│   └── settings.gradle.kts
│
├── docs/                   # Documentação
│   ├── ARCHITECTURE.md
│   ├── API.md
│   ├── DATABASE.md
│   └── rules/            # Regras de negócio
│
├── .github/
│   └── copilot-instructions.md
│
├── .gitignore
├── README.md
└── pnpm-workspace.yaml
```

## Fluxos Principais

### 1. Registro de Usuário
```
1. Usuário preenche formulário → Frontend valida (Zod)
2. Frontend envia POST /api/auth/register → Backend
3. Backend valida dados (@Valid)
4. Backend verifica duplicatas (JPA Repository)
5. Backend sugere username alternativo se necessário
6. Backend hash senha (BCryptPasswordEncoder)
7. Backend salva usuário no DB (JPA)
8. Backend gera código de verificação (6 dígitos)
9. Backend envia email (Spring Mail)
10. Backend retorna sucesso
11. Frontend redireciona para tela de verificação
```

### 2. Login
```
1. Usuário insere email/senha → Frontend
2. Frontend envia POST /api/auth/login → Backend
3. Backend busca usuário por email (JPA Repository)
4. Backend compara senha (BCryptPasswordEncoder.matches)
5. Backend gera JWT tokens com jjwt (access + refresh)
6. Backend retorna tokens (httpOnly cookies)
7. Frontend verifica se é primeiro login
8. Se sim, mostra onboarding
9. Frontend redireciona para dashboard
```

### 3. Edição de Documento
```
1. Usuário digita no editor (Tiptap)
2. Editor debounce mudanças (1 segundo)
3. Frontend mostra "Salvando..."
4. Frontend envia PATCH /api/documents/:id → Backend
5. Backend valida permissões (Spring Security @PreAuthorize)
6. Backend verifica limites do plano
7. Backend salva no DB (JPA Repository)
8. Backend emite evento WebSocket (se Premium)
9. Backend retorna sucesso
10. Frontend mostra "Salvo"
```

### 4. Colaboração em Tempo Real (Premium)
```
1. Usuário A abre documento
2. Frontend conecta WebSocket (STOMP)
3. Frontend subscreve ao tópico do documento
4. Usuário A edita texto
5. Tiptap gera operação de mudança
6. Frontend envia mensagem STOMP
7. Backend recebe mensagem
8. Backend valida permissões
9. Backend broadcast para subscritos no tópico
10. Usuário B recebe mudança
11. Tiptap aplica mudança localmente
```

## Segurança

### Autenticação
- JWT com Spring Security (algoritmo HS256 ou RS256)
- Refresh token rotation
- Logout invalida tokens (blacklist no Redis - opcional)

### Autorização
- Spring Security verifica JWT (JwtAuthenticationFilter)
- @PreAuthorize para roles/plans
- Validação de ownership de recursos nos services

### Validação de Entrada
- Frontend: Zod schemas
- Backend: Jakarta Bean Validation (@Valid, @NotNull, @Email, etc.)
- Sanitização de HTML no editor (DOMPurify no frontend, OWASP HTML Sanitizer no backend)

### Rate Limiting
- Bucket4j ou custom Spring filter
- Limites por IP e por usuário
- Mais restritivo para login/registro

### CORS
- Configurado no Spring Security
- Permite apenas origem do frontend
- Credentials habilitado para cookies

## Performance

### Frontend
- Next.js Image optimization
- Code splitting automático
- Lazy loading de componentes
- React.memo para componentes pesados
- Debounce em auto-save e search
- Virtual scrolling para listas grandes

### Backend
- Índices no PostgreSQL (email, username, document_id)
- Paginação com Spring Data (Pageable)
- Cache com Spring Cache (@Cacheable) ou Redis
- Connection pooling (HikariCP - padrão do Spring Boot)
- Compressão de respostas (gzip)
- Query optimization com @EntityGraph

### Database
- Índices em foreign keys
- Índices em campos de busca
- Soft delete evita deleções custosas
- Queries otimizadas com Prisma

## Escalabilidade

### Horizontal Scaling
- Backend stateless (JWT, não sessions)
- Spring WebSocket com Redis broker (múltiplas instâncias)
- Load balancer (Nginx/AWS ALB)

### Vertical Scaling
- Aumentar recursos do servidor
- Otimizar queries (EXPLAIN ANALYZE)
- Cache agressivo

### Database Scaling
- Read replicas para leituras
- Write master para escritas
- Connection pooling

## Monitoramento

### Logs
- Logback ou Log4j2 (estruturado)
- Níveis: ERROR, WARN, INFO, DEBUG
- Contexto: user_id, request_id (MDC)

### Métricas
- Prometheus + Grafana (opcional)
- Métricas: requests/s, latency, errors
- Alertas para anomalias

### APM
- New Relic ou Sentry (opcional)
- Rastreamento de erros
- Performance monitoring

## Deployment

### Development
```bash
# Frontend
cd frontend
pnpm install
pnpm dev  # http://localhost:3000

# Backend
cd backend
./gradlew bootRun  # http://localhost:8080
```

### Production
```bash
# Frontend
pnpm build
pnpm start

# Backend
./gradlew build
java -jar build/libs/notion-clone-0.0.1-SNAPSHOT.jar
```

### Docker (Opcional)
```yaml
services:
  postgres:
    image: postgres:15
  backend:
    build: ./backend
    image: openjdk:21-jdk
  frontend:
    build: ./frontend
```

## Considerações Futuras

### Features
- [ ] Suporte a mais tipos de blocos (tabelas, embeds)
- [ ] Versionamento de documentos
- [ ] API pública
- [ ] Mobile app (React Native)
- [ ] Importar/exportar Markdown
- [ ] Templates de páginas
- [ ] Pesquisa full-text

### Melhorias Técnicas
- [ ] GraphQL (alternativa ao REST)
- [ ] Redis para cache e sessions
- [ ] Elasticsearch para busca avançada
- [ ] CDN para assets estáticos
- [ ] CI/CD pipeline
- [ ] Testes E2E (Playwright)
- [ ] Kubernetes para orquestração
