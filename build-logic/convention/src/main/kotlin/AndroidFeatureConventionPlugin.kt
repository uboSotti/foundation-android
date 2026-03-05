import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Backward-compatible aggregate plugin.
            pluginManager.apply("foundation.android.feature.base")
            pluginManager.apply("foundation.android.feature.domain")
            pluginManager.apply("foundation.android.feature.ui")
            pluginManager.apply("foundation.android.feature.navigation3")
            pluginManager.apply("foundation.android.feature.lifecycle")
        }
    }
}
