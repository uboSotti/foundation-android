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
            id = "foundation.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidComposeBase") {
            id = libs.plugins.foundation.android.compose.base.get().pluginId
            implementationClass = "AndroidComposeBaseConventionPlugin"
        }
        register("androidComposeUi") {
            id = libs.plugins.foundation.android.compose.ui.get().pluginId
            implementationClass = "AndroidComposeUiConventionPlugin"
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
            id = "foundation.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidFeatureBase") {
            id = libs.plugins.foundation.android.feature.base.get().pluginId
            implementationClass = "AndroidFeatureBaseConventionPlugin"
        }
        register("androidFeatureDomain") {
            id = libs.plugins.foundation.android.feature.domain.get().pluginId
            implementationClass = "AndroidFeatureDomainConventionPlugin"
        }
        register("androidFeatureUi") {
            id = libs.plugins.foundation.android.feature.ui.get().pluginId
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidFeatureNavigation3") {
            id = libs.plugins.foundation.android.feature.navigation3.get().pluginId
            implementationClass = "AndroidFeatureNavigation3ConventionPlugin"
        }
        register("androidFeatureLifecycle") {
            id = libs.plugins.foundation.android.feature.lifecycle.get().pluginId
            implementationClass = "AndroidFeatureLifecycleConventionPlugin"
        }
        register("kotlinSerialization") {
            id = "foundation.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("kotlinSerializationPlugin") {
            id = libs.plugins.foundation.kotlin.serialization.plugin.get().pluginId
            implementationClass = "KotlinSerializationPluginConventionPlugin"
        }
        register("kotlinSerializationJson") {
            id = libs.plugins.foundation.kotlin.serialization.json.get().pluginId
            implementationClass = "KotlinSerializationJsonConventionPlugin"
        }
    }
}
