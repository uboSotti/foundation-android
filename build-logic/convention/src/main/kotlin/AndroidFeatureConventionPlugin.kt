import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("foundation.android.library")
            pluginManager.apply("foundation.android.hilt")
            pluginManager.apply("foundation.android.compose")

            val libs = catalog()

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:common"))

                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}
