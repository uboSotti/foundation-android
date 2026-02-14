plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
    id("foundation.android.compose")
}

android {
    namespace = "com.foundation.core.testing"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    api(libs.junit)
    api(libs.androidx.test.junit)
    api(libs.androidx.test.espresso)
    api(libs.mockk)
    api(libs.turbine)
    api(libs.kotlinx.coroutines.test)
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.compose.ui.test.manifest)
}
