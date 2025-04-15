package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

interface TopMost {
    fun setPlatformOptionsBeforeInit(options: TopMostOptions?)

    fun setPlatformOptionsAfterInit(options: TopMostOptions?)

    fun initialize(
        window: Window,
        options: TopMostOptions = TopMostOptions(),
        parentInitialize: (() -> Long?)? = null,
        beforeInitialization: InitializationEvent? = DefaultBeforeInitializationEvent,
        afterInitialization: InitializationEvent? = DefaultAfterInitializationEvent
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}
