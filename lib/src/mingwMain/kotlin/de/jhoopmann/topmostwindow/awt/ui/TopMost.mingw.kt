package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WindowHelper

actual open class TopMostImpl() : TopMost, TopMostBase() {
    override fun setPlatformOptionsInit() {
        setWindowsOptionsInit()
    }

    override fun setPlatformOptionsAfterInit() {
    }

    override fun setPlatformOptionsBeforeInit() {
    }

    override fun setPlatformOptionsBeforeVisibility(visible: Boolean) {
    }

    override fun setPlatformOptionsAfterVisibility(visible: Boolean) {
    }

    protected open fun setWindowsOptionsInit() {
        windowHandle?.takeIf { it > 0L }?.let { handle ->
            with(WindowHelper.instance) {
                if (options!!.topMost) {
                    setWindowTopMost(handle)
                }

                if (options!!.sticky) {
                    setWindowSticky(handle)
                }
            }
        }
    }
}
