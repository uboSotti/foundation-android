# foundation-android

Android template project with **Clean Architecture + official Android architecture guidance + Navigation 3 modularization**.
This repository is a template for starting new Android apps quickly with proven libraries and validated architecture conventions.

## Document Role
- `README.md`: project intent, architecture decisions, and contributor-facing usage.
- `AGENTS.md`: autonomous agent execution protocol and guardrails.

## Goals
- Start from a production-ready baseline (DI, modularization, architecture conventions, testing setup).
- Keep architecture readable and scalable from day 1.
- Follow Android official layer guidance (UI / Domain / Data) pragmatically.

## Architecture Principles
- Clean Architecture dependency direction: outer layers depend on inner layers.
- UI follows MVI + UDF:
  - immutable `UiState` (`data class`)
  - `Intent` / `SideEffect` (`sealed interface`)
  - `ViewModel` as state holder (`StateFlow`)
- Domain layer is optional and lightweight; use cases are added when they improve readability/reuse.
- Data layer uses repositories as entry points and typed error mapping.

## Module Structure
```text
foundation-android/
├── app
├── build-logic/
│   └── convention/
├── core/
│   ├── common
│   ├── model
│   ├── domain
│   ├── data
│   ├── network
│   ├── database
│   ├── ui
│   ├── navigation
│   └── testing
└── feature/
    └── example/
        ├── api
        └── impl
```

## Navigation 3 + Modularization
This project follows Navigation 3 modularization guidance.

- `feature/*/api`
  - owns `NavKey` definitions (e.g. `ExampleNavKey`)
- `feature/*/impl`
  - owns screen/viewmodel/navigation entries
  - contributes entry builders via Hilt `@IntoSet`
- `app`
  - collects entry builder factories and start destinations from all features
  - builds `NavDisplay` entryProvider from contributed builders

### Back stack and keys
- Back stack is app-owned state (`SnapshotStateList<NavKey>`).
- Keys must be serializable and implement `androidx.navigation3.runtime.NavKey`.
- Missing start destination is treated as configuration error (fail-fast).

### Entry registration pattern
- Each feature provides:
  - `FeatureEntryBuilderFactory`
  - `StartDestination`
- App composes all builders into `entryProvider { ... }`.

## Layer Rules (summary)
- `:feature:*` -> `:core:domain`, `:core:ui`, `:core:navigation`, `:core:common`, `:core:model` (via convention plugin)
- `:core:data` -> `:core:domain`, `:core:network`, `:core:database`, `:core:model`, `:core:common`
- no feature-to-feature dependency

## Network Configuration
- Base URL is provided via `Host` abstraction in `:core:network` (not app `BuildConfig`).
- Feature modules do not know host implementation details.

## Build & Verify
```bash
./gradlew verifyArchitectureConventions
./gradlew assembleDebug
./gradlew testDebugUnitTest
```
If build scripts/dependencies change:
```bash
./gradlew --refresh-dependencies
```

## Conventions
- Versions are managed only in `gradle/libs.versions.toml`.
- Use convention plugins from `build-logic/convention`.
- Convention plugins are split by purpose:
  - `foundation.android.compose.base` / `foundation.android.compose.ui`
  - `foundation.android.feature.base/domain/ui/navigation3/lifecycle`
  - `foundation.kotlin.serialization.plugin` / `foundation.kotlin.serialization.json`
- Prefer typed errors (`AppError`) and `Result` pipeline.
- Use `collectAsStateWithLifecycle()` for all UI state collection.

## Example Feature Checklist
For a new feature:
1. Create `feature/<name>/api` and define `NavKey`.
2. Create `feature/<name>/impl` with `UiState/Intent/SideEffect/ViewModel/Route/Screen`.
3. Provide entry builder + start destination via Hilt multibinding.
4. Add module includes and app dependency.
5. Run `assembleDebug` and `testDebugUnitTest`.

## Contributing
See [AGENTS.md](AGENTS.md) for strict execution and architecture protocol.

## References
- [UI layer guide](https://developer.android.com/topic/architecture/ui-layer.md.txt)
- [Domain layer guide](https://developer.android.com/topic/architecture/domain-layer.md.txt)
- [Data layer guide](https://developer.android.com/topic/architecture/data-layer.md.txt)
- [Navigation 3 guide](https://developer.android.com/guide/navigation/navigation-3.md.txt)
- [Navigation 3 modularization](https://developer.android.com/guide/navigation/navigation-3/modularize.md.txt)
