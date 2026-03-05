plugins {
    alias(libs.plugins.foundation.android.library)
}

android {
    namespace = "com.foundation.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
}
