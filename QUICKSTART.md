# 🚀 Quick Start Guide - Notion Clone

## ⚡ Iniciar em 5 minutos

### 1️⃣ PostgreSQL
\`\`\`bash
createdb notionclone_dev
\`\`\`

### 2️⃣ Backend
\`\`\`bash
cd backend

# Crie application-local.yml com suas configurações
# Veja SETUP.md para exemplo completo

./gradlew bootRun --args='--spring.profiles.active=local'
\`\`\`

### 3️⃣ Frontend
\`\`\`bash
cd frontend

pnpm install
cp .env.local.example .env.local

pnpm dev
\`\`\`

### 4️⃣ Acesse
- **Frontend**: http://localhost:3000
- **Backend**: http://localhost:8080
- **API Docs**: http://localhost:8080/swagger-ui.html

## 📝 Configuração Mínima

### Backend: `backend/src/main/resources/application-local.yml`

\`\`\`yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/notionclone_dev
    username: postgres
    password: postgres
  
  mail:
    host: smtp.gmail.com
    port: 587
    username: seu-email@gmail.com
    password: senha-de-app-do-gmail

app:
  jwt:
    secret: mude-para-um-secret-forte-com-minimo-32-caracteres-para-producao
    expiration: 900000
    refresh-expiration: 604800000
\`\`\`

### Frontend: `frontend/.env.local`

\`\`\`env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
\`\`\`

## 🎯 Primeiro Teste

1. Acesse http://localhost:3000
2. Clique em "Registrar"
3. Preencha os dados
4. Escolha plano FREE
5. Verifique seu email
6. Crie sua primeira página!

## 📚 Documentação Completa

Ver [SETUP.md](SETUP.md) para instruções detalhadas.

## ⚠️ Problemas Comuns

### Backend não conecta ao PostgreSQL
\`\`\`bash
# Verifique se está rodando
sudo systemctl status postgresql
\`\`\`

### Frontend não conecta ao backend
Verifique se o backend está em http://localhost:8080 e CORS está configurado.

### Email não funciona
Use uma senha de app do Gmail, não sua senha normal.

---

**Happy coding! 🎉**
