plugins {
    id("foundation.android.library")
    id("foundation.android.hilt")
    id("foundation.android.room")
}

android {
    namespace = "com.foundation.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}
