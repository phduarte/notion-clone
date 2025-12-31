# Regras de Negócio - Planos e Permissões

## 1. Tipos de Planos

### 1.1. Plano FREE (Gratuito)

**Recursos:**
- ✅ 1 página principal
- ✅ Até 3 sub-páginas
- ✅ Editor completo com markdown
- ✅ Anexar imagens
- ✅ Blocos de comentários (info, warning, danger)
- ✅ Auto-save
- ❌ Exportar PDF
- ❌ Colaboração em tempo real
- ❌ Compartilhar páginas

**Limitações:**
```kotlin
data class FreePlanLimits(
    val maxMainPages: Int = 1,
    val maxSubPagesPerPage: Int = 3,
    val canExportPdf: Boolean = false,
    val canCollaborate: Boolean = false,
    val canShare: Boolean = false
)
```

### 1.2. Plano PRO (Profissional)

**Recursos:**
- ✅ Até 100 páginas principais
- ✅ Até 10 sub-páginas por página principal
- ✅ Todos os recursos do FREE
- ✅ **Exportar páginas como PDF**
- ❌ Colaboração em tempo real
- ❌ Compartilhar páginas

**Limitações:**
```kotlin
data class ProPlanLimits(
    val maxMainPages: Int = 100,
    val maxSubPagesPerPage: Int = 10,
    val canExportPdf: Boolean = true,
    val canCollaborate: Boolean = false,
    val canShare: Boolean = false
)
```

### 1.3. Plano PREMIUM (Ilimitado)

**Recursos:**
- ✅ Páginas principais ilimitadas
- ✅ Sub-páginas ilimitadas
- ✅ Todos os recursos do PRO
- ✅ **Colaboração em tempo real**
- ✅ **Compartilhar páginas com outros usuários**
- ✅ **Edição simultânea**
- ✅ Histórico de versões (futuro)
- ✅ Suporte prioritário (futuro)

**Limitações:**
```kotlin
data class PremiumPlanLimits(
    val maxMainPages: Int = Int.MAX_VALUE, // Ilimitado
    val maxSubPagesPerPage: Int = Int.MAX_VALUE,
    val canExportPdf: Boolean = true,
    val canCollaborate: Boolean = true,
    val canShare: Boolean = true
)
```

---

## 2. Enum de Planos

```kotlin
enum class PlanType {
    FREE,
    PRO,
    PREMIUM;
    
    fun getLimits(): PlanLimits {
        return when(this) {
            FREE -> FreePlanLimits()
            PRO -> ProPlanLimits()
            PREMIUM -> PremiumPlanLimits()
        }
    }
}
```

---

## 3. Validação de Limites

### 3.1. Criação de Página Principal

**Endpoint**: POST /api/documents

**Validações:**
1. Verificar plano do usuário
2. Contar páginas principais existentes do usuário
3. Comparar com limite do plano
4. Se exceder, retornar erro HTTP 403

**Exemplo:**
```kotlin
fun validateCanCreateMainPage(userId: UUID): Boolean {
    val user = userRepository.findById(userId)
    val currentCount = documentRepository.countMainPagesByUserId(userId)
    val limit = user.plan.getLimits().maxMainPages
    
    if (currentCount >= limit) {
        throw PlanLimitExceededException(
            "Você atingiu o limite de ${limit} páginas principais do plano ${user.plan}. " +
            "Faça upgrade para criar mais páginas."
        )
    }
    return true
}
```

### 3.2. Criação de Sub-página

**Validações:**
1. Verificar plano do usuário
2. Contar sub-páginas da página pai
3. Comparar com limite do plano
4. Se exceder, retornar erro HTTP 403

**Exemplo:**
```kotlin
fun validateCanCreateSubPage(userId: UUID, parentPageId: UUID): Boolean {
    val user = userRepository.findById(userId)
    val currentCount = documentRepository.countSubPagesByParentId(parentPageId)
    val limit = user.plan.getLimits().maxSubPagesPerPage
    
    if (currentCount >= limit) {
        throw PlanLimitExceededException(
            "Você atingiu o limite de ${limit} sub-páginas por página do plano ${user.plan}. " +
            "Faça upgrade para criar mais sub-páginas."
        )
    }
    return true
}
```

### 3.3. Exportar PDF

**Endpoint**: GET /api/documents/:id/export/pdf

**Validações:**
1. Verificar plano do usuário
2. Se FREE, negar acesso
3. Se PRO ou PREMIUM, permitir

```kotlin
@PreAuthorize("hasAuthority('EXPORT_PDF')")
fun exportToPdf(documentId: UUID): ByteArray {
    // Gera PDF
}
```

**Authorities por Plano:**
```kotlin
fun getAuthoritiesByPlan(plan: PlanType): List<String> {
    return when(plan) {
        FREE -> listOf("READ", "WRITE")
        PRO -> listOf("READ", "WRITE", "EXPORT_PDF")
        PREMIUM -> listOf("READ", "WRITE", "EXPORT_PDF", "COLLABORATE", "SHARE")
    }
}
```

### 3.4. Compartilhar Página

**Endpoint**: POST /api/documents/:id/share

**Validações:**
1. Verificar se plano é PREMIUM
2. Se não for, retornar erro HTTP 403

```kotlin
@PreAuthorize("hasAuthority('SHARE')")
fun shareDocument(documentId: UUID, shareWith: List<String>): ShareResponse {
    // Compartilha documento
}
```

### 3.5. Colaboração em Tempo Real

**WebSocket Connect:**

**Validações:**
1. Ao conectar no WebSocket de um documento
2. Verificar plano do DONO do documento
3. Se não for PREMIUM, rejeitar conexão

```kotlin
@MessageMapping("/document/{id}/join")
fun joinDocument(@DestinationVariable id: UUID, principal: Principal) {
    val document = documentRepository.findById(id)
    val owner = userRepository.findById(document.ownerId)
    
    if (owner.plan != PlanType.PREMIUM) {
        throw AccessDeniedException(
            "Colaboração em tempo real requer plano Premium"
        )
    }
    
    // Permite entrar na sessão
}
```

---

## 4. Upgrade e Downgrade de Plano

### 4.1. Upgrade de Plano

**Endpoint**: POST /api/users/me/upgrade

**Payload:**
```json
{
  "newPlan": "PRO",
  "paymentMethod": "credit_card",
  "paymentDetails": {}
}
```

**Fluxo:**
1. Validar novo plano (deve ser superior ao atual)
2. Processar pagamento (integração futura)
3. Atualizar plano do usuário
4. Conceder novas permissões imediatamente
5. Enviar email de confirmação
6. Retornar sucesso

**Regras:**
- FREE → PRO: Permitido
- FREE → PREMIUM: Permitido
- PRO → PREMIUM: Permitido
- Upgrade é imediato após pagamento

### 4.2. Downgrade de Plano

**Endpoint**: POST /api/users/me/downgrade

**Payload:**
```json
{
  "newPlan": "FREE",
  "reason": "TOO_EXPENSIVE"
}
```

**Fluxo:**
1. Validar novo plano (deve ser inferior ao atual)
2. **Verificar se há conteúdo que excede limites**
3. Se exceder, exigir que usuário delete/arquive conteúdo antes
4. Cancelar assinatura
5. Agendar downgrade para fim do período pago
6. Enviar email de confirmação

**Validações de Conteúdo:**

```kotlin
fun validateCanDowngrade(userId: UUID, newPlan: PlanType): DowngradeValidation {
    val user = userRepository.findById(userId)
    val newLimits = newPlan.getLimits()
    
    val mainPagesCount = documentRepository.countMainPagesByUserId(userId)
    val subPagesExceeded = documentRepository.findPagesExceedingSubPageLimit(
        userId, 
        newLimits.maxSubPagesPerPage
    )
    
    val validation = DowngradeValidation(
        canDowngrade = true,
        issues = mutableListOf()
    )
    
    // Verifica limite de páginas principais
    if (mainPagesCount > newLimits.maxMainPages) {
        validation.canDowngrade = false
        validation.issues.add(
            "Você tem ${mainPagesCount} páginas principais, " +
            "mas o plano ${newPlan} permite apenas ${newLimits.maxMainPages}. " +
            "Delete ${mainPagesCount - newLimits.maxMainPages} páginas antes de fazer downgrade."
        )
    }
    
    // Verifica sub-páginas
    if (subPagesExceeded.isNotEmpty()) {
        validation.canDowngrade = false
        validation.issues.add(
            "As seguintes páginas excedem o limite de sub-páginas: " +
            subPagesExceeded.joinToString { it.title }
        )
    }
    
    return validation
}
```

**Resposta se Não Puder Fazer Downgrade:**

```json
{
  "canDowngrade": false,
  "issues": [
    "Você tem 5 páginas principais, mas o plano FREE permite apenas 1. Delete 4 páginas antes de fazer downgrade.",
    "A página 'Projeto X' tem 8 sub-páginas, mas o plano FREE permite apenas 3."
  ]
}
```

### 4.3. Comportamento Após Downgrade

**Se downgrade para FREE:**
1. Desabilitar exportação de PDF
2. Desabilitar compartilhamento (manter compartilhamentos existentes como read-only)
3. Desabilitar colaboração em tempo real
4. Manter conteúdo existente (não deletar)
5. Impedir criação de novo conteúdo além dos limites

---

## 5. Mensagens de Limite Atingido

### 5.1. No Frontend

Quando usuário tenta criar página e atingiu limite:

**Modal:**
```
Limite do Plano Atingido 🚫

Você atingiu o limite de 1 página principal do plano FREE.

Para criar mais páginas, faça upgrade para:

📦 Plano PRO - R$ 29,90/mês
- Até 100 páginas principais
- Até 10 sub-páginas por página
- Exportar PDF

💎 Plano PREMIUM - R$ 59,90/mês
- Páginas ilimitadas
- Sub-páginas ilimitadas
- Exportar PDF
- Colaboração em tempo real

[Ver Comparação de Planos]  [Fazer Upgrade]
```

### 5.2. Banner de Aviso

Quando usuário está próximo do limite (80%):

**Banner:**
```
⚠️ Você está usando 4 de 5 páginas principais do seu plano PRO.
[Fazer Upgrade para Premium]
```

### 5.3. Página de Comparação

Exibir tabela comparativa de planos:

| Recurso | FREE | PRO | PREMIUM |
|---------|------|-----|---------|
| Páginas principais | 1 | 100 | Ilimitadas |
| Sub-páginas por página | 3 | 10 | Ilimitadas |
| Editor completo | ✅ | ✅ | ✅ |
| Imagens | ✅ | ✅ | ✅ |
| Auto-save | ✅ | ✅ | ✅ |
| Exportar PDF | ❌ | ✅ | ✅ |
| Compartilhar | ❌ | ❌ | ✅ |
| Colaboração em tempo real | ❌ | ❌ | ✅ |
| **Preço** | **Grátis** | **R$ 29,90/mês** | **R$ 59,90/mês** |

---

## 6. Permissões Detalhadas

### 6.1. Matriz de Permissões

```kotlin
enum class Permission {
    // Documentos
    CREATE_DOCUMENT,
    READ_DOCUMENT,
    UPDATE_DOCUMENT,
    DELETE_DOCUMENT,
    
    // Sub-páginas
    CREATE_SUB_PAGE,
    
    // Exportação
    EXPORT_PDF,
    
    // Compartilhamento
    SHARE_DOCUMENT,
    VIEW_SHARED_DOCUMENT,
    EDIT_SHARED_DOCUMENT,
    
    // Colaboração
    COLLABORATE_REALTIME,
    
    // Admin (futuro)
    MANAGE_USERS,
    VIEW_ANALYTICS
}

fun getPermissionsByPlan(plan: PlanType): Set<Permission> {
    val base = setOf(
        Permission.CREATE_DOCUMENT,
        Permission.READ_DOCUMENT,
        Permission.UPDATE_DOCUMENT,
        Permission.DELETE_DOCUMENT,
        Permission.CREATE_SUB_PAGE
    )
    
    return when(plan) {
        FREE -> base
        PRO -> base + setOf(Permission.EXPORT_PDF)
        PREMIUM -> base + setOf(
            Permission.EXPORT_PDF,
            Permission.SHARE_DOCUMENT,
            Permission.VIEW_SHARED_DOCUMENT,
            Permission.EDIT_SHARED_DOCUMENT,
            Permission.COLLABORATE_REALTIME
        )
    }
}
```

### 6.2. Verificação de Permissão

**Annotation Customizada:**
```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresPlan(val minPlan: PlanType)

// Uso:
@RequiresPlan(PlanType.PRO)
fun exportToPdf(documentId: UUID): ByteArray {
    // ...
}
```

**Aspect para Validação:**
```kotlin
@Aspect
class PlanPermissionAspect {
    
    @Before("@annotation(requiresPlan)")
    fun checkPlanPermission(joinPoint: JoinPoint, requiresPlan: RequiresPlan) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        
        if (user.plan < requiresPlan.minPlan) {
            throw InsufficientPlanException(
                "Este recurso requer plano ${requiresPlan.minPlan} ou superior"
            )
        }
    }
}
```

---

## 7. Estatísticas e Uso

### 7.1. Endpoint de Estatísticas

**Endpoint**: GET /api/users/me/usage

**Resposta:**
```json
{
  "plan": "PRO",
  "usage": {
    "mainPages": {
      "current": 45,
      "limit": 100,
      "percentage": 45
    },
    "subPages": {
      "highest": 8,
      "limit": 10,
      "percentage": 80,
      "pageWithMostSubPages": "Projeto X"
    },
    "storage": {
      "used": "125 MB",
      "limit": "1 GB",
      "percentage": 12.5
    }
  },
  "recommendations": [
    "Você está usando 80% do limite de sub-páginas na página 'Projeto X'",
    "Considere fazer upgrade para Premium para páginas ilimitadas"
  ]
}
```

### 7.2. Notificações de Uso

**Enviar email quando:**
- Usuário atinge 80% de qualquer limite
- Usuário atinge 100% de qualquer limite
- Downgrade está agendado (7 dias antes)

---

## 8. Trial Premium

### 8.1. Período de Teste

**Oferecer 14 dias de trial Premium:**
- Disponível apenas para usuários FREE ou PRO
- Apenas 1 trial por usuário (lifetime)
- Acesso total aos recursos Premium
- Não requer cartão de crédito
- Após expirar, retorna ao plano anterior

**Endpoint**: POST /api/users/me/start-trial

**Validações:**
1. Usuário nunca usou trial antes?
2. Plano atual é FREE ou PRO?
3. Criar registro de trial com data de expiração

```kotlin
data class Trial(
    val userId: UUID,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val originalPlan: PlanType,
    val status: TrialStatus  // ACTIVE, EXPIRED, CONVERTED
)
```

### 8.2. Expiração do Trial

**Job agendado diário:**
1. Buscar trials com endDate < hoje
2. Reverter usuários para plano original
3. Enviar email "Seu trial expirou"
4. Oferecer desconto para converter

---

## 9. Códigos de Desconto

### 9.1. Cupons Promocionais

```kotlin
data class DiscountCoupon(
    val code: String,              // "BEMVINDO2024"
    val discountType: DiscountType, // PERCENTAGE, FIXED_AMOUNT
    val discountValue: BigDecimal,  // 20 (20% ou R$ 20)
    val validFrom: LocalDateTime,
    val validUntil: LocalDateTime,
    val maxUses: Int?,              // null = ilimitado
    val currentUses: Int,
    val applicablePlans: List<PlanType>,
    val firstPurchaseOnly: Boolean
)

enum class DiscountType {
    PERCENTAGE,      // 20% off
    FIXED_AMOUNT,    // R$ 10 off
    FIRST_MONTH_FREE // Primeiro mês grátis
}
```

### 9.2. Aplicar Cupom

**Endpoint**: POST /api/users/me/apply-coupon

**Payload:**
```json
{
  "code": "BEMVINDO2024",
  "plan": "PRO"
}
```

**Validações:**
1. Cupom existe?
2. Está dentro da validade?
3. Não excedeu máximo de usos?
4. Aplicável ao plano escolhido?
5. Se firstPurchaseOnly, usuário nunca pagou antes?

---

## 10. Mensagens de Erro de Plano

```json
{
  "error": {
    "code": "PLAN_LIMIT_EXCEEDED",
    "message": "Você atingiu o limite de páginas principais do plano FREE",
    "details": {
      "currentPlan": "FREE",
      "limit": 1,
      "current": 1,
      "requiredPlan": "PRO"
    },
    "actions": [
      {
        "label": "Fazer Upgrade",
        "url": "/pricing"
      },
      {
        "label": "Ver Comparação",
        "url": "/plans/compare"
      }
    ]
  }
}
```

**Códigos de Erro:**
- `PLAN_LIMIT_EXCEEDED` - Limite atingido
- `INSUFFICIENT_PLAN` - Plano insuficiente para recurso
- `TRIAL_ALREADY_USED` - Trial já utilizado
- `INVALID_COUPON` - Cupom inválido
- `CANNOT_DOWNGRADE` - Não pode fazer downgrade
