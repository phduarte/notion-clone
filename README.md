# 📝 Notion Clone

Um sistema de gestão de conhecimento corporativo construído com tecnologias modernas e open source. Diferente do Notion, focamos em **Developer Experience (DX)**, **Governança Empresarial** e **Soberania de Dados**, oferecendo funcionalidades que o Notion não pode entregar por ser SaaS fechado.

### 🎯 Por Que Usar Este Sistema ao Invés do Notion?

- 💰 **30-40% mais barato** - Cobrança em Reais (BRL) com nota fiscal brasileira
- 🔒 **Self-Hosting** - Instale no seu servidor, 100% compliance com LGPD
- 🎨 **White Label** - Personalize com sua marca (logo, cores, domínio)
- ⚡ **Performance** - Editor leve e rápido, sem "bloatware"
- 🔧 **API-First** - Webhooks nativos e integrações abertas
- 📊 **Diagramas como Código** - Mermaid.js integrado (não precisa de plugins)

> 📖 **Saiba mais:** [Estratégia de Mercado](docs/MARKET_STRATEGY.md) | [Roadmap Estratégico](docs/ROADMAP_STRATEGIC.md) | [Plano de Implementação](docs/IMPLEMENTATION_PLAN.md)

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
- 🚧 **Diagramas Mermaid.js** - Flowcharts, diagramas de sequência (Q1)
- 🚧 **Import/Export Markdown** - Lossless, CommonMark compatível (Q1)
- 🚧 **Smart Embeds** - Figma, Google Sheets, Loom, YouTube (Q3)
- 🚧 **Embed de PDFs** - Visualização inline com navegação (Q3)

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
- `/mermaid` → Diagramas como código (flowchart, sequence, gantt)
- `/embed` → Incorporar conteúdo externo (Figma, Loom, YouTube)
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
- **SSO (Single Sign-On)** - Google Workspace, Azure AD, Keycloak
- **Webhooks** - Integração com ferramentas externas

#### 🏢 Enterprise (Preço personalizado)
- Todos os recursos do Team
- **Self-Hosting (On-Premise)** - Total controle dos dados
- **White Label** - Domínio e marca personalizados
- **Audit Logs** - Rastreabilidade completa de ações
- **Páginas Verificadas** - Sistema de governança de conteúdo
- **Permissões Granulares** - Controle fino de acesso
- **SLA de suporte** - Resposta prioritária
- **Contrato de confidencialidade** - NDA corporativo

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
- 🚧 **SSO (OpenID Connect)** - Login corporativo simplificado (Q2)
- 🚧 **Audit Logs** - Rastreabilidade total de ações (Q2)
- 🚧 **Permissões Granulares** - Controle fino de acesso (Q2)

### 🔌 Integrações & API
- 🚧 **Webhooks** - Notificações de eventos em tempo real (Q1)
- 🚧 **API RESTful Completa** - Documentação Swagger/OpenAPI (Q3)
- 🚧 **Embed Seguro** - Whitelist de domínios confiáveis (Q3)

### 🏢 Governança Corporativa
- 🚧 **Páginas Verificadas** - Sistema de validade de conteúdo (Q2)
- 🚧 **Self-Hosting Simplificado** - Docker Compose + Helm Chart (Q3)
- 🚧 **White Label Completo** - Marca e domínio personalizados (Q3)

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

## � Executar com Docker (Recomendado)

A maneira mais rápida de executar o projeto completo é usando Docker Compose, que configura automaticamente todos os serviços necessários.

### Pré-requisitos Docker
- Docker 20+ ([Download](https://www.docker.com/get-started))
- Docker Compose 2+ (geralmente incluído no Docker Desktop)

### 1. Configure as Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# Database
POSTGRES_DB=notionclone_dev
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123

# Backend
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production-min-32-chars
JWT_REFRESH_SECRET=your-super-secret-refresh-key-change-this-too-min-32-chars
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=seu-email@gmail.com
SMTP_PASSWORD=sua-senha-app

# Frontend
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_WS_URL=http://localhost:8080
```

### 2. Inicie os Contêineres

```bash
# Build e inicie todos os serviços
docker-compose up -d

# Ou force o rebuild das imagens
docker-compose up -d --build
```

Isso irá:
- ✅ Criar e iniciar o banco PostgreSQL
- ✅ Fazer build e iniciar o backend (Spring Boot)
- ✅ Fazer build e iniciar o frontend (Next.js)
- ✅ Configurar a rede entre os containers
- ✅ Executar migrations automaticamente

### 3. Acesse a Aplicação

Aguarde alguns segundos para os serviços iniciarem completamente, então acesse:

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **PostgreSQL:** localhost:5432

### 4. Verifique os Logs

```bash
# Todos os serviços
docker-compose logs -f

# Apenas backend
docker-compose logs -f backend

# Apenas frontend
docker-compose logs -f frontend

# Apenas database
docker-compose logs -f postgres
```

### 5. Gerenciar os Contêineres

```bash
# Parar os serviços (sem remover)
docker-compose stop

# Iniciar serviços parados
docker-compose start

# Parar e remover contêineres
docker-compose down

# Parar, remover contêineres e volumes (⚠️ APAGA O BANCO!)
docker-compose down -v

# Reiniciar um serviço específico
docker-compose restart backend

# Ver status dos contêineres
docker-compose ps
```

### Comandos Úteis do Docker

```bash
# Entrar no contêiner do backend (shell)
docker-compose exec backend sh

# Entrar no PostgreSQL
docker-compose exec postgres psql -U postgres -d notionclone_dev

# Executar migrations manualmente
docker-compose exec backend ./gradlew flywayMigrate

# Ver logs em tempo real
docker-compose logs -f --tail=100

# Reconstruir apenas um serviço
docker-compose up -d --build backend

# Ver uso de recursos (CPU, memória)
docker stats

# Limpar imagens não utilizadas
docker system prune -a
```

### Troubleshooting Docker

#### Porta já está em uso
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

#### Contêiner reiniciando constantemente
```bash
# Veja os logs para identificar o erro
docker-compose logs backend

# Erros comuns:
# - Variáveis de ambiente faltando (verifique .env)
# - Banco de dados não está pronto (aguarde 10-15 segundos)
# - Erro nas migrations (verifique SQL)
```

#### Build muito lento
```bash
# Use o cache do Docker
docker-compose build --parallel

# Se persistir, limpe o cache
docker builder prune -a
docker-compose build --no-cache
```

#### Banco de dados não persiste
```bash
# Verifique os volumes
docker volume ls

# O volume deve aparecer como: notion-clone_postgres_data
# NUNCA use: docker-compose down -v (apaga os dados!)
```

#### Erro "Cannot connect to Docker daemon"
```bash
# Windows/Mac: Verifique se o Docker Desktop está rodando

# Linux: Inicie o serviço
sudo systemctl start docker
sudo systemctl enable docker

# Adicione seu usuário ao grupo docker
sudo usermod -aG docker $USER
# Faça logout e login novamente
```

#### Erro ao fazer build do backend
```bash
# Limpe o cache do Gradle dentro do container
docker-compose exec backend ./gradlew clean

# Ou reconstrua do zero
docker-compose down
docker-compose build --no-cache backend
docker-compose up -d
```

#### Frontend não conecta ao backend
```bash
# Verifique as variáveis de ambiente
docker-compose exec frontend printenv | grep NEXT_PUBLIC

# Deve mostrar:
# NEXT_PUBLIC_API_URL=http://localhost:8080
# NEXT_PUBLIC_WS_URL=http://localhost:8080

# Se estiver errado, atualize docker-compose.yml e reinicie
docker-compose restart frontend
```

#### Migrations não executam
```bash
# Execute manualmente
docker-compose exec backend ./gradlew flywayMigrate

# Se falhar, verifique o status
docker-compose exec backend ./gradlew flywayInfo

# Repare se necessário
docker-compose exec backend ./gradlew flywayRepair
```

#### Espaço em disco esgotado
```bash
# Veja o uso de espaço
docker system df

# Limpe recursos não utilizados
docker system prune -a --volumes

# Cuidado: Isso remove TUDO que não está em uso
```

### Diferenças entre Desenvolvimento Local e Docker

| Aspecto | Desenvolvimento Local | Docker |
|---------|----------------------|---------|
| **Setup inicial** | Mais complexo (instalar Node, Java, PostgreSQL) | Simples (apenas Docker) |
| **Performance** | Melhor (nativo) | Boa (virtualização leve) |
| **Hot Reload** | Funciona perfeitamente | Pode ter delay no frontend |
| **Depuração** | Mais fácil (attach debugger) | Requer configuração extra |
| **Isolamento** | Não isolado (conflitos de porta) | Totalmente isolado |
| **Portabilidade** | Depende do SO | Funciona igual em todos os SOs |
| **Recomendado para** | Desenvolvimento ativo | Testes, CI/CD, Produção |

### Hot Reload no Docker (Desenvolvimento)

Se você quiser desenvolver com Docker e ter hot reload, modifique o `docker-compose.yml`:

```yaml
# Adicione volumes para mapear o código fonte
services:
  backend:
    volumes:
      - ./backend/src:/app/src
  
  frontend:
    volumes:
      - ./frontend:/app
      - /app/node_modules  # Não sobrescrever node_modules
```

## 🚀 Executar o Projeto (Desenvolvimento Local)

Se você **não** estiver usando Docker, siga estas instruções para executar localmente.

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

### Estratégia e Planejamento
- [Estratégia de Mercado](./docs/MARKET_STRATEGY.md) - Diferenciais competitivos vs Notion
- [Roadmap Estratégico](./docs/ROADMAP_STRATEGIC.md) - Visão de produto e prioridades
- [Plano de Implementação](./docs/IMPLEMENTATION_PLAN.md) - Épicos, tasks e métricas

### Técnica
- [Arquitetura](./docs/ARCHITECTURE.md) - Detalhes técnicos e decisões arquiteturais
- [Regras de Negócio](./docs/rules/) - Regras específicas de cada módulo
- [Troubleshooting](./docs/TROUBLESHOOTING.md) - Guia completo de solução de problemas
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

Encontrando problemas? Consulte nosso **[Guia Completo de Troubleshooting](docs/TROUBLESHOOTING.md)** com soluções para:

- 🗄️ **Banco de Dados** - Conexão, migrations, persistência
- ⚙️ **Backend** - Compilação, JDK, Gradle, Hibernate
- 🎨 **Frontend** - Next.js, hot reload, build
- 🐳 **Docker** - Containers, volumes, rede
- 🔌 **Conectividade** - CORS, WebSocket, portas
- 🔐 **Segurança** - JWT, BCrypt, SMTP
- 🧪 **Testes** - Jest, JUnit, mocks
- 📊 **Performance** - Memória, recursos, uploads

### Problemas Comuns (Quick Fix)

<details>
<summary>❌ Backend não inicia</summary>

```bash
# Verifique se o PostgreSQL está rodando
# Windows: services.msc | Linux/Mac: sudo systemctl status postgresql

# Teste a conexão
psql -U postgres -d notion_clone
```
</details>

<details>
<summary>❌ Frontend não conecta ao backend</summary>

```bash
# Verifique se o backend está rodando
curl http://localhost:8080/actuator/health

# Verifique o .env.local
cat frontend/.env.local
# Deve ter: NEXT_PUBLIC_API_URL="http://localhost:8080"
```
</details>

<details>
<summary>❌ Porta em uso</summary>

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```
</details>

<details>
<summary>❌ Docker container reiniciando</summary>

```bash
# Veja os logs
docker-compose logs backend

# Erros comuns: variáveis de ambiente faltando, banco não pronto
```
</details>

➡️ **[Ver todos os problemas e soluções](docs/TROUBLESHOOTING.md)**

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
- 🐛 Consulte o [Guia de Troubleshooting](./docs/TROUBLESHOOTING.md)
- 📚 Consulte a [Documentação](./docs/)
- 💬 Abra uma [Issue](https://github.com/seu-usuario/notion-clone/issues)

## 🗺️ Roadmap

O projeto segue um roadmap estratégico focado em **Developer Experience (DX)** e **Enterprise Governance**, diferenciando-se do Notion através de features específicas para empresas e desenvolvedores.

📋 **Veja o plano completo:** [Roadmap Estratégico](docs/ROADMAP_STRATEGIC.md) | [Plano de Implementação Detalhado](docs/IMPLEMENTATION_PLAN.md)

### Q1: Developer Experience
- [ ] **Diagramas como Código** - Mermaid.js nativo
- [ ] **Markdown Puro** - Import/Export sem perdas
- [ ] **Webhooks** - Integrações automáticas
- [ ] **Performance** - Otimização de carregamento

### Q2: Enterprise & Governança
- [ ] **Páginas Verificadas** - Sistema de validade de conteúdo
- [ ] **Audit Logs** - Rastreabilidade total
- [ ] **SSO (OIDC)** - Login corporativo
- [ ] **Permissões Granulares** - Controle fino de acesso

### Q3: Integrações Inteligentes
- [ ] **Smart Embeds** - Figma, Google Sheets, Loom
- [ ] **API Pública** - Documentação Swagger
- [ ] **Self-Hosted Installer** - Docker/Helm para on-premise

---

**⭐ Se este projeto te ajudou, deixe uma estrela no GitHub!**
