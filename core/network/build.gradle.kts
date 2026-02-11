plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
}

android {
    namespace = "com.foundation.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
}
