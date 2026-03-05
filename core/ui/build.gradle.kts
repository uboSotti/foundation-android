plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.compose.ui)
}

android {
    namespace = "com.foundation.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
