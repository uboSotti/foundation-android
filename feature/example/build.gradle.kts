plugins {
    alias(libs.plugins.foundation.android.feature)
}

android {
    namespace = "com.foundation.feature.example"
}

dependencies {
    testImplementation(project(":core:testing"))
}
