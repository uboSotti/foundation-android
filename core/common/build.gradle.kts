plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.hilt)
}

android {
    namespace = "com.foundation.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.timber)
}
