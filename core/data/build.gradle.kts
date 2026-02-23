plugins {
    alias(libs.plugins.foundation.android.library)
    alias(libs.plugins.foundation.android.hilt)
}

android {
    namespace = "com.foundation.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.androidx.datastore.preferences)
}
