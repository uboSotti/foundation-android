# AGENTS.md

> **Notice**: This file is the **Official Protocol** for Autonomous AI Agents working on the `foundation-android` project.
> You must operate via **CLI commands**, adhere to **Android Best Practices**, and ensure **build stability** via self-correction.

## 1. Project Context & Environment
* **Project Name**: foundation-android
* **Architecture**: Modularized Clean Architecture (Multi-module)
* **Build System**: Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`)
* **UI Framework**: Jetpack Compose (Material3)
* **Target Environment**: Root Directory `./`, JDK 17+

## 2. Strategic Mission Analysis (Recommended Internal Protocol)
To ensure high-quality output, agents are encouraged to perform internal reasoning (Chain-of-Thought) before modification. If the runtime supports it, a summary of this analysis is preferred.

1.  **`<analysis>`**: Assess the request against the current module structure and dependencies.
2.  **`<plan>`**: Map out the files to be changed and the sequence of Gradle tasks required.
3.  **`<verification>`**: Self-evaluate the plan against the **Layering Principles** and **UDF** requirements.

## 3. Autonomous Execution & Verification Loop
Agents must take full responsibility for the integrity of the codebase:

1.  **Plan & Edit**: Modify code according to the internal analysis.
2.  **Sync & Build**: If dependencies or build scripts change, run `./gradlew --refresh-dependencies`. Always run `./gradlew assembleDebug` after changes.
3.  **Test Verification**: Execute `./gradlew testDebugUnitTest` to ensure no regression in business logic.
4.  **Self-Correction**: If any CLI error occurs, analyze the logs and apply fixes autonomously until the build is green.

## 4. Architecture & Coding Standards

### Module Structure & Layering (Wildcard Pattern)
* **`:app`**: Root entry point (DI, Navigation Host).
* **`:feature:*`**: Independent functional modules (Screens, ViewModels).
* **`:core:*`**: Shared capabilities (Domain, Data, UI, Network, Database, Common).

**Layering Principles (Strict Rules)**:
* **Unidirectional**: `:feature:*` → `:core:domain`, `:core:ui`.
* **Data Flow**: `:core:data` → `:core:domain` (via Interface).
* **Horizontal Isolation**: A `:feature:*` module **MUST NOT** depend on another `:feature:*` module.

### Development Principles
1.  **ViewModel-Centric Logic**:
    * **ALL** business logic **MUST** be placed in the **ViewModel**.
    * Composables must remain "stateless" or purely driven by the UI state provided by the ViewModel.
2.  **Official Android Recommendations**:
    * **UDF (Unidirectional Data Flow)**: Follow the state-down, events-up pattern strictly.
    * **Lifecycle Awareness**: Use `collectAsStateWithLifecycle()` for all UI state collections in Compose.
3.  **Dependency Management**:
    * **MUST** use `gradle/libs.versions.toml`. Hardcoded versions in `build.gradle.kts` are prohibited.
    * Always search for the **latest stable version** for any new library addition.

## 5. Operational Standards

### Git & Commits
* **Standard**: Follow **Conventional Commits 1.0.0**.
* **Format**: `type(scope): description` (Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`).

### File Handling
* Provide code changes in clear blocks or `diff` format.
* Do not restate unchanged code unless necessary for context.

## 6. Communication
* **Language**: Korean (Technical terms in English).
* **Tone**: Professional, Concise, and Objective.

---
**End of Protocol**