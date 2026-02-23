plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.hilt)
    alias(libs.plugins.foundation.android.room)
}

android {
    namespace = "com.foundation.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
