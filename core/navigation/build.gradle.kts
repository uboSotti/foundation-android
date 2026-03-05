plugins {
    alias(libs.plugins.foundation.android.library)
}

android {
    namespace = "com.foundation.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
}
