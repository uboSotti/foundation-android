import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidComposeUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("foundation.android.compose.base")
            configureComposeUi()
        }
    }
}
