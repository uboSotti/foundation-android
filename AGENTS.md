# AGENTS.md

Official protocol for AI/code agents in `foundation-android`.

## Scope
- `AGENTS.md` defines execution behavior for agents.
- Architecture/context explanations belong in `README.md`; do not duplicate long narrative here.

## 1) Mission
- Preserve architecture integrity.
- Follow Android official layer guidance (UI / Domain / Data) and Navigation 3 guidance.
- Keep the project always buildable.

## 2) Mandatory Workflow
1. Analyze impact before edits.
2. Edit with clear module/layer boundaries.
3. If build scripts or dependencies changed: run `./gradlew --refresh-dependencies`.
4. Always run:
   - `./gradlew verifyArchitectureConventions`
   - `./gradlew assembleDebug`
   - `./gradlew testDebugUnitTest`
5. If failure occurs, self-correct until green.

## 3) Architecture Rules
### Layering
- `:feature:*` must not depend on another `:feature:*`.
- `:core:domain` must not depend on `:core:data` or network implementation.
- `:core:data` is the entry point to data sources; other layers access data via repositories/use cases.

### UI Layer
- Use MVI + UDF.
- `UiState` is immutable (`data class`).
- Composables are stateless/pure UI where possible.
- `Route` collects state/effects, `Screen` renders UI.
- Use `collectAsStateWithLifecycle()`.

### Domain Layer
- Optional layer; add use cases when complexity/reuse justifies it.
- Use case naming: `VerbNounUseCase`.
- Prefer `operator fun invoke()`.

### Data Layer
- Repositories are data entry points.
- Data sources are not used directly by UI layer.
- Keep mapping and conflict resolution inside repository/data layer.

## 4) Navigation 3 + Modularization Rules
- Follow feature split: `feature/<name>/api` + `feature/<name>/impl`.
- `api` contains `NavKey`.
- `impl` contains entries/content and DI bindings.
- Keys implement `androidx.navigation3.runtime.NavKey` and should be serializable.
- App builds `entryProvider` from DI-contributed entry builders.
- Unknown/missing navigation configuration should fail fast, not silently ignored.

## 5) Dependency and Build Rules
- No hardcoded library versions in module build scripts.
- Use `libs.versions.toml` and convention plugins.
- Prefer adding shared module constraints in `build-logic` when rule should be global.
- Apply split convention plugins by intent:
  - Compose: `foundation.android.compose.base` / `foundation.android.compose.ui`
  - Feature: `foundation.android.feature.base/domain/ui/navigation3/lifecycle`
  - Serialization: `foundation.kotlin.serialization.plugin` / `foundation.kotlin.serialization.json`

## 6) Coding and Change Policy
- Keep changes minimal but complete.
- Preserve existing behavior unless change is intentional and verified.
- Do not leave architecture in intermediate broken state.
- Include concise rationale when introducing new abstractions.

## 7) Commit Convention
- Conventional Commits: `type(scope): description`
- Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`

## 8) Communication
- Korean for discussion; keep technical terms in English.
- Be concise, objective, and evidence-based.

End of protocol.
