package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

data class TopMostOptions(
    val topMost: Boolean = true,
    val sticky: Boolean = true,
    val skipTaskbar: Boolean = true,
)

typealias InitializeParentFunction = TopMost.(Window) -> Unit
typealias InitializationEvent = TopMost.() -> Unit
