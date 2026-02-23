# foundation-android

A pre-configured Android project template designed to be cloned as the starting point for new apps. All libraries are verified, wired together, and working — so you can skip the setup and start building features immediately.

## Purpose

Starting a new Android project from scratch involves significant repetitive work: choosing libraries, configuring the build system, establishing architecture conventions, writing boilerplate DI setup, and validating that everything actually works together. This template eliminates that overhead.

**What this is:**
- A stable, buildable baseline with all foundational decisions already made
- A reference implementation of the architectural patterns described below
- A starting point to clone and rename, not a library to depend on

**What this is not:**
- A framework or SDK
- A production app with business logic

## Architecture Philosophy

The guiding direction is **Clean Architecture** — clear separation between data, domain, and UI layers, with dependency flow pointing inward (UI depends on domain, domain depends on nothing platform-specific). However, this is tempered by practical Android considerations:

- **MVVM in the presentation layer**: ViewModel + `StateFlow` + sealed `UiState` interfaces. This aligns with the Android platform grain; fighting it creates unnecessary friction.
- **Unidirectional Data Flow (UDF)**: State flows down, events flow up. The ViewModel owns state and exposes it as `StateFlow`.
- **Multi-module from the start**: Feature isolation and core module separation are established early, before the codebase is large enough to make refactoring painful. This directly benefits build speed via parallel compilation and incremental builds.
- **Pragmatism over purity**: Domain use cases are present but lightweight. Not every operation needs a use case class if the repository abstraction is sufficient.

The result is architecture that is genuinely useful for a team — not a theoretical exercise.

## Module Structure

```
foundation-android/
├── app/                    # Application entry point
├── build-logic/            # Convention plugins (Gradle build system)
│   └── convention/
├── core/
│   ├── common/             # Cross-cutting utilities
│   ├── model/              # Domain models
│   ├── domain/             # Use cases and repository interfaces
│   ├── data/               # Repository implementations, data sources
│   ├── network/            # Retrofit + OkHttp configuration
│   ├── database/           # Room database configuration
│   ├── ui/                 # Design system, theme, shared Compose components
│   └── testing/            # Shared test utilities
└── feature/
    └── example/            # Example feature demonstrating all patterns
```

### Module Responsibilities

| Module | Responsibility | Key Dependencies |
|---|---|---|
| `:app` | Application class, DI graph root, navigation host, app-level state | `:feature:*`, `:core:ui`, `:core:data`, `:core:common` |
| `:core:common` | Coroutine dispatchers (IO/Default/Main) via Hilt, `Result<T>` wrapper, `@BaseUrl` qualifier | Coroutines, Timber |
| `:core:model` | Pure Kotlin domain models, `@Serializable`, `@Parcelize` | Kotlinx Serialization, Parcelize |
| `:core:domain` | Repository interfaces, use case base classes | `:core:common`, `:core:model` |
| `:core:data` | Repository implementations bridging network and database | `:core:domain`, `:core:network`, `:core:database` |
| `:core:network` | Retrofit instance, OkHttp client, JSON converter, debug logging interceptor | Retrofit, OkHttp, Kotlinx Serialization |
| `:core:database` | Room database definition, DAOs, type converters | Room |
| `:core:ui` | `FoundationTheme` (Material3 + Dynamic Color), shared Composables (`LoadingContent`, `ErrorContent`, `EmptyContent`) | Compose, Material3 |
| `:core:testing` | `MainDispatcherRule`, shared test dependencies exposed via `api()` | JUnit, MockK, Turbine, Compose UI Test |
| `:feature:example` | End-to-end example: ViewModel, UiState, Screen, Navigation | `foundation.android.feature` plugin (aggregates all needed core deps) |

### Module Dependency Rules

Feature modules depend on core modules only. Feature modules never depend on each other. If a feature needs data from another domain, that data is accessed through shared core layers, not by importing another feature module.

```
:feature:* → :core:domain → :core:model
                           → :core:common
:feature:* → :core:ui
:app       → :feature:*
:app       → :core:data   → :core:network
                          → :core:database
```

## Convention Plugins

Convention plugins live in `build-logic/convention/` and eliminate build script boilerplate across modules. Rather than repeating the same `compileSdk`, `minSdk`, `jvmTarget`, plugin applications, and dependency declarations in every `build.gradle.kts`, each module applies one or more convention plugins.

| Plugin ID | What it configures |
|---|---|
| `foundation.android.application` | `com.android.application`, `compileSdk 36`, `minSdk 26`, `targetSdk 36`, Java 17, R8/ProGuard for release |
| `foundation.android.library` | `com.android.library`, same SDK/Java baseline as above |
| `foundation.android.hilt` | Hilt plugin, KSP, `hilt-android` + `hilt-android-compiler` dependencies |
| `foundation.android.compose` | Kotlin Compose compiler plugin, Compose BOM, Material3, UI tooling |
| `foundation.android.room` | Room Gradle plugin, KSP, Room runtime/ktx/compiler, schema export directory |
| `foundation.android.feature` | Aggregates `library` + `hilt` + `compose`; auto-adds `:core:model`, `:core:ui`, `:core:domain`, `:core:common`, Navigation3, Hilt Navigation Compose, Lifecycle, Coroutines |
| `foundation.kotlin.serialization` | Kotlin Serialization plugin + `kotlinx-serialization-json` dependency |

**Example — a new feature module `build.gradle.kts`:**

```kotlin
plugins {
    id("foundation.android.feature")
}

android {
    namespace = "com.yourapp.feature.profile"
}

dependencies {
    // Only feature-specific additions needed here
}
```

The `foundation.android.feature` plugin handles the rest automatically.

## Tech Stack

### Language & Core

| Library | Version |
|---|---|
| Kotlin | 2.3.10 |
| KSP (Kotlin Symbol Processing) | 2.3.5 |

### UI

| Library | Version |
|---|---|
| Jetpack Compose BOM | 2026.02.00 |
| Material3 | (via BOM) |
| Activity Compose | 1.12.4 |
| Lifecycle Runtime KTX | 2.10.0 |

### Navigation

| Library | Version |
|---|---|
| androidx.navigation3 runtime | 1.0.1 |
| androidx.navigation3 ui | 1.0.1 |
| Hilt Navigation Compose | 1.3.0 |

Navigation is managed via a `SnapshotStateList`-backed back stack in `FoundationAppState`. Each feature exposes a `NavKey` (a `@Serializable` object/data class) and a `NavEntry` factory function.

### Dependency Injection

| Library | Version |
|---|---|
| Hilt | 2.59.1 |

### Networking

| Library | Version |
|---|---|
| Retrofit | 3.0.0 |
| OkHttp | 5.3.2 |
| Kotlinx Serialization JSON converter | (via Retrofit 3.0.0) |

OkHttp logging interceptor is added in `debugImplementation` only. The `@BaseUrl` Hilt qualifier allows the base URL to be injected from `:app`'s `BuildConfig` without `:core:network` depending on `:app`. Interceptors are registered via Hilt multibindings (`@IntoSet`) for extensibility.

### Serialization

| Library | Version |
|---|---|
| Kotlinx Serialization JSON | 1.10.0 |

### Database

| Library | Version |
|---|---|
| Room runtime + ktx + compiler | 2.8.4 |

Schema export is enabled and configured to export to a `schemas/` directory within each database module.

### Async

| Library | Version |
|---|---|
| Kotlin Coroutines | 1.10.2 |

IO, Default, and Main dispatchers are provided via Hilt using `@Dispatcher(FoundationDispatcher.IO)` qualifiers, making dispatcher injection testable.

### App Initialization & Logging

| Library | Version |
|---|---|
| AndroidX App Startup | 1.2.0 |
| Timber | 5.0.1 |

Timber is initialized automatically via `TimberInitializer` (an `androidx.startup.Initializer`). The debug tree is planted only on debuggable builds — no manual `Application.onCreate()` call needed.

### Testing

| Library | Version |
|---|---|
| JUnit 4 | 4.13.2 |
| AndroidX JUnit | 1.3.0 |
| Espresso | 3.7.0 |
| MockK | 1.14.9 |
| Turbine (Flow testing) | 1.2.1 |
| Compose UI Test | (via Compose BOM) |
| Kotlinx Coroutines Test | 1.10.2 |

All test dependencies are declared in `:core:testing` and exposed via `api()`, so any module that depends on `:core:testing` receives all testing utilities transitively. `MainDispatcherRule` is included for ViewModel unit tests.

### Build System

| Tool | Version |
|---|---|
| Android Gradle Plugin | 9.0.1 |
| Gradle Kotlin DSL | — |
| Version Catalog (`libs.versions.toml`) | — |
| Convention Plugins (`build-logic/`) | — |

## Build Configuration

`gradle.properties`:

```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=2g -XX:+HeapDumpOnOutOfMemoryError
org.gradle.parallel=true
org.gradle.configuration-cache=true
kotlin.code.style=official
```

SDK targets:

- `minSdk` = 26 (Android 8.0)
- `compileSdk` / `targetSdk` = 36
- Java / JVM target = 17

## Getting Started

### Prerequisites

- Android Studio Meerkat or newer
- JDK 17

### Using as a Template

**1. Clone or use as template**

```bash
git clone <this-repo-url> my-new-app
cd my-new-app
```

**2. Rename the package**

- In Android Studio: Refactor > Rename the root package from `com.foundation.android` to your package name
- Update `namespace` in every module's `build.gradle.kts`
- Update `applicationId` in `:app/build.gradle.kts`
- Update `rootProject.name` in `settings.gradle.kts`

**3. Set your base URL**

In `:app/build.gradle.kts`, update the `BASE_URL` `buildConfigField`:

```kotlin
buildConfigField("String", "BASE_URL", "\"https://api.yourapp.com/\"")
```

**4. Remove or replace the example feature**

`:feature:example` demonstrates all patterns. Once familiar, delete it and create your first real feature:

```bash
# Remove the module directory
rm -rf feature/example

# Remove its include from settings.gradle.kts
# Remove its dependency from :app/build.gradle.kts
```

**5. Create your first feature module**

Create `feature/your-feature/build.gradle.kts`:

```kotlin
plugins {
    id("foundation.android.feature")
}

android {
    namespace = "com.yourapp.feature.yourfeature"
}
```

Add `include(":feature:your-feature")` to `settings.gradle.kts`.

Implement your `NavKey`, `UiState`, `ViewModel`, `Screen`, and navigation entry following the `:feature:example` pattern.

**6. Sync and build**

```bash
./gradlew :app:assembleDebug
```

### Adding a New Core Module

```kotlin
// core/newmodule/build.gradle.kts
plugins {
    id("foundation.android.library")
    // add hilt, room, serialization as needed
}

android {
    namespace = "com.yourapp.core.newmodule"
}
```

Add `include(":core:newmodule")` to `settings.gradle.kts`.

## Project Conventions

- **UiState**: Sealed interfaces per screen, with at minimum `Loading` and `Success` states
- **Result wrapper**: Use `Result<T>` from `:core:common` across data/domain boundaries; map to `UiState` in ViewModel
- **Dispatcher injection**: Always inject `CoroutineDispatcher` via `@Dispatcher` qualifier rather than hardcoding `Dispatchers.IO`
- **Navigation keys**: Each feature owns its own `NavKey` (`@Serializable` object or data class)
- **No inter-feature dependencies**: Features communicate through shared domain/data layers only
- **Debug-only tools**: OkHttp logging interceptor and Timber debug tree are applied in debug build variants only

## Contributing

Please refer to `AGENTS.md` for coding conventions and guidelines.
