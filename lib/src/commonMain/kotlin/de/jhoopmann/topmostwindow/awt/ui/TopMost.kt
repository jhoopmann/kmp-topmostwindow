package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window
import kotlin.reflect.full.companionObjectInstance

interface TopMostCompanion {
    fun setPlatformOptionsBeforeInit(options: TopMostOptions?) {}
    fun setPlatformOptionsAfterInit(options: TopMostOptions?) {}
}

interface TopMost {
    companion object : TopMostCompanion

    fun initialize(
        window: Window,
        options: TopMostOptions = TopMostOptions(),
        parentInitialize: (() -> Long?)? = null,
        beforeInitialization: ((TopMost, TopMostOptions) -> Unit)? = { topMost, options ->
            (topMost::class.companionObjectInstance as TopMostCompanionBase).setPlatformOptionsBeforeInit(options)
        },
        afterInitialization: ((TopMost, TopMostOptions) -> Unit)? = { topMost, options ->
            (topMost::class.companionObjectInstance as TopMostCompanionBase).setPlatformOptionsAfterInit(options)
        }
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}
