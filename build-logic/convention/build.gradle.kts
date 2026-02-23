plugins {
    `kotlin-dsl`
}

group = "com.foundation.buildlogic"

dependencies {
    // Android & Kotlin core
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)

    // Kotlin compiler plugins
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.kotlin.serialization.plugin)

    // Annotation processing
    compileOnly(libs.ksp.gradle.plugin)

    // DI
    compileOnly(libs.hilt.gradle.plugin)

    // Storage
    compileOnly(libs.room.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.foundation.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.foundation.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = libs.plugins.foundation.android.compose.get().pluginId
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = libs.plugins.foundation.android.hilt.get().pluginId
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.foundation.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFeature") {
            id = libs.plugins.foundation.android.feature.get().pluginId
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("kotlinSerialization") {
            id = libs.plugins.foundation.kotlin.serialization.get().pluginId
            implementationClass = "KotlinSerializationConventionPlugin"
        }
    }
}
