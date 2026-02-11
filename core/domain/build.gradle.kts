plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
}

android {
    namespace = "com.foundation.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}
