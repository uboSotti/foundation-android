pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "foundation-android"
include(":app")
include(":core:common")
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:testing")
include(":feature:example")
