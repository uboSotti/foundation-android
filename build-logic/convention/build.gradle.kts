plugins {
    `kotlin-dsl`
}

group = "com.foundation.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.androidx.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "foundation.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "foundation.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "foundation.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "foundation.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "foundation.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidFeature") {
            id = "foundation.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}
