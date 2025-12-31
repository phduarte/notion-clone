# 🔄 Atualização de Dependências - Dezembro 2025

## ⚠️ Pré-requisitos Críticos

### Node.js
**Versão atual detectada: 14.15.1 (EOL - End of Life)**

⚠️ **AÇÃO NECESSÁRIA**: Atualizar Node.js para uma versão suportada:
- **Recomendado**: Node.js 20.x LTS (Long Term Support)
- **Mínimo**: Node.js 18.18.0
- **Download**: https://nodejs.org/

**Por que atualizar?**
- Node.js 14 chegou ao fim do suporte em abril de 2023
- Vulnerabilidades de segurança não são mais corrigidas
- Pacotes modernos não suportam mais Node 14
- Next.js 14+ requer Node >= 18.17.0
- React Hook Form 7.54+ requer Node >= 18.0.0

### Instalação Node.js 20 LTS
```powershell
# Windows (via Chocolatey)
choco install nodejs-lts

# Ou baixe o instalador em: https://nodejs.org/en/download/
```

## 📦 Dependências Frontend Atualizadas

### package.json (Atualizado)

```json
{
  "dependencies": {
    "next": "^14.2.21",              // Atualizado de 14.2.0
    "react": "^18.3.1",               // Atualizado de 18.3.0
    "react-dom": "^18.3.1",           // Atualizado de 18.3.0
    "@tanstack/react-query": "^5.62.7", // Atualizado de 5.0.0
    "axios": "^1.7.9",                // Atualizado de 1.6.0
    "zustand": "^5.0.2",              // Atualizado de 4.5.0 (Breaking change)
    "react-hook-form": "^7.54.2",     // Atualizado de 7.50.0
    "@hookform/resolvers": "^3.9.1",  // Atualizado de 3.3.0
    "zod": "^3.24.1",                 // Atualizado de 3.22.0
    "@tiptap/react": "^2.10.4",       // Atualizado de 2.2.0
    "@tiptap/starter-kit": "^2.10.4", // Atualizado de 2.2.0
    "@tiptap/extension-placeholder": "^2.10.4", // Atualizado de 2.2.0
    "@tiptap/extension-image": "^2.10.4",       // Atualizado de 2.2.0
    "@tiptap/extension-link": "^2.10.4",        // Atualizado de 2.2.0
    "class-variance-authority": "^0.7.1",       // Atualizado de 0.7.0
    "clsx": "^2.1.1",                 // Atualizado de 2.1.0
    "tailwind-merge": "^2.6.0",       // Atualizado de 2.2.0
    "lucide-react": "^0.468.0",       // Atualizado de 0.344.0
    "sonner": "^1.7.1"                // Atualizado de 1.4.0
  },
  "devDependencies": {
    "typescript": "^5.7.2",           // Atualizado de 5.3.0
    "@types/node": "^20.17.10",       // Atualizado de 20.11.0
    "@types/react": "^18.3.18",       // Atualizado de 18.3.0
    "@types/react-dom": "^18.3.5",    // Atualizado de 18.3.0
    "autoprefixer": "^10.4.20",       // Atualizado de 10.4.0
    "postcss": "^8.4.49",             // Atualizado de 8.4.0
    "tailwindcss": "^3.4.17",         // Atualizado de 3.4.0
    "eslint": "^8.57.1",              // Atualizado de 8.56.0
    "eslint-config-next": "^14.2.21", // Atualizado de 14.2.0
    "prettier": "^3.4.2",             // Atualizado de 3.2.0
    "@typescript-eslint/eslint-plugin": "^7.18.0", // Atualizado de 7.0.0
    "@typescript-eslint/parser": "^7.18.0"         // Atualizado de 7.0.0
  }
}
```

### ⚠️ Breaking Changes - Zustand 5.0

Zustand 4.5 → 5.0 tem algumas mudanças:
- `create` agora é exportado diretamente
- Middleware foi reorganizado

**Antes (v4):**
```typescript
import create from 'zustand'
```

**Depois (v5):**
```typescript
import { create } from 'zustand'
```

## 🔧 Dependências Backend Atualizadas

### build.gradle.kts (Atualizado)

```kotlin
plugins {
    id("org.springframework.boot") version "3.4.1"    // Atualizado de 3.2.1
    id("io.spring.dependency-management") version "1.1.7" // Atualizado de 1.1.4
    kotlin("jvm") version "2.1.0"                     // Atualizado de 1.9.21
    kotlin("plugin.spring") version "2.1.0"           // Atualizado de 1.9.21
    kotlin("plugin.jpa") version "2.1.0"              // Atualizado de 1.9.21
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2" // Atualizado de 12.0.3
    id("io.gitlab.arturbosch.detekt") version "1.23.7"   // Atualizado de 1.23.4
}

dependencies {
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")     // 1.7.3 → 1.10.1
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.1")  // 1.7.3 → 1.10.1
    
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")      // 0.12.3 → 0.12.6
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")        // 0.12.3 → 0.12.6
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")     // 0.12.3 → 0.12.6
    
    // OpenAPI/Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")  // 2.3.0 → 2.7.0
    
    // HTML Sanitizer
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1") // 20220608.1 → 20240325.1
    
    // PDF Generation
    implementation("com.github.librepdf:openpdf:2.0.3")    // 1.3.35 → 2.0.3 (Breaking change)
    
    // Rate Limiting
    implementation("com.bucket4j:bucket4j-core:8.15.0")    // 8.7.0 → 8.15.0
    
    // Test
    testImplementation("io.mockk:mockk:1.13.14")           // 1.13.8 → 1.13.14
}

ktlint {
    version.set("1.5.0")  // 1.0.1 → 1.5.0
}
```

### ⚠️ Breaking Changes - Kotlin 2.1.0

Kotlin 1.9 → 2.1 principais mudanças:
- Melhorias no compilador
- Data classes com `copy()` mais eficiente
- Compatível com Spring Boot 3.4+

### ⚠️ Breaking Changes - OpenPDF 2.0

OpenPDF 1.3 → 2.0 tem mudanças na API:
- Pacotes renomeados
- Algumas classes movidas

**Verificar código em:** `backend/src/main/kotlin/.../export/PdfExportService.kt`

## 📋 Procedimento de Atualização

### 1. Atualizar Node.js

```powershell
# Verificar versão atual
node --version

# Instalar Node.js 20 LTS
# Baixar de: https://nodejs.org/

# Verificar instalação
node --version  # Deve mostrar v20.x.x
npm --version   # Deve mostrar v10.x.x
```

### 2. Limpar Cache e Dependências

```powershell
# Frontend
cd d:\Users\phdua\source\notion-clone\frontend
Remove-Item -Recurse -Force node_modules, package-lock.json
npm install

# Backend (limpar build)
cd d:\Users\phdua\source\notion-clone\backend
./gradlew clean
./gradlew build
```

### 3. Executar Testes

```powershell
# Frontend
cd frontend
npm run lint
npm run build

# Backend
cd backend
./gradlew test
./gradlew ktlintCheck
```

### 4. Verificar Breaking Changes

#### Frontend - Zustand
Procurar por `import create from 'zustand'` e substituir por:
```typescript
import { create } from 'zustand'
```

#### Backend - OpenPDF (se usando)
Verificar imports e API do OpenPDF em `PdfExportService.kt`

## 🔍 Verificação de Vulnerabilidades

```powershell
# Frontend
cd frontend
npm audit
npm audit fix

# Backend
cd backend
./gradlew dependencyCheckAnalyze
```

## 📊 Resumo das Atualizações

### Frontend
- ✅ Next.js 14.2.0 → 14.2.21 (patches de segurança)
- ✅ React 18.3.0 → 18.3.1 (patch)
- ✅ Zustand 4.5.0 → 5.0.2 (major - breaking change)
- ✅ TanStack Query 5.0.0 → 5.62.7 (patches + features)
- ✅ Tiptap 2.2.0 → 2.10.4 (melhorias + bugs)
- ✅ TypeScript 5.3.0 → 5.7.2 (melhorias)
- ✅ Lucide React 0.344.0 → 0.468.0 (novos ícones)

### Backend
- ✅ Spring Boot 3.2.1 → 3.4.1 (patches de segurança)
- ✅ Kotlin 1.9.21 → 2.1.0 (major - melhorias)
- ✅ Coroutines 1.7.3 → 1.10.1 (melhorias)
- ✅ JWT 0.12.3 → 0.12.6 (patches)
- ✅ OpenPDF 1.3.35 → 2.0.3 (major - breaking change)
- ✅ SpringDoc OpenAPI 2.3.0 → 2.7.0 (melhorias)
- ✅ Bucket4j 8.7.0 → 8.15.0 (melhorias)

## 🚀 Próximos Passos (Opcional)

### Considerar Upgrade para Next.js 15

**Requer Node.js 18.18.0+**

```json
{
  "dependencies": {
    "next": "^15.1.3",
    "react": "^19.0.0",
    "react-dom": "^19.0.0"
  }
}
```

**Breaking Changes React 19:**
- `React.FC` removido (usar function components)
- Mudanças no StrictMode
- Novos hooks: `useActionState`, `useOptimistic`

## 📝 Notas Importantes

1. **Node.js 14 não é mais suportado** - Atualizar é CRÍTICO
2. **Testar após atualização** - Executar todos os testes
3. **Verificar breaking changes** - Especialmente Zustand e OpenPDF
4. **Backup antes de atualizar** - Commit antes de aplicar mudanças
5. **Atualizar em etapas** - Frontend primeiro, depois backend

## 🔗 Links Úteis

- [Node.js Downloads](https://nodejs.org/)
- [Spring Boot 3.4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.4-Release-Notes)
- [Kotlin 2.1.0 Release](https://kotlinlang.org/docs/whatsnew21.html)
- [Zustand 5.0 Migration](https://github.com/pmndrs/zustand/releases/tag/v5.0.0)
- [Next.js 14 Docs](https://nextjs.org/docs)

---

**Data da atualização**: 31 de dezembro de 2025
**Criado por**: GitHub Copilot
