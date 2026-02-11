plugins {
    id("foundation.android.application")
    id("foundation.android.hilt")
    id("foundation.android.compose")
}

android {
    namespace = "com.foundation.android"

    defaultConfig {
        applicationId = "com.foundation.android"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":feature:example"))

    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
}
