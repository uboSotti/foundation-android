import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureNavigation3ConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = catalog()

            dependencies {
                add("implementation", project(":core:navigation"))

                add("implementation", libs.findLibrary("androidx.navigation3.runtime").get())
                add("implementation", libs.findLibrary("androidx.navigation3.ui").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
            }
        }
    }
}
