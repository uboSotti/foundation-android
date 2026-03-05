plugins {
    alias(libs.plugins.foundation.android.feature.base)
    alias(libs.plugins.foundation.android.feature.domain)
    alias(libs.plugins.foundation.android.feature.ui)
    alias(libs.plugins.foundation.android.feature.navigation3)
    alias(libs.plugins.foundation.android.feature.lifecycle)
}

android {
    namespace = "com.foundation.feature.example"
}

dependencies {
    implementation(project(":feature:example:api"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(project(":core:testing"))
}
