# 📋 Plano de Implementação Detalhado

Este documento detalha os épicos, tasks e critérios de aceitação para implementar o roadmap estratégico do projeto.

---

## 🚀 Épico 1: Developer Experience (DX) Core
**Objetivo:** Tornar o sistema a ferramenta favorita dos desenvolvedores para documentação técnica.  
**Prazo:** Q1  
**Prioridade:** 🔴 Alta

### Task 1.1: Suporte a Diagramas com Mermaid.js
**Estimativa:** 8 pontos (2 semanas)  
**Responsável:** Backend + Frontend  

#### Subtasks Backend:
- [ ] Criar enum `BlockType.MERMAID` no domínio
- [ ] Adicionar validação de sintaxe Mermaid no DTO
- [ ] Permitir salvar blocos Mermaid como JSON no campo `content`
- [ ] Criar testes unitários para validação

#### Subtasks Frontend:
- [ ] Instalar biblioteca `mermaid` (`pnpm add mermaid`)
- [ ] Criar componente `MermaidBlock.tsx`
- [ ] Integrar com Tiptap como custom node
- [ ] Adicionar comando `/mermaid` na paleta
- [ ] Implementar preview ao vivo (debounced)
- [ ] Adicionar syntax highlighting no editor de código
- [ ] Tratar erros de sintaxe com mensagens amigáveis

#### Critérios de Aceitação:
- [ ] Usuário digita `/mermaid` e abre um bloco de código
- [ ] Ao digitar código Mermaid válido, o diagrama renderiza em tempo real
- [ ] Suporta flowchart, sequence, gantt, class diagrams
- [ ] Se houver erro de sintaxe, mostra mensagem clara
- [ ] O diagrama é exportado corretamente em PDF
- [ ] Funciona em modo claro e escuro

---

### Task 1.2: Importação/Exportação Markdown "Lossless"
**Estimativa:** 13 pontos (3 semanas)  
**Responsável:** Backend + Frontend

#### Subtasks Backend:
- [ ] Criar endpoint `POST /api/documents/import`
  - Aceita arquivo `.md` (multipart/form-data)
  - Parseia Markdown usando `commonmark-java`
  - Converte para estrutura JSON do Tiptap
  - Preserva tabelas, code blocks, imagens
- [ ] Criar endpoint `GET /api/documents/:id/export?format=markdown`
  - Converte JSON do Tiptap para Markdown padrão
  - Preserva formatação (CommonMark spec)
  - Gera arquivo `.md` para download
- [ ] Criar serviço `MarkdownConverter` com métodos:
  - `markdownToTiptapJson(markdown: String): JsonNode`
  - `tiptapJsonToMarkdown(json: JsonNode): String`
- [ ] Adicionar testes com arquivos `.md` complexos

#### Subtasks Frontend:
- [ ] Criar botão "Importar Markdown" no dashboard
- [ ] Implementar upload de arquivo `.md`
- [ ] Criar modal de preview antes de importar
- [ ] Adicionar botão "Exportar como Markdown" no menu da página
- [ ] Fazer download do arquivo `.md`
- [ ] Adicionar loading states e feedback de sucesso

#### Critérios de Aceitação:
- [ ] Usuário importa um `.md` do GitHub e renderiza perfeitamente
- [ ] Tabelas, listas, code blocks preservados na importação
- [ ] Exportar → Importar novamente resulta em conteúdo idêntico (lossless)
- [ ] Imagens externas (URLs) são importadas corretamente
- [ ] Metadados (frontmatter YAML) são ignorados sem quebrar
- [ ] Suporta arquivos de até 10MB

---

### Task 1.3: Sistema de Webhooks (MVP)
**Estimativa:** 8 pontos (2 semanas)  
**Responsável:** Backend

#### Subtasks:
- [ ] Criar entidade `Webhook`:
  ```kotlin
  @Entity
  data class Webhook(
      @Id val id: UUID,
      val userId: UUID,
      val url: String,
      val events: List<String>, // ["page.created", "page.updated", etc.]
      val secret: String, // Para HMAC signature
      val active: Boolean = true,
      val createdAt: Instant
  )
  ```
- [ ] Criar CRUD de webhooks (apenas Admin/Enterprise)
- [ ] Criar serviço `WebhookDispatcher`:
  - Método `dispatch(event: WebhookEvent)`
  - Assina payload com HMAC-SHA256
  - Retry logic (3 tentativas com backoff exponencial)
  - Timeout de 5 segundos
- [ ] Integrar eventos:
  - `page.created` → Após criar documento
  - `page.updated` → Após salvar auto-save
  - `page.deleted` → Após soft delete
  - `page.shared` → Após compartilhar com usuário
- [ ] Criar tabela de logs de webhooks para debug
- [ ] UI simples: Tabela de webhooks + formulário de cadastro

#### Critérios de Aceitação:
- [ ] Admin cadastra webhook com URL e eventos
- [ ] Webhook dispara quando evento ocorre
- [ ] Payload inclui: `event`, `timestamp`, `data`, `signature`
- [ ] Signature é validável com secret
- [ ] Se webhook falhar 3x, marca como inativo
- [ ] Logs mostram histórico de disparos (sucesso/falha)

---

## 🛡️ Épico 2: Enterprise Governance & Security
**Objetivo:** Vender para CTOs e departamentos jurídicos.  
**Prazo:** Q2  
**Prioridade:** 🔴 Alta

### Task 2.1: Sistema de "Verified Pages" (Governança)
**Estimativa:** 5 pontos (1 semana)  
**Responsável:** Backend + Frontend

#### Subtasks Backend:
- [ ] Adicionar campos à entidade `Document`:
  ```kotlin
  val verifiedAt: Instant? = null
  val verifiedBy: UUID? = null
  val verificationFrequency: Int = 180 // dias
  val nextVerificationDate: Instant? = null
  ```
- [ ] Criar endpoint `PATCH /api/documents/:id/verify`
  - Atualiza `verifiedAt`, `verifiedBy`, calcula `nextVerificationDate`
- [ ] Criar scheduled job (`@Scheduled(cron = "0 0 9 * * *")`)
  - Verifica páginas com `nextVerificationDate` vencida
  - Envia email para `verifiedBy` pedindo revalidação
- [ ] Adicionar template de email Thymeleaf
- [ ] Permitir configurar frequência (90, 180, 365 dias) no plano

#### Subtasks Frontend:
- [ ] Adicionar selo visual no topo da página:
  - 🟢 Verde → Verificada recentemente
  - 🟡 Amarelo → Próxima da validade (faltam <30 dias)
  - 🔴 Vermelho → Expirada
  - ⚪ Cinza → Nunca verificada
- [ ] Botão "Marcar como Verificada" (apenas dono ou admin)
- [ ] Modal para escolher frequência de revalidação
- [ ] Badge na listagem de páginas

#### Critérios de Aceitação:
- [ ] Usuário marca página como verificada
- [ ] Selo verde aparece no topo
- [ ] 150 dias depois (se config for 180), usuário recebe email
- [ ] Email tem link direto para a página
- [ ] Se não revalidar, selo fica vermelho após 180 dias
- [ ] Apenas planos Team/Enterprise têm essa feature

---

### Task 2.2: Audit Logs (Rastreabilidade Total)
**Estimativa:** 13 pontos (3 semanas)  
**Responsável:** Backend + Frontend

#### Subtasks Backend:
- [ ] Criar entidade `AuditLog`:
  ```kotlin
  @Entity
  data class AuditLog(
      @Id val id: UUID,
      val userId: UUID?,
      val action: String, // "login", "page.delete", "export.pdf", etc.
      val resourceType: String?, // "Document", "User", etc.
      val resourceId: UUID?,
      val ipAddress: String,
      val userAgent: String,
      val metadata: JsonNode?, // Dados extras (ex: old vs new values)
      val timestamp: Instant
  )
  ```
- [ ] Criar serviço `AuditLogger`:
  ```kotlin
  fun log(action: String, resourceType: String?, resourceId: UUID?, metadata: Map<String, Any>?)
  ```
- [ ] Integrar AOP (Aspect) para interceptar:
  - Todos os métodos de controller com `@PostMapping`, `@PatchMapping`, `@DeleteMapping`
  - Capturar IP do request (`HttpServletRequest.getRemoteAddr()`)
  - Capturar User-Agent
- [ ] Criar endpoint `GET /api/audit-logs?page=0&size=50&resourceId=...&action=...`
  - Apenas planos Enterprise
  - Paginação e filtros
- [ ] Adicionar índices no banco:
  - `(userId, timestamp)`
  - `(resourceId, timestamp)`
  - `(action, timestamp)`

#### Subtasks Frontend:
- [ ] Criar página `/admin/audit-logs`
- [ ] Tabela com colunas: Timestamp, Usuário, Ação, Recurso, IP
- [ ] Filtros: Data (range), Usuário, Ação, Recurso
- [ ] Exportar logs como CSV
- [ ] Detalhes expandíveis (metadata JSON)

#### Critérios de Aceitação:
- [ ] Toda ação crítica gera log (login, delete, export, permission change)
- [ ] Admin vê histórico completo de ações
- [ ] Filtros funcionam corretamente
- [ ] Logs são imutáveis (não podem ser editados/deletados via UI)
- [ ] Performance: Busca em 100k logs retorna em <500ms
- [ ] Feature exclusiva do plano Enterprise

---

### Task 2.3: SSO com OpenID Connect (OIDC)
**Estimativa:** 13 pontos (3 semanas)  
**Responsável:** Backend + Frontend

#### Subtasks Backend:
- [ ] Adicionar dependência Spring Security OAuth2 Client
- [ ] Configurar `application.yml` com provedor OIDC genérico:
  ```yaml
  spring:
    security:
      oauth2:
        client:
          registration:
            oidc:
              client-id: ${OIDC_CLIENT_ID}
              client-secret: ${OIDC_CLIENT_SECRET}
              scope: openid, profile, email
          provider:
            oidc:
              issuer-uri: ${OIDC_ISSUER_URI}
  ```
- [ ] Criar endpoint `/api/auth/sso/login?provider=oidc`
  - Redireciona para provedor
- [ ] Criar endpoint `/api/auth/sso/callback`
  - Recebe código de autorização
  - Troca por access token
  - Cria/atualiza usuário no banco
  - Retorna JWT do sistema
- [ ] Associar usuários SSO ao domínio da empresa:
  - `@empresa.com.br` → Workspace da empresa
- [ ] Permitir configurar múltiplos provedores (Google, Azure AD, Keycloak)

#### Subtasks Frontend:
- [ ] Adicionar botão "Login com SSO" na tela de login
- [ ] Modal para escolher provedor (se houver múltiplos)
- [ ] Fluxo de redirecionamento (loading state)
- [ ] Tratar erros de SSO (provider offline, denied, etc.)

#### Critérios de Aceitação:
- [ ] Usuário clica em "Login com Google Workspace"
- [ ] É redirecionado para Google
- [ ] Após autorizar, volta para o sistema logado
- [ ] Conta é criada automaticamente no primeiro login
- [ ] Funciona com Google, Azure AD e Keycloak
- [ ] Apenas planos Team/Enterprise (configurável por admin)
- [ ] Se domínio do email não bate com workspace, mostra erro

---

## 🔌 Épico 3: Smart Integrations
**Objetivo:** Centralizar conhecimento sem duplicar ferramentas.  
**Prazo:** Q3  
**Prioridade:** 🟡 Média

### Task 3.1: Bloco de iFrame Seguro (Smart Embeds)
**Estimativa:** 8 pontos (2 semanas)  
**Responsável:** Frontend + Backend

#### Subtasks Backend:
- [ ] Criar whitelist de domínios permitidos:
  ```kotlin
  val allowedEmbedDomains = listOf(
      "figma.com",
      "www.figma.com",
      "docs.google.com",
      "sheets.google.com",
      "youtube.com",
      "www.youtube.com",
      "loom.com",
      "www.loom.com",
      "miro.com",
      "excalidraw.com"
  )
  ```
- [ ] Validar URL no DTO antes de salvar
- [ ] Sanitizar URL (remover scripts)
- [ ] Endpoint de preview: `GET /api/embed/preview?url=...`
  - Retorna metadados (título, thumbnail) usando Open Graph

#### Subtasks Frontend:
- [ ] Criar componente `IframeBlock.tsx`
- [ ] Adicionar comando `/embed` na paleta
- [ ] Modal para colar URL e fazer preview
- [ ] Sandbox attributes: `allow="fullscreen"`
- [ ] Aspect ratio responsivo (16:9 default, configurável)
- [ ] Loading skeleton enquanto carrega
- [ ] Fallback se embed falhar (mostra link)

#### Critérios de Aceitação:
- [ ] Usuário digita `/embed` e cola URL do Figma
- [ ] Iframe renderiza com o design
- [ ] URLs de domínios não permitidos são bloqueadas
- [ ] Iframe é responsivo (mobile-friendly)
- [ ] Não permite `<script>` no src (XSS protection)
- [ ] Exportação PDF mostra link ao invés do iframe

---

### Task 3.2: Embed e Visualização de PDF
**Estimativa:** 5 pontos (1 semana)  
**Responsável:** Frontend

#### Subtasks:
- [ ] Instalar `react-pdf` (`pnpm add react-pdf`)
- [ ] Criar componente `PDFViewer.tsx`
- [ ] Permitir upload de PDF (reutilizar lógica de imagem)
- [ ] Renderizar PDF inline com navegação de páginas
- [ ] Adicionar controles: Zoom, Download, Fullscreen
- [ ] Thumbnail das primeiras 3 páginas

#### Critérios de Aceitação:
- [ ] Usuário faz upload de PDF
- [ ] PDF renderiza inline (não abre em nova aba)
- [ ] Controles de navegação funcionam (prev/next page)
- [ ] Botão de download disponível
- [ ] Limite de 10MB por PDF

---

## 🗑️ Backlog Congelado (Despriorizados)

Estas features NÃO serão implementadas nos próximos 9 meses. Se houver demanda real de clientes, reavaliar.

### ❌ Templates Visuais Complexos
- **Motivo:** Devs preferem começar do zero. Templates são "bloat" para o público-alvo.
- **Alternativa:** Documentar "Receitas" (exemplos de estrutura) no blog/docs.

### ❌ Comentários em Linha (Inline/Thread Comments)
- **Motivo:** Complexidade técnica alta (CRDTs, conflitos). Google Docs levou anos para fazer isso bem.
- **Alternativa:** Comentários simples no final da página (estilo GitHub Issues) são suficientes.

### ❌ Capas e Ícones Personalizados
- **Motivo:** Puramente estético. Não agrega valor para empresas.
- **Alternativa:** Ícones padrão (Lucide) + possibilidade de emoji.

### ❌ Lixeira com UI Complexa
- **Motivo:** Soft delete simples (flag no banco) é suficiente.
- **Alternativa:** Admin pode restaurar via SQL se necessário (raro).

### ❌ Publicação na Web (Public Pages)
- **Motivo:** Abre vetores de abuso (SEO spam, DMCA takedowns). Foco é B2B privado.
- **Alternativa:** Exportar HTML estático e hospedar no S3/Vercel do cliente.

### ❌ Notion AI (GPT Integration)
- **Motivo:** Caro (OpenAI API), commoditizado (todos têm). Nosso diferencial é privacidade.
- **Alternativa:** Clientes podem usar ChatGPT em outra aba.

### ❌ Mobile App Nativo (React Native)
- **Motivo:** PWA responsivo atende 90% dos casos. App nativo é manutenção 3x maior.
- **Alternativa:** Otimizar PWA com Service Workers e Add to Home Screen.

---

## 📊 Métricas de Sucesso

### Q1 (Developer Experience)
- **Adoção:** 30% dos usuários ativos usam Mermaid.js
- **Importação:** 100 arquivos `.md` importados/semana
- **Webhooks:** 10 empresas configuraram webhooks

### Q2 (Enterprise Governance)
- **Verificação:** 50% das páginas em contas Team/Enterprise estão verificadas
- **Audit:** 5 empresas solicitaram acesso aos logs
- **SSO:** 20% dos logins via SSO (não senha)

### Q3 (Integrations)
- **Embeds:** 500 iframes ativos (Figma, Loom, etc.)
- **PDFs:** 200 PDFs embedados

### Métricas de Negócio
- **MRR (Monthly Recurring Revenue):** R$ 50k até final de Q2
- **Churn:** <5% ao mês
- **Conversão Free → Pago:** 10%
- **NPS (Net Promoter Score):** >50

---

## 🚦 Governança de Desenvolvimento

### Definition of Ready (DoR)
Uma task só entra em desenvolvimento se:
- [ ] Tem todos os critérios de aceitação definidos
- [ ] Design/mockup aprovado (se aplicável)
- [ ] Dependências técnicas mapeadas
- [ ] Estimativa consensual do time

### Definition of Done (DoD)
Uma task só é considerada completa se:
- [ ] Código implementado e revisado (Code Review)
- [ ] Testes unitários e de integração passando
- [ ] Documentação atualizada (Swagger, README)
- [ ] Deploy em staging e validado por QA
- [ ] Performance testada (não degrada app)
- [ ] Segurança revisada (OWASP checklist)

### Cadência
- **Sprints:** 2 semanas
- **Planning:** Segunda (manhã)
- **Daily Standup:** 9h (15 min)
- **Review/Retro:** Sexta (tarde)

### Priorização (Framework RICE)
- **R**each: Quantos usuários impacta?
- **I**mpact: Quanto melhora a experiência? (0.25 a 3)
- **C**onfidence: Certeza de sucesso? (0% a 100%)
- **E**ffort: Quanto tempo leva? (person-weeks)

**Score RICE = (R × I × C) / E**

Priorizamos tasks com maior score.

---

## 📚 Recursos e Referências

### Bibliotecas e Ferramentas
- [Mermaid.js Docs](https://mermaid.js.org/)
- [CommonMark Spec](https://commonmark.org/)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Tiptap Custom Nodes](https://tiptap.dev/guide/custom-extensions)
- [OWASP Embedding Content](https://cheatsheetseries.owasp.org/cheatsheets/XSS_Prevention_Cheat_Sheet.html)

### Inspirações de Features
- **Diagramas:** [GitBook](https://www.gitbook.com/), [Confluence](https://www.atlassian.com/software/confluence)
- **Webhooks:** [GitHub Webhooks](https://docs.github.com/en/developers/webhooks-and-events/webhooks)
- **Audit Logs:** [AWS CloudTrail](https://aws.amazon.com/cloudtrail/)
- **SSO:** [Slack Enterprise](https://slack.com/help/articles/203772216-SAML-single-sign-on)

---

**Última Atualização:** 31 de Dezembro de 2025
