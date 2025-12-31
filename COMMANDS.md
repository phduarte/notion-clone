# 🔧 Comandos Úteis - Notion Clone

## Backend (Kotlin + Spring Boot)

### Build & Run

\`\`\`bash
# Build do projeto
./gradlew build

# Build sem testes
./gradlew build -x test

# Rodar aplicação (profile local)
./gradlew bootRun --args='--spring.profiles.active=local'

# Rodar em modo debug (porta 5005)
./gradlew bootRun --debug-jvm

# Clean build
./gradlew clean build
\`\`\`

### Tests

\`\`\`bash
# Rodar todos os testes
./gradlew test

# Rodar testes específicos
./gradlew test --tests "com.notionclone.auth.*"

# Rodar com relatório de coverage
./gradlew test jacocoTestReport

# Testes de integração
./gradlew integrationTest
\`\`\`

### Code Quality

\`\`\`bash
# Ktlint check
./gradlew ktlintCheck

# Ktlint format
./gradlew ktlintFormat

# Detekt (análise estática)
./gradlew detekt

# Verificar tudo
./gradlew check
\`\`\`

### Database

\`\`\`bash
# Limpar e recriar migrations
./gradlew flywayClean flywayMigrate

# Verificar status das migrations
./gradlew flywayInfo

# Validar migrations
./gradlew flywayValidate

# Reparar histórico de migrations
./gradlew flywayRepair
\`\`\`

---

## Frontend (Next.js + TypeScript)

### Development

\`\`\`bash
# Instalar dependências
pnpm install

# Modo desenvolvimento
pnpm dev

# Build de produção
pnpm build

# Iniciar produção
pnpm start

# Lint
pnpm lint

# Format com Prettier
pnpm format
\`\`\`

### Tests

\`\`\`bash
# Rodar testes
pnpm test

# Modo watch
pnpm test:watch

# Coverage
pnpm test:coverage
\`\`\`

### Type Checking

\`\`\`bash
# Verificar tipos
pnpm tsc --noEmit

# Verificar tipos em watch mode
pnpm tsc --noEmit --watch
\`\`\`

---

## Database (PostgreSQL)

### Criação e Gestão

\`\`\`bash
# Criar database
createdb notionclone_dev

# Dropar database
dropdb notionclone_dev

# Conectar ao database
psql -d notionclone_dev

# Conectar como postgres
psql -U postgres

# Listar databases
psql -l

# Executar SQL file
psql -d notionclone_dev -f script.sql
\`\`\`

### Queries Úteis (psql)

\`\`\`sql
-- Listar todas as tabelas
\dt

-- Descrever tabela
\d users

-- Listar indexes
\di

-- Listar schemas
\dn

-- Ver tamanho do database
SELECT pg_size_pretty(pg_database_size('notionclone_dev'));

-- Ver tamanho das tabelas
SELECT 
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Ver conexões ativas
SELECT * FROM pg_stat_activity;

-- Matar conexões
SELECT pg_terminate_backend(pid) 
FROM pg_stat_activity 
WHERE datname = 'notionclone_dev' AND pid <> pg_backend_pid();
\`\`\`

### Backup e Restore

\`\`\`bash
# Backup
pg_dump notionclone_dev > backup.sql

# Backup com compressão
pg_dump notionclone_dev | gzip > backup.sql.gz

# Restore
psql notionclone_dev < backup.sql

# Restore com compressão
gunzip -c backup.sql.gz | psql notionclone_dev
\`\`\`

---

## Docker

### Build e Run

\`\`\`bash
# Build e iniciar todos os serviços
docker-compose up -d

# Build e iniciar (com logs)
docker-compose up

# Rebuild se houver mudanças
docker-compose up --build

# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Ver logs
docker-compose logs

# Logs de um serviço específico
docker-compose logs backend

# Logs em tempo real
docker-compose logs -f backend
\`\`\`

### Gestão de Containers

\`\`\`bash
# Listar containers
docker ps

# Entrar em um container
docker exec -it notionclone-backend bash
docker exec -it notionclone-frontend sh
docker exec -it notionclone-db psql -U postgres

# Remover todos os containers
docker rm -f $(docker ps -aq)

# Remover images não usadas
docker image prune -a

# Limpar tudo (cuidado!)
docker system prune -a --volumes
\`\`\`

---

## Git

### Commits Convencionais

\`\`\`bash
git commit -m "feat: adiciona autenticação JWT"
git commit -m "fix: corrige bug no login"
git commit -m "docs: atualiza README"
git commit -m "style: formata código com ktlint"
git commit -m "refactor: melhora estrutura do AuthService"
git commit -m "test: adiciona testes para DocumentService"
git commit -m "chore: atualiza dependências"
\`\`\`

### Branches

\`\`\`bash
# Criar e mudar para branch
git checkout -b feature/nova-feature

# Mudar de branch
git checkout main

# Listar branches
git branch -a

# Deletar branch local
git branch -d feature/velha-feature

# Deletar branch remota
git push origin --delete feature/velha-feature
\`\`\`

### Reset e Revert

\`\`\`bash
# Desfazer último commit (mantém mudanças)
git reset --soft HEAD~1

# Desfazer último commit (descarta mudanças)
git reset --hard HEAD~1

# Reverter commit específico
git revert <commit-hash>

# Limpar arquivos não rastreados
git clean -fd
\`\`\`

---

## Segurança

### Gerar Secrets

\`\`\`bash
# JWT Secret (Linux/Mac)
openssl rand -base64 64

# JWT Secret (Windows PowerShell)
$bytes = New-Object byte[] 64
[System.Security.Cryptography.RNGCryptoServiceProvider]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)

# UUID
uuidgen

# Password forte
openssl rand -base64 32
\`\`\`

### Verificar Portas

\`\`\`bash
# Linux/Mac
lsof -i :8080
lsof -i :3000
lsof -i :5432

# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :3000
netstat -ano | findstr :5432
\`\`\`

### Matar Processos

\`\`\`bash
# Linux/Mac
kill -9 <PID>

# Windows
taskkill /PID <PID> /F
\`\`\`

---

## Logs

### Backend

\`\`\`bash
# Ver logs em tempo real
tail -f logs/spring.log

# Buscar por erro
grep -i "error" logs/spring.log

# Últimas 100 linhas
tail -n 100 logs/spring.log
\`\`\`

### Frontend

\`\`\`bash
# Logs do Next.js são no terminal onde rodou pnpm dev
# Mas pode redirecionar para arquivo:
pnpm dev > logs/nextjs.log 2>&1
\`\`\`

---

## Performance

### Backend

\`\`\`bash
# Verificar heap memory
jps -v

# Conectar JConsole
jconsole

# Spring Boot Actuator
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/metrics
\`\`\`

### Database

\`\`\`sql
-- Queries lentas
SELECT pid, now() - query_start as duration, query 
FROM pg_stat_activity 
WHERE state = 'active' 
ORDER BY duration DESC;

-- Cache hit ratio
SELECT 
  sum(heap_blks_read) as heap_read,
  sum(heap_blks_hit)  as heap_hit,
  sum(heap_blks_hit) / (sum(heap_blks_hit) + sum(heap_blks_read)) as ratio
FROM pg_statio_user_tables;

-- Indexes não utilizados
SELECT 
  schemaname,
  tablename,
  indexname,
  idx_scan
FROM pg_stat_user_indexes
WHERE idx_scan = 0;
\`\`\`

---

## Troubleshooting

### Port já em uso

\`\`\`bash
# Linux/Mac
sudo lsof -i :8080 -t | xargs kill -9

# Windows
FOR /F "tokens=5" %P IN ('netstat -ano ^| findstr :8080') DO taskkill /PID %P /F
\`\`\`

### Database não conecta

\`\`\`bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql  # Linux
brew services list               # Mac
Get-Service postgresql*          # Windows

# Iniciar PostgreSQL
sudo systemctl start postgresql  # Linux
brew services start postgresql   # Mac
\`\`\`

### Migrations falharam

\`\`\`bash
# Resetar migrations (CUIDADO: perde dados!)
psql -d notionclone_dev -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"

# Ou via Gradle
./gradlew flywayClean flywayMigrate
\`\`\`

---

## Aliases Úteis (Opcional)

Adicione ao seu `.bashrc` ou `.zshrc`:

\`\`\`bash
# Backend
alias ncb='cd ~/notion-clone/backend'
alias ncbr='./gradlew bootRun --args="--spring.profiles.active=local"'
alias ncbt='./gradlew test'

# Frontend
alias ncf='cd ~/notion-clone/frontend'
alias ncfd='pnpm dev'
alias ncfb='pnpm build'

# Database
alias ncdb='psql -d notionclone_dev'

# Docker
alias ncd='docker-compose'
alias ncdup='docker-compose up -d'
alias ncddown='docker-compose down'
alias ncdlogs='docker-compose logs -f'
\`\`\`

Windows PowerShell (perfil: `$PROFILE`):

\`\`\`powershell
function ncb { Set-Location ~/notion-clone/backend }
function ncbr { .\gradlew.bat bootRun --args='--spring.profiles.active=local' }
function ncf { Set-Location ~/notion-clone/frontend }
function ncfd { pnpm dev }
\`\`\`

---

**💡 Dica**: Salve este arquivo como referência rápida!
