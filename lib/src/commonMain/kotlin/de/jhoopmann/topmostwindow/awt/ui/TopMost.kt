package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

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
            companion.setPlatformOptionsBeforeInit(options)
        },
        afterInitialization: ((TopMost, TopMostCompanion, TopMostOptions) -> Unit)? = { topMost, companion, options ->
            companion.setPlatformOptionsAfterInit(options)
        }
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}
