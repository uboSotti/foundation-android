plugins {
    id("foundation.android.library")
    id("foundation.android.compose")
}

android {
    namespace = "com.foundation.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}
