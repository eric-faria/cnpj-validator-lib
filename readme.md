# 🛡️ Core Business Logic: CNPJ & Pix Validator (KMP)

Prova de Conceito (POC) arquitetural demonstrando o uso de **Kotlin Multiplatform (KMP)** para centralizar regras de negócio críticas da nossa fintech. O objetivo é escrever o código de validação apenas uma vez e distribuí-lo nativamente para ecossistemas JVM (Spring Boot) e JavaScript (Node.js/BFF/Frontend).

## 🎯 O Problema & A Solução

Historicamente, regras de validação (como cálculos complexos de dígito verificador e hashes) são duplicadas no Backend (Java/Kotlin) e no Frontend/BFF (JavaScript/TypeScript). Essa duplicação de regras gera:

* **Risco de Dessincronização:** Ex: Adoção do novo modelo de CNPJ Alfanumérico da RFB (Regra 2026).
* **Retrabalho Técnico:** Necessidade de escrever e manter a mesma suíte de testes unitários em duas linguagens diferentes.
* **Bugs em Produção:** Falsos positivos gerados por diferenças sutis de implementação entre linguagens.

**A Solução KMP:** Esta biblioteca garante uma **Single Source of Truth** (Fonte Única de Verdade). A regra é escrita em Kotlin puro, validada matematicamente e transpilada com máxima fidelidade para binários Java e pacotes JavaScript.

## ✨ Funcionalidades (Regras Cobertas)

* ✅ **Validador de CNPJ (`CnpjValidator`)**
  * Suporte total à nova regra da Receita Federal (CNPJ Alfanumérico - 2026).
  * Suporte ao modelo numérico clássico (com ou sem máscara).
  * Bloqueio de sequências repetidas e cálculo de dígitos verificadores (Módulo 11).

* ✅ **Validador de End-to-End ID do Pix (`PixE2eIdValidator`)**
  * Validação estrutural rigorosa do ID de 32 caracteres do Banco Central.
  * Verificação de prefixo (`E`), tamanho, integridade do ISPB e parsing de data/hora válidas.

## 🛠️ Stack Tecnológica & Decisões Arquiteturais

* **Linguagem:** Kotlin `2.3.20` (Padrão moderno, Provider API nativa).
* **Target JVM:** Java 17 (Configurado via Gradle Toolchain nativo).
* **Target JS:** Node.js (Geração de ecossistema CommonJS/ES Modules com tipagem TypeScript `.d.ts` injetada via anotação `@JsExport`).
* **Automação & Build:** Gradle 9.x + GitHub Actions.
* **Distribuição (Registry):** GitHub Packages (Maven Privado e NPM Privado).

## 📦 Estrutura do Projeto

A arquitetura do projeto segue o padrão oficial da JetBrains para KMP:
    
    cnpj-validator-lib/
    ├── src/
    │   ├── commonMain/      # Código Kotlin puro com as regras de negócio (Sem dependência de I/O ou OS).
    │   └── commonTest/      # Testes unitários executados simultaneamente em JVM e Node.js.
    ├── .github/
    │   └── workflows/       # Pipelines de CI (Validação de PRs) e CD (Release de Packages).
    └── build.gradle.kts     # Orquestração dos targets de compilação (jvm() e js(IR)).

## 🚀 Ciclo de Vida e CI/CD (Segurança)

Visando a segurança e estabilidade dos microsserviços, o processo de publicação utiliza a diretriz de **Publishing by CI Only**. Publicações locais de artefatos são bloqueadas. O fluxo da equipe deve ser:

1. **Desenvolvimento (PR):** O desenvolvedor abre um Pull Request para a branch `main`.
2. **Continuous Integration (CI):** O GitHub Actions (`pr-check.yml`) entra em ação executando `./gradlew check`. O PR é **bloqueado** se algum teste falhar nos motores JVM ou Node.
3. **Merge & Tagging:** Após revisão, o PR é mergeado. O Tech Lead atualiza a versão no `build.gradle.kts` e gera uma Release Tag (ex: `v1.0.4`).
4. **Continuous Delivery (CD):** O workflow de publicação (`publish.yml`) intercepta a Tag, empacota e distribui os artefatos assinados:
    * ☕ Um arquivo `.jar` publicado no Maven Registry do GitHub.
    * 🟨 Um pacote `.tgz` com definições tipadas publicado no NPM Registry do GitHub.

## 🧑‍💻 Como Consumir esta Biblioteca

Por se tratar de um pacote privado corporativo, a máquina hospedeira ou a esteira de CI/CD precisa estar autenticada no GitHub Packages via **Personal Access Token (PAT)** com escopo `read:packages`.

### 1. Consumo no Backend (Spring Boot / Java / Kotlin)

Adicione o repositório privado e a dependência no seu `build.gradle.kts` (ou `pom.xml` equivalente):

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("[https://maven.pkg.github.com/eric-faria/cnpj-validator-lib](https://maven.pkg.github.com/eric-faria/cnpj-validator-lib)")
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    // O Gradle resolverá automaticamente o artefato para a variante JVM
    implementation("com.ericfaria.lib:cnpj-validator-lib:1.0.4")
}
```

### 2. Consumo no BFF/Frontend (Node.js / TypeScript)

Garanta que seu .npmrc aponta para o Registry corporativo:
Plaintext

    @eric-faria:registry=[https://npm.pkg.github.com/](https://npm.pkg.github.com/)
    //[npm.pkg.github.com/:_authToken=$](https://npm.pkg.github.com/:_authToken=$){GITHUB_TOKEN}

E instale via NPM:
```Bash
npm install @eric-faria/cnpj-validator-lib
```

Uso no Código TypeScript:
```TypeScript

import { CnpjValidator, PixE2eIdValidator } from '@eric-faria/cnpj-validator-lib';

const isCnpjValid = CnpjValidator.isValid("12ABC34501DE35");
const isPixValid = PixE2eIdValidator.isValid("E00000000202604130900abcdef12345");
```
---
