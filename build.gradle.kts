plugins {
    // Android
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    // Kotlin
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false

    // 3rd party
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
}

val moduleBuildFiles = rootDir
    .walkTopDown()
    .filter { file ->
        file.isFile &&
            file.name == "build.gradle.kts" &&
            !file.invariantSeparatorsPath.contains("/build/") &&
            !file.invariantSeparatorsPath.contains("/.gradle/") &&
            !file.invariantSeparatorsPath.contains("/.claude/")
    }
    .toList()

fun File.toModulePath(): String {
    val rel = relativeTo(rootDir).invariantSeparatorsPath
    if (rel == "build.gradle.kts") return ":"
    return ":" + rel.removeSuffix("/build.gradle.kts").replace("/", ":")
}

fun String.lineNumberAt(index: Int): Int = take(index).count { it == '\n' } + 1

tasks.register("checkModulePluginUsage") {
    group = "verification"
    description = "Checks convention-plugin usage by module role."

    doLast {
        val errors = mutableListOf<String>()

        moduleBuildFiles.forEach { file ->
            val rel = file.relativeTo(rootDir).invariantSeparatorsPath
            val content = file.readText()

            if (rel.matches(Regex("""feature/[^/]+/impl/build\.gradle\.kts"""))) {
                val required = listOf(
                    "alias(libs.plugins.foundation.android.feature.base)",
                    "alias(libs.plugins.foundation.android.feature.ui)",
                    "alias(libs.plugins.foundation.android.feature.navigation3)",
                    "alias(libs.plugins.foundation.android.feature.lifecycle)",
                )
                required.forEach { plugin ->
                    if (!content.contains(plugin)) {
                        errors += "$rel: missing plugin `$plugin`"
                    }
                }

                if (content.contains("alias(libs.plugins.foundation.android.feature)")) {
                    errors += "$rel: use split feature plugins, not aggregate `foundation.android.feature`"
                }
            }

            if (rel == "core/testing/build.gradle.kts") {
                if (!content.contains("alias(libs.plugins.foundation.android.compose.base)")) {
                    errors += "$rel: must use `foundation.android.compose.base`"
                }
                if (content.contains("alias(libs.plugins.foundation.android.compose.ui)")) {
                    errors += "$rel: must not use `foundation.android.compose.ui`"
                }
            }

            if (rel == "core/model/build.gradle.kts") {
                if (!content.contains("alias(libs.plugins.foundation.kotlin.serialization.plugin)")) {
                    errors += "$rel: must use `foundation.kotlin.serialization.plugin`"
                }
                if (content.contains("alias(libs.plugins.foundation.kotlin.serialization.json)")) {
                    errors += "$rel: must not use `foundation.kotlin.serialization.json`"
                }
            }

            if (rel == "core/network/build.gradle.kts") {
                if (!content.contains("alias(libs.plugins.foundation.kotlin.serialization.json)")) {
                    errors += "$rel: must use `foundation.kotlin.serialization.json`"
                }
            }
        }

        if (errors.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Module plugin usage check failed:")
                    errors.forEach { appendLine(" - $it") }
                }
            )
        }
    }
}

tasks.register("checkDependencyBoundaries") {
    group = "verification"
    description = "Checks module dependency boundaries from Gradle scripts."

    doLast {
        val errors = mutableListOf<String>()

        moduleBuildFiles.forEach { file ->
            val rel = file.relativeTo(rootDir).invariantSeparatorsPath
            val modulePath = file.toModulePath()
            val content = file.readText()
            val projectDepRegex = Regex("""project\("(:[^"]+)"\)""")
            val projectDeps = projectDepRegex.findAll(content).map { it.groupValues[1] }.toList()

            if (modulePath.startsWith(":feature:")) {
                val moduleSegments = modulePath.split(":").filter { it.isNotBlank() }
                val moduleFeature = moduleSegments.getOrNull(1)

                projectDeps
                    .filter { it.startsWith(":feature:") }
                    .forEach { dep ->
                        val depFeature = dep.split(":").filter { it.isNotBlank() }.getOrNull(1)
                        if (moduleFeature != null && depFeature != moduleFeature) {
                            errors += "$rel: cross-feature dependency is forbidden ($modulePath -> $dep)"
                        }
                    }
            }

            if (modulePath == ":core:domain") {
                val forbidden = setOf(":core:data", ":core:network", ":core:database")
                projectDeps.filter { it in forbidden }.forEach { dep ->
                    errors += "$rel: forbidden dependency for core-domain ($modulePath -> $dep)"
                }
            }

            if (modulePath.matches(Regex(""":feature:[^:]+:api"""))) {
                val forbiddenLibs = listOf(
                    "libs.androidx.compose.",
                    "libs.androidx.hilt.navigation.compose",
                    "libs.androidx.navigation3.ui",
                    "libs.androidx.compose.material3",
                )
                forbiddenLibs.forEach { marker ->
                    if (content.contains(marker)) {
                        errors += "$rel: feature api must not depend on UI/runtime lib `$marker*`"
                    }
                }
            }
        }

        if (errors.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Dependency boundary check failed:")
                    errors.forEach { appendLine(" - $it") }
                }
            )
        }
    }
}

tasks.register("checkCatalogOnlyVersions") {
    group = "verification"
    description = "Fails if module scripts use hardcoded dependency versions."

    doLast {
        val errors = mutableListOf<String>()
        val dependencyWithVersionRegex = Regex("""['"][A-Za-z0-9_.-]+:[A-Za-z0-9_.-]+:[^'"]+['"]""")

        moduleBuildFiles
            .filter { it.relativeTo(rootDir).invariantSeparatorsPath != "build.gradle.kts" }
            .forEach { file ->
                val rel = file.relativeTo(rootDir).invariantSeparatorsPath
                val content = file.readText()

                dependencyWithVersionRegex.findAll(content).forEach { match ->
                    val line = content.lineNumberAt(match.range.first)
                    errors += "$rel:$line: hardcoded dependency version `${match.value}`"
                }
            }

        if (errors.isNotEmpty()) {
            throw GradleException(
                buildString {
                    appendLine("Catalog-only version check failed:")
                    errors.forEach { appendLine(" - $it") }
                }
            )
        }
    }
}

tasks.register("verifyArchitectureConventions") {
    group = "verification"
    description = "Runs all custom architecture/convention checks."
    dependsOn(
        "checkModulePluginUsage",
        "checkDependencyBoundaries",
        "checkCatalogOnlyVersions",
    )
}
