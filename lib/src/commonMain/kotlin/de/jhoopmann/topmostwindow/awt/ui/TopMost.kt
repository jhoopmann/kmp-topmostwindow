package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window
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
    val name: String = Random.nextUInt().toString(),
    val topMost: Boolean = true,
    val sticky: Boolean = true,
    val skipTaskbar: Boolean = true,
)

interface TopMostCompanion {
    fun setPlatformOptionsBeforeInit(options: TopMostOptions?)
    fun setPlatformOptionsAfterInit(options: TopMostOptions?)
}

interface TopMost {
    fun initialize(
        window: Window,
        options: TopMostOptions = TopMostOptions(),
        parentInitialize: (() -> Long?)? = null,
        beforeInitialization: ((TopMost, TopMostCompanion, TopMostOptions) -> Unit)? = { topMost, companion, options ->
            defaultBeforeInitialization(companion, options)
        },
        afterInitialization: ((TopMost, TopMostCompanion, TopMostOptions) -> Unit)? = { topMost, companion, options ->
            defaultAfterInitialization(companion, options)
        }
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}

fun defaultBeforeInitialization(companion: TopMostCompanion, options: TopMostOptions) {
    companion.setPlatformOptionsBeforeInit(options)
}

fun defaultAfterInitialization(companion: TopMostCompanion, options: TopMostOptions) {
    companion.setPlatformOptionsAfterInit(options)
}
