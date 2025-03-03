package buildtasks

import org.gradle.process.*
import org.gradle.api.tasks.*
import org.gradle.api.provider.*
import org.gradle.api.*
import javax.inject.*
import org.gradle.api.file.*
import org.gradle.api.tasks.bundling.Jar

abstract class CopyNativeLibraryTask @Inject constructor(
    private val projectLayout: ProjectLayout,
    private val fileSystem: FileSystemOperations,
    private val providerFactory: ProviderFactory,
) : DefaultTask() {
    @TaskAction
    fun action() {
        val targetJvmPlatform = providerFactory.gradleProperty("targetJvmPlatform").getOrNull() ?: "macos"

        val projectNativeLibDir = projectLayout.projectDirectory.dir("src/${targetJvmPlatform}Main/native/build")
        val buildNativeLibDir = projectLayout.buildDirectory.dir("native")

        fileSystem.copy {
            from(projectNativeLibDir)
            into(buildNativeLibDir)
        }
    }
}