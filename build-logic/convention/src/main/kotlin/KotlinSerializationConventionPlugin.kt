import org.gradle.api.Plugin
import org.gradle.api.Project

class KotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Backward-compatible aggregate plugin.
            pluginManager.apply("foundation.kotlin.serialization.json")
        }
    }
}
