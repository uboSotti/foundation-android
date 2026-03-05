plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.kotlin.serialization)
}

android {
    namespace = "com.foundation.feature.example.api"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
