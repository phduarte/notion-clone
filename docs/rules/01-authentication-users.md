# Regras de Negócio - Autenticação e Usuários

## 1. Registro de Usuário

### 1.1. Campos Obrigatórios
- **Nome** (name)
  - Mínimo 2 caracteres
  - Máximo 100 caracteres
  - Apenas letras, espaços e acentos
  - Não pode conter números ou caracteres especiais

- **Username** (username)
  - Mínimo 3 caracteres
  - Máximo 20 caracteres
  - Apenas letras minúsculas, números e underscore
  - Deve começar com letra
  - **Deve ser único no sistema**
  
- **Email** (email)
  - Formato válido de email (RFC 5322)
  - **Deve ser único no sistema**
  - Case-insensitive para validação de duplicatas
  
- **Senha** (password)
  - Veja regras específicas em 1.3
  
- **Plano** (plan)
  - Valores aceitos: FREE, PRO, PREMIUM
  - Default: FREE se não especificado

### 1.2. Campos Opcionais
- **Telefone** (phone)
  - Formato: +55 (XX) XXXXX-XXXX
  - Validação de formato brasileiro
  - Opcional mas se fornecido deve ser válido

### 1.3. Validação de Senha Forte

A senha deve atender **TODOS** os critérios abaixo:

1. **Comprimento**: Mínimo 8 caracteres
2. **Letra Maiúscula**: Pelo menos 1 letra maiúscula (A-Z)
3. **Letra Minúscula**: Pelo menos 1 letra minúscula (a-z)
4. **Número**: Pelo menos 1 número (0-9)
5. **Caractere Especial**: Pelo menos 1 (@, #, $, %, &, *, !, etc.)
6. **Sem Números Sequenciais**: Não pode conter sequências como "123", "234", "12345", etc.
7. **Não Contém Nome**: A senha não pode conter o nome do usuário (case-insensitive)

**Exemplos de senhas válidas:**
- `Segura@123!`
- `P@ssw0rd!`
- `MyP@ss456`

**Exemplos de senhas inválidas:**
- `senha123` (falta maiúscula e caractere especial)
- `SENHA@123` (falta minúscula)
- `SenhaForte` (falta número e caractere especial)
- `Maria@1234` (contém números sequenciais)
- `Maria@Senha1` (contém o nome "Maria")

### 1.4. Sugestão de Username

Se o username desejado já existir, o sistema deve **sugerir alternativas** similar ao Reddit:

**Algoritmo de sugestão:**
1. Adicionar números aleatórios ao final: `username123`, `username456`
2. Adicionar underscore e números: `username_123`, `username_456`
3. Adicionar prefixo: `cool_username`, `super_username`
4. Sugerir até 5 alternativas disponíveis

**Exemplo:**
- Username desejado: `joao`
- Username já existe
- Sugestões: `joao_123`, `joao456`, `joao_silva`, `joaodev`, `cool_joao`

### 1.5. Validação de Email Duplicado

- Antes de criar o usuário, verificar se o email já existe
- Comparação case-insensitive: `joao@email.com` == `JOAO@email.com`
- Se existir, retornar erro HTTP 409 (Conflict)
- Mensagem: "Este email já está cadastrado"

### 1.6. Fluxo de Registro

```
1. Usuário preenche formulário
2. Frontend valida campos (Zod)
3. Frontend envia POST /api/auth/register
4. Backend valida DTO (@Valid)
5. Backend verifica duplicatas:
   - Email existe? → Erro 409
   - Username existe? → Sugere alternativas
6. Backend valida regras de senha
7. Backend cria hash da senha (BCrypt, strength 10)
8. Backend salva usuário no banco (status: PENDING_VERIFICATION)
9. Backend gera código de verificação (6 dígitos, expira em 15 min)
10. Backend salva código na tabela verification_codes
11. Backend envia email com código
12. Backend retorna sucesso (HTTP 201)
13. Frontend redireciona para tela de verificação
```

### 1.7. Código de Verificação de Email

- **Formato**: 6 dígitos numéricos (ex: 123456)
- **Geração**: Aleatória (não sequencial)
- **Validade**: 15 minutos
- **Tentativas**: Máximo 5 tentativas incorretas
- **Reenvio**: Permitido após 1 minuto do último envio
- **Limite de reenvio**: Máximo 3 reenvios por hora

**Template do Email:**
```
Assunto: Confirme seu email - Notion Clone

Olá [Nome],

Bem-vindo ao Notion Clone!

Use o código abaixo para confirmar seu email:

[123456]

Este código expira em 15 minutos.

Se você não criou esta conta, ignore este email.

Atenciosamente,
Equipe Notion Clone
```

### 1.8. Confirmação do Código

**Endpoint**: POST /api/auth/verify-email

**Payload:**
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

**Validações:**
1. Email existe no sistema?
2. Código existe e está válido?
3. Código não expirou?
4. Não excedeu limite de tentativas?

**Sucesso:**
- Atualiza status do usuário para ACTIVE
- Marca código como usado
- Retorna tokens JWT (access + refresh)
- HTTP 200

**Erros:**
- Código inválido: HTTP 400 "Código inválido"
- Código expirado: HTTP 400 "Código expirado. Solicite um novo código"
- Limite de tentativas: HTTP 429 "Muitas tentativas. Solicite um novo código"

---

## 2. Login

### 2.1. Credenciais

**Endpoint**: POST /api/auth/login

**Payload:**
```json
{
  "email": "user@example.com",
  "password": "senha123"
}
```

### 2.2. Validações

1. Email e senha são obrigatórios
2. Buscar usuário por email (case-insensitive)
3. Usuário existe?
4. Usuário está ativo? (status = ACTIVE)
5. Senha está correta? (BCrypt.matches)
6. Conta não está bloqueada?

### 2.3. Bloqueio de Conta

**Proteção contra brute force:**
- Após 5 tentativas de login incorretas consecutivas
- Bloquear conta por 15 minutos
- Enviar email notificando o bloqueio
- Reset do contador após login bem-sucedido

### 2.4. Tokens JWT

**Access Token:**
- Algoritmo: HS256 (ou RS256 para produção)
- Expiração: 15 minutos
- Claims: user_id, email, username, plan, roles
- Storage: httpOnly cookie

**Refresh Token:**
- Algoritmo: HS256
- Expiração: 7 dias
- Claims: user_id, token_id (para revogação)
- Storage: httpOnly cookie
- Salvo no banco para permitir revogação

**Exemplo de Access Token Claims:**
```json
{
  "sub": "user-uuid",
  "email": "user@example.com",
  "username": "johndoe",
  "plan": "PRO",
  "roles": ["USER"],
  "iat": 1640995200,
  "exp": 1640996100
}
```

### 2.5. Primeiro Login

- Backend verifica campo `first_login` (boolean)
- Se `true`:
  - Retorna flag `isFirstLogin: true` na resposta
  - Frontend exibe popup de onboarding
  - Após onboarding, frontend chama PATCH /api/users/me/first-login
  - Backend atualiza `first_login` para `false`

### 2.6. Resposta de Sucesso

**HTTP 200:**
```json
{
  "user": {
    "id": "uuid",
    "name": "João Silva",
    "username": "joao",
    "email": "joao@example.com",
    "plan": "PRO",
    "avatar": "url"
  },
  "accessToken": "eyJhbG...",
  "refreshToken": "eyJhbG...",
  "isFirstLogin": false
}
```

---

## 3. Recuperação de Senha

### 3.1. Solicitar Código de Recuperação

**Endpoint**: POST /api/auth/forgot-password

**Payload:**
```json
{
  "email": "user@example.com"
}
```

**Fluxo:**
1. Verificar se email existe
2. Gerar código de 6 dígitos
3. Salvar código com expiração de 15 minutos
4. Enviar email com código
5. **SEMPRE retornar sucesso** (mesmo se email não existir - segurança)

**Template do Email:**
```
Assunto: Recuperação de Senha - Notion Clone

Olá [Nome],

Recebemos uma solicitação de recuperação de senha.

Use o código abaixo para redefinir sua senha:

[123456]

Este código expira em 15 minutos.

Se você não solicitou esta recuperação, ignore este email.

Atenciosamente,
Equipe Notion Clone
```

### 3.2. Limites de Requisição

- Máximo 3 solicitações por hora por email
- Intervalo mínimo de 1 minuto entre solicitações
- Código anterior é invalidado ao gerar novo

### 3.3. Validar Código e Redefinir Senha

**Endpoint**: POST /api/auth/reset-password

**Payload:**
```json
{
  "email": "user@example.com",
  "code": "123456",
  "newPassword": "NovaSenha@123"
}
```

**Validações:**
1. Email existe?
2. Código é válido?
3. Código não expirou?
4. Nova senha atende regras de senha forte?
5. Nova senha é diferente da anterior?

**Sucesso:**
- Atualiza senha (novo hash BCrypt)
- Invalida código
- Revoga todos os refresh tokens existentes
- Envia email confirmando alteração
- HTTP 200

---

## 4. Logout

### 4.1. Logout Simples

**Endpoint**: POST /api/auth/logout

**Comportamento:**
- Invalida o refresh token no banco
- Remove cookies httpOnly
- Frontend limpa estado local
- HTTP 204 (No Content)

### 4.2. Logout de Todas as Sessões

**Endpoint**: POST /api/auth/logout-all

**Comportamento:**
- Invalida TODOS os refresh tokens do usuário
- Remove cookies httpOnly
- Força logout de todos os dispositivos
- HTTP 204

---

## 5. Refresh Token

### 5.1. Renovar Access Token

**Endpoint**: POST /api/auth/refresh

**Payload:**
```json
{
  "refreshToken": "eyJhbG..."
}
```

**Validações:**
1. Refresh token é válido?
2. Token não expirou?
3. Token não foi revogado?
4. Usuário ainda está ativo?

**Sucesso:**
- Gera novo access token
- (Opcional) Gera novo refresh token (rotation)
- Invalida refresh token anterior se rotation
- HTTP 200

**Rotation de Refresh Token:**
- A cada renovação, gera novo refresh token
- Aumenta segurança
- Evita reutilização de tokens

---

## 6. Perfil do Usuário

### 6.1. Obter Perfil

**Endpoint**: GET /api/users/me

**Resposta:**
```json
{
  "id": "uuid",
  "name": "João Silva",
  "username": "joao",
  "email": "joao@example.com",
  "phone": "+55 (11) 99999-9999",
  "plan": "PRO",
  "avatar": "url",
  "emailVerified": true,
  "communicationPreferences": {
    "emailNotifications": true,
    "marketingEmails": false
  },
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

### 6.2. Atualizar Perfil

**Endpoint**: PATCH /api/users/me

**Campos Editáveis:**
- name
- username (verificar unicidade)
- phone
- avatar (upload de imagem)
- communicationPreferences

**Campos NÃO Editáveis:**
- email (requer verificação separada)
- password (endpoint específico)
- plan (apenas admins)

### 6.3. Alterar Email

**Endpoint**: POST /api/users/me/change-email

**Payload:**
```json
{
  "newEmail": "newemail@example.com",
  "password": "senhaAtual"
}
```

**Fluxo:**
1. Validar senha atual
2. Verificar se novo email já existe
3. Gerar código de verificação
4. Enviar código para NOVO email
5. Usuário confirma código
6. Email é atualizado

### 6.4. Alterar Senha

**Endpoint**: POST /api/users/me/change-password

**Payload:**
```json
{
  "currentPassword": "senhaAtual",
  "newPassword": "novaSenha@123"
}
```

**Validações:**
1. Senha atual está correta?
2. Nova senha atende regras?
3. Nova senha é diferente da atual?

**Sucesso:**
- Atualiza senha
- Revoga todos refresh tokens (exceto o atual - opcional)
- Envia email notificando alteração

### 6.5. Upload de Avatar

**Endpoint**: POST /api/users/me/avatar

**Content-Type**: multipart/form-data

**Validações:**
- Tamanho máximo: 2MB
- Formatos aceitos: jpg, jpeg, png, webp
- Redimensionar para 256x256 pixels
- Salvar no storage (local ou cloud)

---

## 7. Exclusão de Conta

### 7.1. Solicitar Exclusão

**Endpoint**: DELETE /api/users/me

**Payload:**
```json
{
  "password": "senhaAtual",
  "reason": "Não uso mais o serviço",
  "feedback": "Opcional: detalhes adicionais"
}
```

### 7.2. Motivos de Exclusão (Enum)

- `NOT_USING` - Não uso mais
- `FOUND_ALTERNATIVE` - Encontrei alternativa
- `TOO_EXPENSIVE` - Muito caro
- `MISSING_FEATURES` - Falta funcionalidades
- `PRIVACY_CONCERNS` - Preocupações com privacidade
- `OTHER` - Outro motivo

### 7.3. Confirmação no Frontend

Antes de enviar a requisição, exibir modal de confirmação:

**Modal:**
```
Tem certeza que deseja excluir sua conta?

⚠️ Esta ação não pode ser desfeita!

Ao excluir sua conta:
- Todos os seus documentos serão deletados permanentemente
- Você perderá acesso a todas as páginas compartilhadas
- Seu plano será cancelado imediatamente
- Os dados serão mantidos por 30 dias para recuperação

Por favor, informe o motivo da exclusão:
[Dropdown com motivos]

[Campo opcional para feedback]

[Input para senha]

[Cancelar]  [Confirmar Exclusão]
```

### 7.4. Soft Delete

- Não deletar fisicamente do banco
- Atualizar status para `DELETED`
- Adicionar campo `deleted_at` (timestamp)
- Manter dados por 30 dias para possível recuperação
- Após 30 dias, job agendado deleta permanentemente

### 7.5. Salvar Feedback

Tabela `account_deletion_feedback`:
```sql
CREATE TABLE account_deletion_feedback (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  reason VARCHAR(50) NOT NULL,
  feedback TEXT,
  deleted_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP NOT NULL
);
```

### 7.6. Após Exclusão

- Invalidar todos os tokens
- Enviar email de confirmação
- Limpar cookies
- Redirecionar para página de confirmação

**Email de Confirmação:**
```
Assunto: Conta Excluída - Notion Clone

Olá [Nome],

Sua conta foi excluída com sucesso.

Seus dados serão mantidos por 30 dias caso você mude de ideia.
Para reativar sua conta, entre em contato conosco.

Sentiremos sua falta! 💔

Atenciosamente,
Equipe Notion Clone
```

---

## 8. Status do Usuário

### 8.1. Estados Possíveis (Enum)

```kotlin
enum class UserStatus {
    PENDING_VERIFICATION,  // Aguardando verificação de email
    ACTIVE,                // Conta ativa
    BLOCKED,              // Bloqueada (por tentativas de login)
    SUSPENDED,            // Suspensa (por admin/violação)
    DELETED               // Deletada (soft delete)
}
```

### 8.2. Transições de Estado

```
PENDING_VERIFICATION → ACTIVE (após verificar email)
ACTIVE → BLOCKED (após 5 tentativas de login incorretas)
BLOCKED → ACTIVE (após 15 minutos ou reset manual)
ACTIVE → SUSPENDED (por admin)
SUSPENDED → ACTIVE (por admin)
ACTIVE → DELETED (por usuário)
DELETED → ACTIVE (recuperação em 30 dias)
```

---

## 9. Preferências de Comunicação

### 9.1. Opções Disponíveis

```json
{
  "communicationPreferences": {
    "emailNotifications": true,      // Notificações do sistema
    "marketingEmails": false,        // Emails promocionais
    "weeklyDigest": false,           // Resumo semanal
    "collaborationAlerts": true,     // Alertas de colaboração
    "documentReminders": true        // Lembretes de documentos
  }
}
```

### 9.2. Atualizar Preferências

**Endpoint**: PATCH /api/users/me/preferences

**Regras:**
- `emailNotifications` não pode ser desabilitado (emails críticos)
- Outras preferências são opcionais
- Salvar histórico de mudanças (auditoria)

---

## 10. Segurança

### 10.1. Rate Limiting

**Endpoints Críticos:**
- Login: 5 tentativas / 15 minutos por IP
- Registro: 3 tentativas / hora por IP
- Forgot Password: 3 tentativas / hora por email
- Verify Email: 5 tentativas / código

### 10.2. Auditoria

Registrar eventos em tabela `audit_log`:
- Login bem-sucedido
- Login falho
- Alteração de senha
- Alteração de email
- Exclusão de conta
- Alteração de plano

### 10.3. Validações de Segurança

- Sanitizar todos os inputs
- Prevenir SQL Injection (JPA faz isso)
- Prevenir XSS (sanitizar HTML)
- CSRF protection (Spring Security)
- CORS configurado corretamente

---

## 11. Mensagens de Erro

### 11.1. Padrão de Resposta de Erro

```json
{
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "Email ou senha incorretos",
    "details": {},
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

### 11.2. Códigos de Erro Comuns

- `INVALID_CREDENTIALS` - Credenciais inválidas
- `EMAIL_ALREADY_EXISTS` - Email já cadastrado
- `USERNAME_ALREADY_EXISTS` - Username já existe
- `WEAK_PASSWORD` - Senha não atende requisitos
- `INVALID_VERIFICATION_CODE` - Código inválido
- `EXPIRED_VERIFICATION_CODE` - Código expirado
- `ACCOUNT_BLOCKED` - Conta bloqueada
- `ACCOUNT_NOT_VERIFIED` - Email não verificado
- `TOO_MANY_ATTEMPTS` - Muitas tentativas
