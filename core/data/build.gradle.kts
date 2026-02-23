plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
}

android {
    namespace = "com.foundation.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.datastore.preferences)
}
