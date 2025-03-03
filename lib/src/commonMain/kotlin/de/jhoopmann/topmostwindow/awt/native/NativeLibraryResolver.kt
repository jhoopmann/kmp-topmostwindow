package de.jhoopmann.topmostwindow.awt.native

import java.io.File

class NativeLibraryResolver(private val name: String) {
    var path: String =
        this::class.java.getResourceAsStream("/native/$name").takeIf { it != null }?.use { input ->
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
