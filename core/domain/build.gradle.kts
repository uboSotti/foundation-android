plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
}

android {
    namespace = "com.foundation.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
