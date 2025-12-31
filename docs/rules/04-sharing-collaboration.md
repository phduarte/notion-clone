# Regras de Negócio - Compartilhamento e Colaboração

## 1. Compartilhamento de Documentos (Premium)

### 1.1. Visão Geral

**Requisito:** Plano Premium

**Funcionalidades:**
- Compartilhar documentos com outros usuários
- Definir permissões (visualizar ou editar)
- Gerenciar acessos
- Link público (opcional)

### 1.2. Model de Compartilhamento

```kotlin
@Entity
@Table(name = "document_shares")
data class DocumentShare(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_by", nullable = false)
    val sharedBy: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with", nullable = false)
    val sharedWith: User,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val permission: SharePermission,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column
    val expiresAt: LocalDateTime? = null,
    
    @Column(nullable = false)
    val isActive: Boolean = true
)

enum class SharePermission {
    VIEW,   // Apenas visualizar
    EDIT    // Visualizar e editar
}
```

### 1.3. Compartilhar Documento

**Endpoint**: POST /api/documents/:id/share

**Payload:**
```json
{
  "shareWith": [
    {
      "userIdentifier": "joao@example.com",  // Email ou username
      "permission": "EDIT"
    },
    {
      "userIdentifier": "maria",
      "permission": "VIEW"
    }
  ],
  "message": "Confira este documento!"  // Opcional
}
```

**Validações:**
1. Documento existe?
2. Usuário é o dono?
3. Plano do dono é Premium?
4. Usuários a compartilhar existem?
5. Não compartilhar com si mesmo
6. Não duplicar compartilhamentos

**Resposta (HTTP 200):**
```json
{
  "shares": [
    {
      "id": "uuid",
      "user": {
        "id": "uuid",
        "name": "João Silva",
        "username": "joao",
        "email": "joao@example.com",
        "avatar": "https://..."
      },
      "permission": "EDIT",
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ],
  "notFound": ["usuario_inexistente@email.com"]
}
```

**Enviar Email de Notificação:**
```
Assunto: [Nome do Dono] compartilhou um documento com você

Olá [Nome],

[Nome do Dono] compartilhou o documento "[Título]" com você.

[Visualizar Documento]

Mensagem: "Confira este documento!"

Atenciosamente,
Notion Clone
```

### 1.4. Listar Compartilhamentos

**Endpoint**: GET /api/documents/:id/shares

**Resposta:**
```json
{
  "shares": [
    {
      "id": "uuid",
      "user": {
        "id": "uuid",
        "name": "João Silva",
        "username": "joao",
        "avatar": "https://..."
      },
      "permission": "EDIT",
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ]
}
```

### 1.5. Atualizar Permissão

**Endpoint**: PATCH /api/documents/:docId/shares/:shareId

**Payload:**
```json
{
  "permission": "VIEW"
}
```

**Validações:**
1. Usuário é o dono do documento?
2. Compartilhamento existe?

### 1.6. Revogar Acesso

**Endpoint**: DELETE /api/documents/:docId/shares/:shareId

**Validações:**
1. Usuário é o dono do documento?
2. Compartilhamento existe?

**Comportamento:**
- Atualiza `isActive` para `false`
- Emite evento WebSocket se usuário estiver online
- Frontend fecha documento automaticamente

**Enviar Email:**
```
Assunto: Acesso revogado ao documento "[Título]"

Olá [Nome],

[Nome do Dono] revogou seu acesso ao documento "[Título]".

Se tiver dúvidas, entre em contato com o dono do documento.

Atenciosamente,
Notion Clone
```

### 1.7. Documentos Compartilhados Comigo

**Endpoint**: GET /api/documents/shared-with-me

**Query Params:**
- `permission` (VIEW|EDIT, opcional)
- `page`, `size`, `sort`

**Resposta:**
```json
{
  "content": [
    {
      "id": "uuid",
      "title": "Documento Compartilhado",
      "icon": "📄",
      "owner": {
        "name": "João Silva",
        "username": "joao",
        "avatar": "https://..."
      },
      "permission": "EDIT",
      "sharedAt": "2024-01-01T12:00:00Z",
      "updatedAt": "2024-01-02T15:30:00Z"
    }
  ],
  "totalElements": 5
}
```

---

## 2. Link Público (Premium)

### 2.1. Gerar Link Público

**Endpoint**: POST /api/documents/:id/public-link

**Payload:**
```json
{
  "permission": "VIEW",  // VIEW ou EDIT
  "expiresAt": "2024-12-31T23:59:59Z",  // Opcional
  "password": "senha123"  // Opcional
}
```

**Comportamento:**
1. Gera token único e seguro (UUID ou hash)
2. Link: `https://notion-clone.com/public/[token]`
3. Salva no banco com configurações

**Model:**
```kotlin
@Entity
@Table(name = "public_links")
data class PublicLink(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @Column(unique = true, nullable = false)
    val token: String = generateSecureToken(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val permission: SharePermission,
    
    @Column
    val password: String? = null,  // Hashed
    
    @Column
    val expiresAt: LocalDateTime? = null,
    
    @Column(nullable = false)
    val isActive: Boolean = true,
    
    @Column(nullable = false)
    val viewCount: Int = 0,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

**Resposta:**
```json
{
  "id": "uuid",
  "url": "https://notion-clone.com/public/secure-token-123",
  "permission": "VIEW",
  "expiresAt": "2024-12-31T23:59:59Z",
  "hasPassword": true,
  "createdAt": "2024-01-01T12:00:00Z"
}
```

### 2.2. Acessar Link Público

**Endpoint**: GET /public/:token

**Validações:**
1. Token existe?
2. Link está ativo?
3. Link não expirou?
4. Se tem senha, validar senha

**Fluxo com Senha:**
```
1. Usuário acessa /public/token
2. Se hasPassword = true, mostrar modal de senha
3. POST /public/:token/verify-password { password: "..." }
4. Se correto, gerar session token temporário
5. Redirecionar para visualização do documento
```

**Resposta (sem senha):**
- Retorna documento completo em modo visualização
- Desabilita edição se permission = VIEW
- Incrementa viewCount

**Resposta (com senha - sem autenticar):**
```json
{
  "requiresPassword": true,
  "documentTitle": "Documento Público",
  "owner": "João Silva"
}
```

### 2.3. Listar Links Públicos

**Endpoint**: GET /api/documents/:id/public-links

**Resposta:**
```json
{
  "links": [
    {
      "id": "uuid",
      "url": "https://notion-clone.com/public/token",
      "permission": "VIEW",
      "hasPassword": true,
      "expiresAt": "2024-12-31T23:59:59Z",
      "viewCount": 42,
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ]
}
```

### 2.4. Desabilitar Link Público

**Endpoint**: DELETE /api/documents/:docId/public-links/:linkId

**Comportamento:**
- Atualiza `isActive` para `false`
- Link para de funcionar imediatamente

---

## 3. Colaboração em Tempo Real (Premium)

### 3.1. Visão Geral

**Requisito:** Plano Premium do DONO do documento

**Funcionalidades:**
- Edição simultânea
- Ver cursores de outros usuários
- Ver quem está online
- Sincronização em tempo real
- Presença de usuários

### 3.2. Tecnologia

**Protocolo:** WebSocket (STOMP sobre WebSocket)

**Endpoints WebSocket:**
```
/ws/connect           → Conectar ao WebSocket
/topic/document/{id}  → Subscrever ao documento
/app/document/{id}    → Enviar mudanças
```

### 3.3. Conectar ao Documento

**Cliente (Frontend):**
```typescript
const socket = new SockJS('http://localhost:8080/ws/connect');
const stompClient = Stomp.over(socket);

stompClient.connect({ 
  Authorization: `Bearer ${accessToken}` 
}, () => {
  // Subscrever ao tópico do documento
  stompClient.subscribe(`/topic/document/${docId}`, (message) => {
    const event = JSON.parse(message.body);
    handleCollaborationEvent(event);
  });
  
  // Anunciar presença
  stompClient.send(`/app/document/${docId}/join`, {}, JSON.stringify({
    userId: currentUser.id,
    username: currentUser.username,
    avatar: currentUser.avatar
  }));
});
```

**Servidor (Backend):**
```kotlin
@MessageMapping("/document/{id}/join")
@SendTo("/topic/document/{id}")
fun joinDocument(
    @DestinationVariable id: UUID,
    @Payload joinMessage: JoinMessage,
    principal: Principal
): CollaborationEvent {
    // Validar permissões
    validateCanCollaborate(id, principal)
    
    // Adicionar à lista de presença
    presenceService.addUser(id, joinMessage.userId)
    
    // Broadcast para todos
    return CollaborationEvent(
        type = EventType.USER_JOINED,
        documentId = id,
        user = joinMessage,
        timestamp = Instant.now()
    )
}
```

### 3.4. Tipos de Eventos

```kotlin
enum class CollaborationEventType {
    USER_JOINED,      // Usuário entrou
    USER_LEFT,        // Usuário saiu
    CONTENT_CHANGED,  // Conteúdo mudou
    CURSOR_MOVED,     // Cursor de usuário moveu
    SELECTION_CHANGED // Seleção de texto mudou
}

data class CollaborationEvent(
    val type: CollaborationEventType,
    val documentId: UUID,
    val user: UserPresence,
    val data: Any? = null,
    val timestamp: Instant
)

data class UserPresence(
    val userId: UUID,
    val username: String,
    val avatar: String?,
    val color: String  // Cor única para o usuário
)
```

### 3.5. Sincronização de Conteúdo

**Tiptap Collaboration Extension:**

Usa **Y.js** para CRDTs (Conflict-free Replicated Data Types):

```typescript
import { TiptapCollabProvider } from '@hocuspocus/provider'

const provider = new TiptapCollabProvider({
  name: documentId,
  appId: 'notion-clone',
  token: accessToken,
  websocketProvider: stompClient
})

const editor = new Editor({
  extensions: [
    StarterKit,
    Collaboration.configure({
      document: provider.document,
    }),
    CollaborationCursor.configure({
      provider: provider,
      user: {
        name: currentUser.username,
        color: getUserColor(currentUser.id)
      }
    })
  ]
})
```

**Operational Transformation:**
1. Usuário A digita "Hello"
2. Frontend gera operação: `insert("Hello", position: 0)`
3. Envia via WebSocket
4. Backend recebe e valida
5. Backend aplica transformação (Y.js)
6. Backend broadcast para todos os clientes
7. Clientes aplicam operação localmente

**Tratamento de Conflitos:**
- Y.js gerencia automaticamente conflitos
- CRDTs garantem convergência eventual
- Todos usuários chegam ao mesmo estado final

### 3.6. Presença de Usuários

**Endpoint**: GET /api/documents/:id/presence

**Resposta:**
```json
{
  "users": [
    {
      "userId": "uuid",
      "username": "joao",
      "avatar": "https://...",
      "color": "#FF5733",
      "cursor": {
        "position": 123,
        "timestamp": "2024-01-01T12:30:45Z"
      }
    },
    {
      "userId": "uuid2",
      "username": "maria",
      "avatar": "https://...",
      "color": "#33FF57",
      "cursor": {
        "position": 456,
        "timestamp": "2024-01-01T12:30:50Z"
      }
    }
  ],
  "count": 2
}
```

**Atualização em Tempo Real:**
- WebSocket envia eventos de presença
- Frontend atualiza UI mostrando avatares dos usuários online
- Mostra cursores de outros usuários no editor

**UI de Presença:**
```
┌─────────────────────────────────────────┐
│ Meu Documento                  👤 👤    │  ← Avatares de quem está online
├─────────────────────────────────────────┤
│ # Título                                │
│                                         │
│ Parágrafo de texto...█                  │  ← Cursor do João
│                      [João]             │
│                                         │
│ Mais texto aqui...      █               │  ← Cursor da Maria
│                        [Maria]          │
└─────────────────────────────────────────┘
```

### 3.7. Desconexão

**Detectar Desconexão:**
- Cliente envia heartbeat a cada 30 segundos
- Se não receber heartbeat por 1 minuto, considerar desconectado
- Remover da lista de presença
- Broadcast evento USER_LEFT

**Reconexão:**
- Cliente tenta reconectar automaticamente
- Sincroniza estado atual do documento
- Reanuncia presença

### 3.8. Limitações

**Máximo de Usuários Simultâneos:**
- FREE: 0 (não suporta colaboração)
- PRO: 0 (não suporta colaboração)
- PREMIUM: 10 usuários simultâneos por documento

**Se exceder:**
- Novos usuários entram em "modo visualização apenas"
- Mostrar mensagem: "Limite de colaboradores atingido. Você está em modo visualização."

---

## 4. Comentários (Futuro)

### 4.1. Adicionar Comentário

**Endpoint**: POST /api/documents/:id/comments

**Payload:**
```json
{
  "content": "Ótimo ponto!",
  "position": {
    "blockId": "uuid",
    "start": 10,
    "end": 20
  }
}
```

**Model:**
```kotlin
@Entity
@Table(name = "document_comments")
data class DocumentComment(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,
    
    @Column
    val blockId: UUID?,  // Bloco onde foi comentado
    
    @Column
    val startPosition: Int?,
    
    @Column
    val endPosition: Int?,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent: DocumentComment? = null,  // Para respostas
    
    @Column(nullable = false)
    val isResolved: Boolean = false,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
```

### 4.2. Listar Comentários

**Endpoint**: GET /api/documents/:id/comments

**Query Params:**
- `resolved` (boolean, opcional)
- `blockId` (UUID, opcional) - Comentários de um bloco específico

### 4.3. Resolver Comentário

**Endpoint**: PATCH /api/documents/:docId/comments/:commentId/resolve

**Comportamento:**
- Atualiza `isResolved` para `true`
- Envia notificação para autor original

---

## 5. Notificações

### 5.1. Tipos de Notificações

```kotlin
enum class NotificationType {
    DOCUMENT_SHARED,      // Documento compartilhado com você
    SHARE_PERMISSION_CHANGED,  // Sua permissão mudou
    ACCESS_REVOKED,       // Seu acesso foi revogado
    COMMENT_ADDED,        // Alguém comentou
    COMMENT_REPLIED,      // Alguém respondeu seu comentário
    COMMENT_RESOLVED,     // Comentário foi resolvido
    MENTION,              // Você foi mencionado (@usuario)
    DOCUMENT_UPDATED      // Documento compartilhado foi atualizado
}
```

### 5.2. Model de Notificação

```kotlin
@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: NotificationType,
    
    @Column(nullable = false)
    val title: String,
    
    @Column(columnDefinition = "TEXT")
    val message: String,
    
    @Column
    val relatedDocumentId: UUID?,
    
    @Column
    val relatedUserId: UUID?,
    
    @Column(nullable = false)
    val isRead: Boolean = false,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

### 5.3. Listar Notificações

**Endpoint**: GET /api/notifications

**Query Params:**
- `read` (boolean, opcional)
- `type` (NotificationType, opcional)
- `page`, `size`

**Resposta:**
```json
{
  "content": [
    {
      "id": "uuid",
      "type": "DOCUMENT_SHARED",
      "title": "Novo documento compartilhado",
      "message": "João Silva compartilhou 'Projeto X' com você",
      "relatedDocument": {
        "id": "uuid",
        "title": "Projeto X"
      },
      "relatedUser": {
        "id": "uuid",
        "name": "João Silva",
        "username": "joao"
      },
      "isRead": false,
      "createdAt": "2024-01-01T12:00:00Z"
    }
  ],
  "unreadCount": 5,
  "totalElements": 42
}
```

### 5.4. Marcar como Lida

**Endpoint**: PATCH /api/notifications/:id/read

**Endpoint**: PATCH /api/notifications/read-all

### 5.5. WebSocket para Notificações em Tempo Real

**Cliente subscreve:**
```
/user/queue/notifications
```

**Servidor envia:**
```json
{
  "type": "NEW_NOTIFICATION",
  "notification": {
    "id": "uuid",
    "type": "DOCUMENT_SHARED",
    "title": "...",
    "message": "..."
  }
}
```

**Frontend:**
- Mostra badge com contador de não lidas
- Toast notification
- Som (opcional, nas preferências)

---

## 6. Auditoria de Acesso

### 6.1. Log de Acessos

```kotlin
@Entity
@Table(name = "document_access_log")
data class DocumentAccessLog(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User?,  // null se acesso público
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val action: AccessAction,
    
    @Column
    val ipAddress: String,
    
    @Column
    val userAgent: String,
    
    @CreatedDate
    val timestamp: LocalDateTime = LocalDateTime.now()
)

enum class AccessAction {
    VIEW,
    EDIT,
    SHARE,
    DOWNLOAD_PDF,
    PUBLIC_LINK_ACCESS
}
```

### 6.2. Endpoint de Auditoria

**Endpoint**: GET /api/documents/:id/access-log

**Resposta:**
```json
{
  "content": [
    {
      "user": {
        "name": "João Silva",
        "username": "joao"
      },
      "action": "EDIT",
      "timestamp": "2024-01-01T12:30:00Z",
      "ipAddress": "192.168.1.1"
    },
    {
      "user": null,
      "action": "PUBLIC_LINK_ACCESS",
      "timestamp": "2024-01-01T13:00:00Z",
      "ipAddress": "203.0.113.0"
    }
  ]
}
```

---

## 7. Mensagens de Erro

```json
{
  "error": {
    "code": "COLLABORATION_NOT_AVAILABLE",
    "message": "Colaboração em tempo real requer plano Premium",
    "details": {
      "currentPlan": "PRO",
      "requiredPlan": "PREMIUM"
    }
  }
}
```

**Códigos de Erro:**
- `COLLABORATION_NOT_AVAILABLE` - Plano insuficiente
- `MAX_COLLABORATORS_REACHED` - Limite de colaboradores atingido
- `SHARE_NOT_ALLOWED` - Não pode compartilhar (plano insuficiente)
- `INVALID_PERMISSION` - Permissão inválida
- `USER_NOT_FOUND` - Usuário para compartilhar não existe
- `ALREADY_SHARED` - Já compartilhado com este usuário
- `LINK_EXPIRED` - Link público expirado
- `INVALID_PASSWORD` - Senha do link público incorreta
