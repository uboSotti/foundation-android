plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.compose)
}

android {
    namespace = "com.foundation.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
