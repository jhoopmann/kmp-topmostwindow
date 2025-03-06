package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WMState
import de.jhoopmann.topmostwindow.awt.native.WindowHelper

actual open class TopMostImpl : TopMost, TopMostBase() {
    override fun setPlatformOptionsInit() {

    }

    override fun setPlatformOptionsAfterInit() {

    }

    override fun setPlatformOptionsBeforeInit() {

    }

    override fun setPlatformOptionsBeforeVisibility(visible: Boolean) {

    }

    override fun setPlatformOptionsAfterVisibility(visible: Boolean) {
        setLinuxOptionsAfterVisibility(visible)
    }

    protected open fun setLinuxOptionsAfterVisibility(visible: Boolean) {
        val states: MutableList<WMState> = mutableListOf()
        if (options!!.skipTaskbar) {
            states.add(WMState.SKIP_TASKBAR)
        }
        if (options!!.sticky) {
            states.add(WMState.STICKY)
        }

        states.takeIf { it.size > 0 }?.map { it.value }?.let { stateValues ->
            windowHandle?.takeIf { it > 0L && visible }?.let { handle ->
                with(WindowHelper.instance) {
                    addWindowMode(
                        handle,
                        stateValues.toTypedArray()
                    )
                }
            }
        }
    }
}
