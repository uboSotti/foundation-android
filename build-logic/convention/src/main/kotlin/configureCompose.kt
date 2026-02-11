import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCompose(
    commonExtension: CommonExtension,
) {
    commonExtension.buildFeatures.apply {
        compose = true
    }

    val libs = catalog()

    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.findLibrary("androidx.ui").get())
        add("implementation", libs.findLibrary("androidx.ui.graphics").get())
        add("implementation", libs.findLibrary("androidx.ui.tooling.preview").get())
        add("implementation", libs.findLibrary("androidx.material3").get())
        add("debugImplementation", libs.findLibrary("androidx.ui.tooling").get())
        add("debugImplementation", libs.findLibrary("androidx.ui.test.manifest").get())
    }
}
