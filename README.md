# foundation-android

Modern Android Application Template following Clean Architecture and Modularization best practices.

## Project Structure

This project adopts a multi-module architecture:

*   **`:app`**: Application entry point, DI setup (Hilt), Navigation graph.
*   **`:core`**: Shared logic and resources.
    *   **`:core:model`**: Domain models (Pure Kotlin/Android).
    *   **`:core:domain`**: Use cases, Repository interfaces (Clean Architecture).
    *   **`:core:data`**: Repository implementations, Data sources.
    *   **`:core:network`**: Retrofit/OkHttp configurations.
    *   **`:core:database`**: Room database configurations.
    *   **`:core:ui`**: Design system, Theme, Common Compose components.
    *   **`:core:common`**: Utility functions, Dispatcher providers.
    *   **`:core:testing`**: Shared test utilities and rules.
*   **`:feature`**: Feature-specific modules (e.g., `:feature:example`).
    *   Depends on `:core:domain`, `:core:ui`, `:core:model`.

## Tech Stack

*   **Language**: Kotlin
*   **UI**: Jetpack Compose (Material3)
*   **Architecture**: MVVM + Clean Architecture (Data/Domain/UI layers)
*   **Dependency Injection**: Hilt
*   **Async**: Coroutines + Flow
*   **Network**: Retrofit + OkHttp
*   **Database**: Room
*   **Build System**: Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`) + Convention Plugins

## Convention Plugins

Build logic is shared via convention plugins located in `build-logic`:

*   `foundation.android.application`: Configures Android Application module.
*   `foundation.android.library`: Configures Android Library module.
*   `foundation.android.hilt`: Configures Hilt.
*   `foundation.android.compose`: Configures Jetpack Compose.
*   `foundation.android.room`: Configures Room.
*   `foundation.android.feature`: Aggregates plugins for Feature modules.

## Getting Started

1.  Sync Project with Gradle Files.
2.  Run the `app` configuration.

## Contributing

Please refer to `AGENTS.md` for coding conventions and guidelines.
