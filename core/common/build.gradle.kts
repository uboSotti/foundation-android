plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
}

android {
    namespace = "com.foundation.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.timber)
}
