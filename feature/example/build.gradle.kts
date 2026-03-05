plugins {
    alias(libs.plugins.foundation.android.feature)
}

android {
    namespace = "com.foundation.feature.example"
}

dependencies {
    implementation(project(":core:navigation"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    testImplementation(project(":core:testing"))
}
