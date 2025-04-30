package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WindowHelper

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class TopMostImpl() : TopMost, TopMostBase() {
    actual override fun setWindowOptionsAfterVisibility(visible: Boolean) {
        if (visible) {
            with(WindowHelper.instance) {
                if (options.topMost) {
                    setWindowTopMost(windowHandle)
                }

                if (options.sticky) {
                    setWindowSticky(windowHandle)
                }
            }
        }
    }
}
