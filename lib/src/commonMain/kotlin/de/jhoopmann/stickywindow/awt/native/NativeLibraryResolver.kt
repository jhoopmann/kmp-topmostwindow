package de.jhoopmann.stickywindow.awt.native

import java.io.File

private const val LibraryDirectory: String = "/native"

internal class NativeLibraryResolver(private val name: String) {
    val path: String = this::class.java.getResourceAsStream("$LibraryDirectory/$name").takeIf { it != null }?.use { input ->
        File.createTempFile(name, "w+").run {
            outputStream().use { output ->
                output.write(input.readAllBytes())
            }

            setReadable(true)
            setExecutable(true)
            setWritable(true)

            this
        }.absolutePath
    } ?: throw RuntimeException("Library $name not found in package.")
}
