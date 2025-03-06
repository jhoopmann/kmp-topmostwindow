package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.ApplicationHelper
import de.jhoopmann.topmostwindow.awt.native.WindowHelper
import de.jhoopmann.topmostwindow.awt.native.CGWindowLevelKey
import de.jhoopmann.topmostwindow.awt.native.NSApplicationActivationPolicy
import de.jhoopmann.topmostwindow.awt.native.NSWindowCollectionBehavior

actual open class TopMostImpl : TopMost, TopMostBase() {
    protected open val macOSState: MacOSState = MacOSState()

    override fun setPlatformOptionsBeforeInit() {
        setMacOsOptionsBeforeInit()
    }

    override fun setPlatformOptionsInit() {
        setMacOsOptionsInit()
    }

    override fun setPlatformOptionsAfterInit() {
        setMacOsOptionsAfterInit()
    }

    override fun setPlatformOptionsBeforeVisibility(visible: Boolean) {
    }

    override fun setPlatformOptionsAfterVisibility(visible: Boolean) {
    }

    protected open fun setMacOsOptionsBeforeInit() {
        with(ApplicationHelper.instance) {
            macOSState.activationPolicy = NSApplicationActivationPolicy.fromValue(getActivationPolicy())
                ?: NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular

            if (options!!.sticky && macOSState.activationPolicy != NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory) {
                setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory.value)
            }
        }
    }

    protected open fun setMacOsOptionsInit() {
        windowHandle?.takeIf { it > 0L }?.let { handle ->
            with(WindowHelper.instance) {
                if (options!!.topMost) {
                    setWindowLevel(
                        handle,
                        getCGWindowLevelForKey(CGWindowLevelKey.kCGAssistiveTechHighWindowLevelKey.value)
                    )
                }

                if (options!!.sticky) {
                    setWindowCollectionBehavior(
                        handle,
                        NSWindowCollectionBehavior.NSWindowCollectionBehaviorCanJoinAllSpaces.value or
                                NSWindowCollectionBehavior.NSWindowCollectionBehaviorFullScreenAuxiliary.value or
                                NSWindowCollectionBehavior.NSWindowCollectionBehaviorStationary.value
                    )
                }
            }
        }
    }

    protected open fun setMacOsOptionsAfterInit() {
        with(ApplicationHelper.instance) {
            if (options!!.sticky && getActivationPolicy() != macOSState.activationPolicy.value) {
                setActivationPolicy(macOSState.activationPolicy.value)
            }
        }
    }

    protected data class MacOSState(
        var activationPolicy: NSApplicationActivationPolicy =
            NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular
    )
}
