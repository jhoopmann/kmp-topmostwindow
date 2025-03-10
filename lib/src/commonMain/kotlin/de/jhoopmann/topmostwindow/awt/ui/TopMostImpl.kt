package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

expect open class TopMostCompanionImpl : TopMostCompanion {
    override fun setPlatformOptionsBeforeInit(options: TopMostOptions?)

    override fun setPlatformOptionsAfterInit(options: TopMostOptions?)
}

expect open class TopMostImpl : TopMost {
    companion object : TopMostCompanion

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        parentInitialize: (() -> Long?)?,
        onInitialized: (() -> Unit)?
    )

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}