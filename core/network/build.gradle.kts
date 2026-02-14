plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
    id("foundation.kotlin.serialization")
}

android {
    namespace = "com.foundation.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.core)
    debugImplementation(libs.okhttp.logging)
}
