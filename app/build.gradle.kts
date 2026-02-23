plugins {
    id("foundation.android.application")
    id("foundation.android.hilt")
    id("foundation.android.compose")
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
    // Feature Modules
    implementation(project(":feature:example"))

    // Core Modules
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // Compose & UI
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // Misc
    implementation(libs.androidx.startup.runtime)
}
