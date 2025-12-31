# Regras de Negócio - Documentos e Editor

## 1. Estrutura de Documentos

### 1.1. Hierarquia

```
Workspace (Usuário)
├── Página Principal 1
│   ├── Sub-página 1.1
│   ├── Sub-página 1.2
│   └── Sub-página 1.3
├── Página Principal 2
│   ├── Sub-página 2.1
│   │   └── ❌ Não permite sub-sub-páginas
│   └── Sub-página 2.2
└── Página Principal 3
```

**Regras:**
- Máximo 2 níveis de hierarquia (página principal + sub-páginas)
- Não permite sub-sub-páginas (netos)
- Cada sub-página pertence a apenas uma página pai

### 1.2. Model de Documento

```kotlin
@Entity
@Table(name = "documents")
data class Document(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false)
    val title: String,
    
    @Column(columnDefinition = "TEXT")
    val content: String = "",
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    val owner: User,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent: Document? = null,  // null = página principal
    
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL])
    val subPages: MutableList<Document> = mutableListOf(),
    
    @Column(nullable = false)
    val icon: String? = "📄",  // Emoji como ícone
    
    @Column(nullable = false)
    val coverImage: String? = null,  // URL da imagem de capa
    
    @Enumerated(EnumType.STRING)
    val status: DocumentStatus = DocumentStatus.DRAFT,
    
    @Column(nullable = false)
    val isPublic: Boolean = false,
    
    @Column(nullable = false)
    val isFavorite: Boolean = false,
    
    @Column(nullable = false)
    val isArchived: Boolean = false,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column
    val lastEditedBy: UUID? = null,
    
    @Column
    val deletedAt: LocalDateTime? = null
)

enum class DocumentStatus {
    DRAFT,      // Rascunho
    PUBLISHED,  // Publicado
    ARCHIVED    // Arquivado
}
```

---

## 2. CRUD de Documentos

### 2.1. Criar Documento

**Endpoint**: POST /api/documents

**Payload:**
```json
{
  "title": "Minha Nova Página",
  "parentId": null,  // null = página principal
  "icon": "📝",
  "coverImage": null
}
```

**Validações:**
1. Título é obrigatório (mín 1, máx 255 caracteres)
2. Se parentId for fornecido:
   - Página pai deve existir
   - Página pai deve pertencer ao usuário ou ser compartilhada
   - Página pai não pode ser sub-página (max 2 níveis)
3. Validar limites do plano (ver regras de planos)
4. Se criar página principal, verificar limite de páginas principais
5. Se criar sub-página, verificar limite de sub-páginas da página pai

**Resposta Sucesso (HTTP 201):**
```json
{
  "id": "uuid",
  "title": "Minha Nova Página",
  "content": "",
  "parentId": null,
  "icon": "📝",
  "coverImage": null,
  "status": "DRAFT",
  "isPublic": false,
  "isFavorite": false,
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

### 2.2. Listar Documentos

**Endpoint**: GET /api/documents

**Query Params:**
- `parentId` (UUID, opcional) - Listar sub-páginas de uma página
- `status` (DRAFT|PUBLISHED|ARCHIVED, opcional)
- `favorite` (boolean, opcional)
- `archived` (boolean, opcional)
- `page` (number, default: 0)
- `size` (number, default: 20)
- `sort` (string, default: "updatedAt,desc")

**Exemplos:**
```
GET /api/documents
→ Lista todas páginas principais do usuário

GET /api/documents?parentId=uuid
→ Lista sub-páginas da página especificada

GET /api/documents?favorite=true
→ Lista apenas favoritos

GET /api/documents?archived=true
→ Lista apenas arquivados

GET /api/documents?status=PUBLISHED&sort=title,asc
→ Lista publicados ordenados por título
```

**Resposta:**
```json
{
  "content": [
    {
      "id": "uuid",
      "title": "Projeto X",
      "icon": "🚀",
      "status": "PUBLISHED",
      "isFavorite": true,
      "subPagesCount": 5,
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 45,
  "totalPages": 3
}
```

### 2.3. Obter Documento

**Endpoint**: GET /api/documents/:id

**Validações:**
1. Documento existe?
2. Usuário tem permissão de leitura?
   - É o dono?
   - Documento foi compartilhado com ele?
   - Documento é público?

**Resposta:**
```json
{
  "id": "uuid",
  "title": "Minha Página",
  "content": "# Título\n\nConteúdo...",
  "parentId": null,
  "parent": null,
  "subPages": [
    {
      "id": "uuid",
      "title": "Sub-página 1",
      "icon": "📄"
    }
  ],
  "icon": "📝",
  "coverImage": "https://...",
  "status": "PUBLISHED",
  "isPublic": false,
  "isFavorite": false,
  "owner": {
    "id": "uuid",
    "name": "João Silva",
    "username": "joao",
    "avatar": "https://..."
  },
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T12:30:00Z",
  "lastEditedBy": {
    "id": "uuid",
    "name": "Maria Santos",
    "username": "maria"
  }
}
```

### 2.4. Atualizar Documento

**Endpoint**: PATCH /api/documents/:id

**Payload:**
```json
{
  "title": "Novo Título",
  "content": "# Conteúdo atualizado",
  "icon": "🎯",
  "coverImage": "https://...",
  "status": "PUBLISHED"
}
```

**Validações:**
1. Documento existe?
2. Usuário tem permissão de escrita?
3. Se mudar status para PUBLISHED, validar que tem conteúdo

**Comportamento:**
- Atualiza apenas campos fornecidos (merge)
- Atualiza `updatedAt`
- Atualiza `lastEditedBy` com ID do usuário atual
- Se houver colaboradores online, emite evento WebSocket

### 2.5. Deletar Documento

**Endpoint**: DELETE /api/documents/:id

**Comportamento: Soft Delete**
1. Não deleta fisicamente
2. Atualiza `deletedAt` com timestamp atual
3. Queries normais não retornam documentos deletados
4. Mantém por 30 dias para recuperação
5. Job agendado limpa definitivamente após 30 dias

**Validações:**
1. Documento existe?
2. Usuário é o dono?
3. Se deletar página principal, todos sub-páginas são deletadas em cascata

**Resposta:**
```json
{
  "message": "Documento movido para lixeira",
  "deletedAt": "2024-01-01T12:00:00Z",
  "permanentDeletionAt": "2024-01-31T12:00:00Z"
}
```

### 2.6. Restaurar da Lixeira

**Endpoint**: POST /api/documents/:id/restore

**Validações:**
1. Documento está deletado?
2. Usuário é o dono?
3. Ainda está dentro dos 30 dias?
4. Restaurar respeitando limites do plano atual

---

## 3. Editor de Texto

### 3.1. Formato de Conteúdo

O conteúdo é armazenado em **formato JSON** do Tiptap:

```json
{
  "type": "doc",
  "content": [
    {
      "type": "heading",
      "attrs": { "level": 1 },
      "content": [
        { "type": "text", "text": "Título Principal" }
      ]
    },
    {
      "type": "paragraph",
      "content": [
        { "type": "text", "text": "Parágrafo normal com " },
        { 
          "type": "text", 
          "marks": [{ "type": "bold" }],
          "text": "negrito" 
        }
      ]
    }
  ]
}
```

**Vantagens:**
- Estruturado e fácil de parsear
- Fácil conversão para HTML
- Suporta extensões customizadas
- Permite validação de conteúdo

### 3.2. Atalhos Markdown Suportados

**Títulos:**
- `#` + espaço → H1
- `##` + espaço → H2
- `###` + espaço → H3

**Formatação:**
- `*texto*` ou `_texto_` → *itálico*
- `**texto**` ou `__texto__` → **negrito**
- `~~texto~~` → ~~tachado~~
- `` `código` `` → `código inline`

**Listas:**
- `-` + espaço → Lista não ordenada
- `*` + espaço → Lista não ordenada (alternativo)
- `1.` + espaço → Lista ordenada
- `[ ]` + espaço → Checkbox desmarcado
- `[x]` + espaço → Checkbox marcado

**Blocos:**
- ` ``` ` + enter → Bloco de código
- `>` + espaço → Citação (blockquote)
- `---` → Linha horizontal

### 3.3. Comando Slash (/)

Digite `/` para abrir a paleta de comandos:

**Comandos Disponíveis:**

**Básicos:**
- `/heading1` ou `/h1` → Título 1
- `/heading2` ou `/h2` → Título 2
- `/heading3` ou `/h3` → Título 3
- `/paragraph` → Parágrafo normal
- `/bulletlist` → Lista com bullets
- `/numberlist` → Lista numerada
- `/checkbox` → Lista de tarefas

**Blocos Especiais:**
- `/code` → Bloco de código
- `/quote` → Citação

**Callouts:**
- `/info` → Bloco de informação (azul)
- `/warning` → Bloco de aviso (amarelo)
- `/danger` → Bloco de perigo (vermelho)
- `/success` → Bloco de sucesso (verde)

**Mídia:**
- `/image` → Inserir imagem
- `/video` → Inserir vídeo (URL)
- `/divider` → Linha divisória

**Avançado:**
- `/table` → Tabela
- `/page` → Criar sub-página

### 3.4. Callout Blocks

**Tipos de Callouts:**

```typescript
type CalloutType = 'info' | 'warning' | 'danger' | 'success';

interface Callout {
  type: CalloutType;
  title?: string;
  content: string;
}
```

**Renderização:**

**Info (Azul):**
```
┌────────────────────────────────┐
│ ℹ️ Informação                  │
├────────────────────────────────┤
│ Conteúdo informativo aqui...   │
└────────────────────────────────┘
```

**Warning (Amarelo):**
```
┌────────────────────────────────┐
│ ⚠️ Atenção                     │
├────────────────────────────────┤
│ Conteúdo de aviso aqui...      │
└────────────────────────────────┘
```

**Danger (Vermelho):**
```
┌────────────────────────────────┐
│ 🚨 Perigo                      │
├────────────────────────────────┤
│ Conteúdo crítico aqui...       │
└────────────────────────────────┘
```

**Success (Verde):**
```
┌────────────────────────────────┐
│ ✅ Sucesso                     │
├────────────────────────────────┤
│ Mensagem de sucesso aqui...    │
└────────────────────────────────┘
```

**JSON Structure:**
```json
{
  "type": "callout",
  "attrs": {
    "type": "warning",
    "title": "Atenção"
  },
  "content": [
    {
      "type": "paragraph",
      "content": [
        { "type": "text", "text": "Conteúdo do callout..." }
      ]
    }
  ]
}
```

---

## 4. Auto-save

### 4.1. Estratégia de Salvamento

**Debounce de 1 segundo:**
1. Usuário digita no editor
2. Frontend espera 1 segundo sem mudanças
3. Envia PATCH /api/documents/:id com novo conteúdo
4. Backend salva no banco
5. Backend emite evento WebSocket (se colaboração)

**Estados do Auto-save:**
- **Typing...** - Usuário está digitando
- **Saving...** - Enviando para backend
- **Saved** - Salvo com sucesso
- **Error** - Erro ao salvar (mostrar mensagem, tentar novamente)

### 4.2. Indicador Visual

**No header do documento:**
```
[Meu Documento] [✏️ Salvando...]
[Meu Documento] [✅ Salvo às 14:30]
[Meu Documento] [❌ Erro ao salvar - Tentar novamente]
```

### 4.3. Payload de Auto-save

**Endpoint**: PATCH /api/documents/:id/content

**Payload:**
```json
{
  "content": "{...}",  // JSON do Tiptap
  "version": 123        // Número de versão para controle
}
```

**Versionamento:**
- Cada salvamento incrementa a versão
- Previne conflitos em salvamentos simultâneos
- Se versão recebida < versão atual, rejeitar (HTTP 409 Conflict)

### 4.4. Tratamento de Erros

**Erro de Rede:**
- Mostrar mensagem "Sem conexão. Suas alterações serão salvas quando voltar online"
- Armazenar conteúdo no localStorage
- Tentar reenviar a cada 10 segundos
- Quando conexão voltar, salvar e limpar localStorage

**Erro de Permissão:**
- Documento foi descompartilhado
- Mostrar "Você não tem mais permissão para editar este documento"
- Desabilitar editor
- Oferecer opção de salvar cópia

**Erro de Versão (Conflito):**
- Outro usuário editou simultaneamente
- Mostrar diff das mudanças
- Permitir escolher qual versão manter
- Ou mesclar mudanças (merge)

---

## 5. Upload de Imagens

### 5.1. Endpoint de Upload

**Endpoint**: POST /api/documents/:id/images

**Content-Type**: multipart/form-data

**Validações:**
1. Documento existe?
2. Usuário tem permissão de escrita?
3. Tamanho máximo: 5MB
4. Formatos permitidos: jpg, jpeg, png, gif, webp
5. Validar que é realmente uma imagem (magic bytes)

**Fluxo:**
1. Recebe arquivo
2. Valida tipo e tamanho
3. Gera nome único (UUID + extensão)
4. Redimensiona para máximo 1920x1080 (manter proporção)
5. Gera thumbnail 300x300
6. Salva em storage (local ou S3)
7. Salva registro no banco
8. Retorna URLs

**Resposta (HTTP 201):**
```json
{
  "id": "uuid",
  "url": "https://storage.com/images/uuid.jpg",
  "thumbnail": "https://storage.com/images/uuid-thumb.jpg",
  "filename": "minha-imagem.jpg",
  "size": 1024000,
  "width": 1920,
  "height": 1080,
  "uploadedAt": "2024-01-01T12:00:00Z"
}
```

### 5.2. Model de Imagem

```kotlin
@Entity
@Table(name = "document_images")
data class DocumentImage(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: Document,
    
    @Column(nullable = false)
    val url: String,
    
    @Column(nullable = false)
    val thumbnailUrl: String,
    
    @Column(nullable = false)
    val filename: String,
    
    @Column(nullable = false)
    val size: Long,
    
    @Column
    val width: Int?,
    
    @Column
    val height: Int?,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    val uploadedBy: User,
    
    @CreatedDate
    val uploadedAt: LocalDateTime = LocalDateTime.now()
)
```

### 5.3. Deletar Imagem

**Endpoint**: DELETE /api/documents/:docId/images/:imageId

**Comportamento:**
1. Remove do storage
2. Remove registro do banco
3. Se imagem estiver no conteúdo do documento, mantém (broken link)
4. Frontend pode detectar broken links e removê-los

### 5.4. Limites de Storage (Futuro)

**Por Plano:**
- FREE: 100 MB
- PRO: 1 GB
- PREMIUM: 10 GB

---

## 6. Ícones e Capas

### 6.1. Ícone da Página

**Tipos Suportados:**
- **Emoji**: 📝, 🚀, 💡, etc.
- **URL de Imagem**: https://...
- **Iniciais**: Gerar automaticamente das iniciais do título

**Endpoint**: PATCH /api/documents/:id/icon

**Payload:**
```json
{
  "icon": "🎯"
}
```

### 6.2. Imagem de Capa

**Endpoint**: POST /api/documents/:id/cover

**Content-Type**: multipart/form-data

**Validações:**
- Mesmas validações de upload de imagem
- Redimensionar para 1920x400 (banner)

**Endpoint**: DELETE /api/documents/:id/cover

Remove a imagem de capa.

---

## 7. Favoritos

### 7.1. Adicionar aos Favoritos

**Endpoint**: POST /api/documents/:id/favorite

**Comportamento:**
- Atualiza `isFavorite` para `true`
- Adiciona ao topo da lista de favoritos

### 7.2. Remover dos Favoritos

**Endpoint**: DELETE /api/documents/:id/favorite

**Comportamento:**
- Atualiza `isFavorite` para `false`

### 7.3. Listar Favoritos

**Endpoint**: GET /api/documents?favorite=true

Retorna apenas documentos favoritados.

---

## 8. Arquivamento

### 8.1. Arquivar Documento

**Endpoint**: POST /api/documents/:id/archive

**Comportamento:**
- Atualiza `isArchived` para `true`
- Remove da lista principal
- Disponível na seção "Arquivados"

### 8.2. Desarquivar

**Endpoint**: POST /api/documents/:id/unarchive

**Comportamento:**
- Atualiza `isArchived` para `false`
- Retorna à lista principal

### 8.3. Listar Arquivados

**Endpoint**: GET /api/documents?archived=true

---

## 9. Duplicar Documento

### 9.1. Duplicar

**Endpoint**: POST /api/documents/:id/duplicate

**Comportamento:**
1. Cria cópia do documento
2. Título: "[Cópia] Título Original"
3. Copia todo o conteúdo
4. **NÃO copia** sub-páginas (apenas documento raiz)
5. **NÃO copia** compartilhamentos
6. Respeita limites do plano

**Opção de Duplicar com Sub-páginas:**

**Payload:**
```json
{
  "includeSubPages": true
}
```

Se `true`, duplica recursivamente todas sub-páginas.

---

## 10. Busca

### 10.1. Busca Global

**Endpoint**: GET /api/documents/search

**Query Params:**
- `q` (string, obrigatório) - Termo de busca
- `in` (title|content|both, default: both)
- `page`, `size`, `sort`

**Exemplo:**
```
GET /api/documents/search?q=projeto&in=title
```

**Busca em:**
- Título do documento
- Conteúdo do documento (full-text search)
- Tags (futuro)

**Resposta:**
```json
{
  "content": [
    {
      "id": "uuid",
      "title": "Projeto X",
      "excerpt": "...texto com <mark>projeto</mark> destacado...",
      "match": "title",
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "totalElements": 12
}
```

### 10.2. Full-Text Search (PostgreSQL)

**Índice:**
```sql
CREATE INDEX idx_documents_content_search 
ON documents 
USING gin(to_tsvector('portuguese', content));
```

**Query:**
```kotlin
@Query("""
    SELECT d FROM Document d 
    WHERE d.owner = :owner 
    AND (
        to_tsvector('portuguese', d.content) @@ plainto_tsquery('portuguese', :query)
        OR LOWER(d.title) LIKE LOWER(CONCAT('%', :query, '%'))
    )
""")
fun searchDocuments(owner: User, query: String): List<Document>
```

---

## 11. Histórico de Versões (Futuro - Premium)

### 11.1. Snapshot de Versão

```kotlin
@Entity
@Table(name = "document_versions")
data class DocumentVersion(
    @Id
    val id: UUID = UUID.randomUUID(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    val document: Document,
    
    @Column(columnDefinition = "TEXT")
    val content: String,
    
    @Column
    val version: Int,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    val createdBy: User,
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

### 11.2. Estratégia de Versionamento

**Criar snapshot:**
- A cada 10 salvamentos
- A cada hora (se houver mudanças)
- Manual (usuário clica "Salvar versão")

**Manter:**
- Últimas 50 versões
- Versões manuais: sempre

### 11.3. Restaurar Versão

**Endpoint**: POST /api/documents/:id/versions/:versionId/restore

**Comportamento:**
1. Cria novo snapshot da versão atual
2. Restaura conteúdo da versão especificada
3. Incrementa número de versão

---

## 12. Mensagens de Erro

```json
{
  "error": {
    "code": "DOCUMENT_NOT_FOUND",
    "message": "Documento não encontrado"
  }
}
```

**Códigos de Erro:**
- `DOCUMENT_NOT_FOUND` - Documento não existe
- `ACCESS_DENIED` - Sem permissão
- `INVALID_PARENT` - Página pai inválida
- `MAX_DEPTH_EXCEEDED` - Máximo 2 níveis de hierarquia
- `IMAGE_TOO_LARGE` - Imagem excede 5MB
- `INVALID_IMAGE_FORMAT` - Formato não suportado
- `VERSION_CONFLICT` - Conflito de versão (salvamento simultâneo)
- `STORAGE_LIMIT_EXCEEDED` - Limite de storage atingido
