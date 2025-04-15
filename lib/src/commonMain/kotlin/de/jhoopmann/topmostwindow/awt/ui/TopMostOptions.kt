package de.jhoopmann.topmostwindow.awt.ui

import kotlin.random.Random
import kotlin.random.nextUInt

/**
 * topMost (Natively sets Window above all other Windows)
 * sticky (Natively sets Window to appear on all Spaces)
 * skipTaskbar (Natively hides Window from taskbar):
 *  has no effect on macOS because non mainWindows never appear in Dock anyway, use sticky.
 *  has no effect on windows because non toolbox window without parent always appear in taskbar, use sticky.
 */
data class TopMostOptions(
    val topMost: Boolean = true,
    val sticky: Boolean = true,
    val skipTaskbar: Boolean = true,
)
