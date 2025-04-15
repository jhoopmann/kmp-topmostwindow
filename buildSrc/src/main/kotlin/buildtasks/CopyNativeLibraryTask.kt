package buildtasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class CopyNativeLibraryTask @Inject constructor(
    private val projectLayout: ProjectLayout,
    private val fileSystem: FileSystemOperations,
    private val providerFactory: ProviderFactory,
) : DefaultTask() {
    @TaskAction
    fun action() {
        val targetJvmPlatform: String = providerFactory.gradleProperty("targetJvmPlatform").getOrNull() ?: "macos"

        val projectNativeLibDir: Directory = projectLayout.projectDirectory.dir("src/${targetJvmPlatform}Main/native/build")
        val buildNativeLibDir: Provider<Directory> = projectLayout.buildDirectory.dir("native")

        fileSystem.copy {
            from(projectNativeLibDir)
            into(buildNativeLibDir)
        }
    }
}