package de.jhoopmann.stickywindow.awt.ui

import java.awt.Window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class TopMostImpl : TopMost {
    override val initialized: Boolean
    override val options: TopMostOptions
    override var windowHandle: Long

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        initializeParent: InitializeParentFunction,
        beforeInitialization: InitializationEvent,
        afterInitialization: InitializationEvent
    )

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)

    override fun setWindowOptionsBeforeInit()

    override fun setWindowOptionsAfterInit()

    override fun findWindowHandle(window: Window): Long

    override fun setWindowOptionsBeforeVisibility(visible: Boolean)

    override fun setWindowOptionsAfterVisibility(visible: Boolean)
}