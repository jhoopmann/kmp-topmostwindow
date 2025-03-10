package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WindowHelper

actual open class TopMostCompanionImpl : TopMostCompanion, TopMostCompanionBase()

actual open class TopMostImpl() : TopMost, TopMostBase() {
    actual companion object : TopMostCompanion, TopMostCompanionImpl()

    override fun setWindowOptionsInit() {
        setWindowsOptionsInit()
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
