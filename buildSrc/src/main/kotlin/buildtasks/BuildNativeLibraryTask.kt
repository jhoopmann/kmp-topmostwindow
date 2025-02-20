package buildtasks

import org.gradle.process.*
import org.gradle.api.tasks.*
import org.gradle.api.*
import org.gradle.api.provider.*
import javax.inject.*
import org.gradle.api.file.*

abstract class BuildNativeLibraryTask @Inject constructor(
    private val execOperations: ExecOperations,
    private val projectLayout: ProjectLayout,
    private val providerFactory: ProviderFactory,
) : DefaultTask() {

    @TaskAction
    fun action() {
        val targetJvmPlatform = providerFactory.gradleProperty("targetJvmPlatform").getOrNull() ?: "macos"
        val projectDir = projectLayout.projectDirectory

        execOperations.exec {
            workingDir(projectDir.dir("src/${targetJvmPlatform}Main/native"))

            if (targetJvmPlatform == "mingw") {
                commandLine("cmd", "/c", "build.bat")
            } else {
                commandLine("sh", "build.sh")
            }
        }.assertNormalExitValue()
    }
}