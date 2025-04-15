package de.jhoopmann.topmostwindow.awt.ui

data class TopMostOptions(
    val topMost: Boolean = true,
    val sticky: Boolean = true,
    val skipTaskbar: Boolean = true,
)

typealias InitializationEvent = (TopMost, TopMostOptions) -> Unit
