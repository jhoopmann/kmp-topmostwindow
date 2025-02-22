package de.jhoopmann.topmostwindow.awt.native

enum class Platform {
    Mac,
    Windows,
    Linux
}

expect val platform: Platform
