package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

interface TopMost {
    val initialized: Boolean
    val options: TopMostOptions
    var windowHandle: Long

    fun initialize(
        window: Window,
        options: TopMostOptions = TopMostOptions(),
        initializeParent: InitializeParentFunction = DefaultInitializeParent,
        beforeInitialization: InitializationEvent = DefaultBeforeInitialization,
        afterInitialization: InitializationEvent = DefaultAfterInitialization
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)

    fun setWindowOptionsBeforeInit()

    fun setWindowOptionsAfterInit()

    fun findWindowHandle(window: Window): Long
}
