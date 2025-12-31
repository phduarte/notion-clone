# 🔄 GitHub Actions Workflows

Este diretório contém todos os workflows de CI/CD para o projeto Notion Clone.

## 📋 Workflows Disponíveis

### 1. 🧪 CI - Build and Test (`ci.yml`)

**Trigger**: Push e Pull Requests para `main` e `develop`

**Principais Jobs:**
- **Backend**: 
  - ✅ Ktlint (code style)
  - ✅ Detekt (code quality)
  - ✅ Testes unitários
  - ✅ Build do JAR
  - ✅ Coverage report (Codecov)
  
- **Frontend**:
  - ✅ ESLint
  - ✅ Prettier check
  - ✅ Type checking (TypeScript)
  - ✅ Testes
  - ✅ Build do Next.js

- **Integration Tests**: Testes end-to-end (apenas em PRs)
- **Security Scan**: Trivy + npm audit
- **Docker Build**: Valida imagens Docker
- **Code Quality**: SonarCloud analysis

**Serviços**:
- PostgreSQL 15 para testes

**Artifacts Gerados**:
- `backend-jar`: JAR do backend (7 dias)
- `frontend-build`: Build do frontend (7 dias)

---

### 2. 🚀 CD - Deploy to Production (`cd.yml`)

**Trigger**: 
- Push de tags `v*.*.*`
- Dispatch manual

**Principais Jobs:**
- **Build and Push**: Cria e envia imagens Docker para GitHub Container Registry
- **Deploy Staging**: Deploy para ambiente de staging
- **Deploy Production**: Deploy para produção (apenas em tags)
- **Rollback**: Rollback automático em caso de falha

**Ambientes**:
- `staging`: https://staging.notion-clone.com
- `production`: https://notion-clone.com

**Secrets Necessários**:
```
STAGING_HOST
STAGING_USER
STAGING_SSH_KEY
STAGING_URL

PRODUCTION_HOST
PRODUCTION_USER
PRODUCTION_SSH_KEY

API_URL
SLACK_WEBHOOK
```

**Health Checks**:
- Aguarda 30s após deploy
- Valida endpoint `/api/health`

---

### 3. 📦 Release (`release.yml`)

**Trigger**: Push de tags `v*.*.*`

**Principais Jobs:**
- **Create Release**: 
  - Gera changelog automático
  - Cria release no GitHub
  - Anexa JAR e build frontend
  - Publica no GitHub Releases
  
- **Update Documentation**:
  - Atualiza `package.json`
  - Atualiza `build.gradle.kts`
  - Cria PR automático

- **Notify**: Notifica release no Slack

**Formato de Tag**:
- `v1.0.0` → Produção
- `v1.0.0-rc.1` → Release Candidate (prerelease)
- `v1.0.0-beta.1` → Beta (prerelease)

**Artifacts Gerados**:
- `backend-{version}.jar`
- `frontend-{version}.tar.gz`

---

### 4. 🔄 Dependency Updates (`dependencies.yml`)

**Trigger**: 
- Schedule: Toda segunda-feira às 9h UTC
- Dispatch manual

**Principais Jobs:**
- **Update Frontend**: 
  - Usa `npm-check-updates`
  - Atualiza para versões minor
  - Cria PR automático
  
- **Update Backend**:
  - Usa Gradle `dependencyUpdates`
  - Cria issue com relatório

- **Security Audit**:
  - `npm audit` (frontend)
  - OWASP Dependency Check (backend)
  - Cria issue para vulnerabilidades críticas

- **Renovate Bot**: Atualizações automáticas (fim de semana)

**Renovate Config**: `.github/renovate.json`
- Agrupa dependências relacionadas
- Auto-merge para patches/minors
- Labels automáticos

---

## 🛠️ Configuração

### Secrets do Repositório

Configure os seguintes secrets no GitHub:

#### Deploy
```bash
STAGING_HOST         # staging.notion-clone.com
STAGING_USER         # deploy
STAGING_SSH_KEY      # Chave SSH privada
STAGING_URL          # https://staging.notion-clone.com

PRODUCTION_HOST      # notion-clone.com
PRODUCTION_USER      # deploy
PRODUCTION_SSH_KEY   # Chave SSH privada
```

#### Integrations
```bash
SONAR_TOKEN         # SonarCloud token
SLACK_WEBHOOK       # Slack webhook URL
```

#### Optional
```bash
CODECOV_TOKEN       # Codecov token (opcional, funciona sem)
```

### Badges

Adicione ao README.md:

```markdown
![CI](https://github.com/{org}/{repo}/workflows/CI%20-%20Build%20and%20Test/badge.svg)
![CD](https://github.com/{org}/{repo}/workflows/CD%20-%20Deploy%20to%20Production/badge.svg)
![Release](https://github.com/{org}/{repo}/workflows/Release/badge.svg)
[![codecov](https://codecov.io/gh/{org}/{repo}/branch/main/graph/badge.svg)](https://codecov.io/gh/{org}/{repo})
```

---

## 📝 Workflow Examples

### Criar Release

```bash
# 1. Atualizar versão
git tag v1.0.0
git push origin v1.0.0

# 2. GitHub Actions automaticamente:
# - Executa testes
# - Cria build
# - Publica release
# - Faz deploy
```

### Deploy Manual

```bash
# Via GitHub UI:
# Actions → CD - Deploy to Production → Run workflow
# Escolher: staging ou production
```

### Forçar Atualização de Dependências

```bash
# Via GitHub UI:
# Actions → Dependency Updates → Run workflow
```

---

## 🔍 Monitoramento

### Status dos Workflows

Acesse: `https://github.com/{org}/{repo}/actions`

### Logs

- Clique em qualquer workflow
- Selecione o job
- Veja logs detalhados

### Artifacts

- Disponíveis na página do workflow
- Baixe JARs, builds, reports

---

## 🐛 Troubleshooting

### CI Falhando

**Ktlint/Detekt errors**:
```bash
cd backend
./gradlew ktlintFormat
./gradlew detekt
```

**ESLint errors**:
```bash
cd frontend
npm run lint -- --fix
npx prettier --write "src/**/*.{ts,tsx}"
```

**Tests failing**:
```bash
# Backend
cd backend
./gradlew test --info

# Frontend
cd frontend
npm test -- --verbose
```

### Deploy Falhando

**SSH connection issues**:
- Verifique se SSH_KEY está correto
- Teste conexão manual: `ssh user@host`

**Docker issues**:
```bash
# No servidor
docker ps
docker logs notion-clone-backend
docker-compose logs
```

**Health check failing**:
- Verifique se serviços iniciaram: `docker-compose ps`
- Veja logs: `docker-compose logs -f`
- Teste manualmente: `curl localhost:8080/api/health`

### Release Issues

**Tag não dispara workflow**:
```bash
# Certifique-se de fazer push da tag
git push origin v1.0.0

# Não apenas criar localmente
git tag v1.0.0  # Isso NÃO dispara
```

**Changelog vazio**:
- Certifique-se de usar Conventional Commits
- Format: `feat: add feature`, `fix: bug`, etc.

---

## 📚 Recursos

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Workflow Syntax](https://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions)
- [GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Renovate Docs](https://docs.renovatebot.com/)

---

## 🔐 Segurança

- ✅ Secrets nunca aparecem nos logs
- ✅ PRs de forks não têm acesso a secrets
- ✅ Renovate apenas auto-merge patches seguros
- ✅ Security scans em todo push

---

## 📊 Metrics

Os workflows geram:
- Coverage reports (Codecov)
- Dependency reports
- Security scan results
- Build artifacts

Acesse via GitHub Actions artifacts ou integrações.
