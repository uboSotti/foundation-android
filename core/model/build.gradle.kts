plugins {
    id("foundation.android.library")
    id("foundation.kotlin.serialization")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.foundation.core.model"
}

dependencies {
    // Pure Kotlin models mainly
}
