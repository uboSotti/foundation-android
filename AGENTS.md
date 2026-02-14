# AGENTS.md

> **Notice**: This file is the **Official Protocol** for Autonomous AI Agents working on the `foundation-android` project.
> You must operate via **CLI commands**, follow a **strict thinking process**, and ensure **build stability** via self-correction.

## 1. Project Context & Environment

* **Project Name**: foundation-android
* **Architecture**: Modularized Clean Architecture (Multi-module)
* **Build System**: Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`)
* **Environment**:
* Root Directory: `./`
* JDK: Version 17+
* Network Access: Allowed for dependency resolution.



## 2. Operational Protocol (Thinking Process)

Before generating code or executing commands, you **MUST** output your internal reasoning using these XML tags. This serves as your execution log.

1. **`<analysis>`**: Analyze the goal, current codebase state, and affected modules.
2. **`<plan>`**: Define the files to modify and the logical order of operations.
3. **`<verification>`**: Self-check against architectural rules (e.g., "Is business logic in ViewModel?", "Is the commit message correct?").

**Example Output:**

```xml
<analysis>
  Goal: Add 'Login' feature.
  Current: Missing :feature:login module.
  Impact: Create module, add to settings.gradle.kts, update libs.versions.toml.
</analysis>
<plan>
  1. Define dependencies in libs.versions.toml.
  2. Create :feature:login with AndroidFeatureConventionPlugin.
  3. Implement LoginViewModel for logic and LoginScreen for UI.
</plan>
<verification>
  Checked: :feature:login depends on :core:ui.
  Checked: Login logic is encapsulated in ViewModel (UDF).
</verification>

```

## 3. Execution & Validation Cycle (Autonomous Loop)

Instead of waiting for user approval, follow this strict **Self-Validation Loop**:

1. **Plan & Edit**: Modify files based on your `<plan>`.
2. **Sync**: If build logic changed, run `./gradlew --refresh-dependencies` (or equivalent).
3. **Build Verification**: **IMMEDIATELY** run `./gradlew assembleDebug` after significant changes.
4. **Test Verification**: Run `./gradlew testDebugUnitTest` to ensure no regression.
5. **Self-Correction**:
* **If Build Fails**: Read the CLI error log, analyze the root cause, and apply a fix. **Do not stop** unless the error is unresolvable.
* **If Build Passes**: Proceed to the next task or mark as complete.



## 4. CLI Verification Commands

Use these commands to verify your work:

| Action | Command | Purpose |
| --- | --- | --- |
| **Verify Build** | `./gradlew assembleDebug` | Essential check after any code change. |
| **Verify Logic** | `./gradlew testDebugUnitTest` | Check for logic regressions. |
| **Lint Check** | `./gradlew lintDebug` | Check for code style violations. |
| **Clean** | `./gradlew clean` | Use if you suspect cache issues. |

## 5. Coding & Architecture Standards

### Module Structure (Wildcard Pattern)

* **`:app`**: Application Root (DI, Nav).
* **`:feature:*`**: Independent features (Screens, ViewModels).
* **`:core:*`**: Shared logic (Domain, Data, UI, Network, Database).

### Critical Architecture Rules

1. **Business Logic Placement**:
* **ALL** business logic **MUST** be placed in the **ViewModel**.
* Composables **MUST** be pure and only render the state provided by the ViewModel.
* Do not write logic inside UI components (e.g., `onClick { logic() }` is forbidden; use `onClick { viewModel.process() }`).


2. **Official Android Architecture**:
* **UDF (Unidirectional Data Flow)**: Strictly follow `ViewModel`  `State`  `UI`  `Event`  `ViewModel`.
* **Lifecycle Awareness**: Always use lifecycle-aware state collection methods like `collectAsStateWithLifecycle()` in Composables.
* **Layered Architecture**: Respect the separation of concerns (UI Layer, Domain Layer, Data Layer).


3. **Layering Principles**:
* `:feature:*`  `:core:domain`, `:core:ui`
* `:core:data`  `:core:domain`
* **Forbidden**: `:feature:A`  `:feature:B` (Horizontal dependency).



### Operational Standards

1. **Git Conventions**:
* Commit messages **MUST** follow **Conventional Commits 1.0.0**.
* **Format**: `type(scope): description`
* **Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`.
* **Example**: `feat(login): Implement input validation in ViewModel`


2. **Dependency Management**:
* **MUST** use `libs.versions.toml`. **NEVER** hardcode versions.
* **ALWAYS** search/use the **latest stable version** for new libraries.


3. **File Output**:
* Use `diff` format or clear code blocks. Do not dump unchanged file content.
