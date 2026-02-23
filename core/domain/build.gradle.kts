plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.hilt)
}

android {
    namespace = "com.foundation.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
