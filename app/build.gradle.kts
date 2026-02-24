plugins {
    alias(libs.plugins.foundation.android.application)
    alias(libs.plugins.foundation.android.hilt)
    alias(libs.plugins.foundation.android.compose)
}

android {
    namespace = "com.foundation.android"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.foundation.android"
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Feature modules
    implementation(project(":feature:example"))

    // Core modules
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Compose & UI
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // Misc
    implementation(libs.androidx.startup.runtime)
}
