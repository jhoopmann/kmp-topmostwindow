package de.jhoopmann.topmostwindow.awt.ui

import java.awt.Window

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect open class TopMostImpl : TopMost {

    override fun setPlatformOptionsBeforeInit(options: TopMostOptions?)

    override fun setPlatformOptionsAfterInit(options: TopMostOptions?)

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        parentInitialize: (() -> Long?)?,
        beforeInitialization: ((TopMost, TopMostOptions) -> Unit)?,
        afterInitialization: ((TopMost, TopMostOptions) -> Unit)?
    )

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}