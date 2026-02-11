import com.android.build.api.dsl.CommonExtension
import com.foundation.buildconfig.Android
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroid(
    commonExtension: CommonExtension,
) {
    commonExtension.apply {
        compileSdk = Android.COMPILE_SDK

        defaultConfig.apply {
            minSdk = Android.MIN_SDK
        }

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    extensions.getByType<KotlinAndroidProjectExtension>().apply {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
