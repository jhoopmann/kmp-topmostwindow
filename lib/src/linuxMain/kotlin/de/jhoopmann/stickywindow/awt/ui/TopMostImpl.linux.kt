package de.jhoopmann.stickywindow.awt.ui

import de.jhoopmann.stickywindow.awt.native.WMState
import de.jhoopmann.stickywindow.awt.native.WindowHelper

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual open class TopMostImpl : TopMost, TopMostBase() {
    actual override fun setWindowOptionsAfterVisibility(visible: Boolean) {
        val states: MutableList<WMState> = mutableListOf()
        if (options.skipTaskbar) {
            states.add(WMState.SKIP_TASKBAR)
        }
        if (options.sticky) {
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
