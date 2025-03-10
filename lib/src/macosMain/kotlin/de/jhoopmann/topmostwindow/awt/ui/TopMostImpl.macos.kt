package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.ApplicationHelper
import de.jhoopmann.topmostwindow.awt.native.WindowHelper
import de.jhoopmann.topmostwindow.awt.native.CGWindowLevelKey
import de.jhoopmann.topmostwindow.awt.native.NSApplicationActivationPolicy
import de.jhoopmann.topmostwindow.awt.native.NSWindowCollectionBehavior

actual open class TopMostCompanionImpl : TopMostCompanion, TopMostCompanionBase() {
    protected open val macOSState: MacOSState = MacOSState()

    actual override fun setPlatformOptionsBeforeInit(options: TopMostOptions?) {
        setMacOsOptionsBeforeInit(options!!)
    }

    actual override fun setPlatformOptionsAfterInit(options: TopMostOptions?) {
        setMacOsOptionsAfterInit(options!!)
    }

    protected fun setMacOsOptionsBeforeInit(options: TopMostOptions) {
        with(ApplicationHelper.instance) {
            macOSState.activationPolicy = NSApplicationActivationPolicy.fromValue(getActivationPolicy())
                ?: NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular

            if (options.sticky && macOSState.activationPolicy != NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory) {
                setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory.value)
            }
        }
    }

    protected fun setMacOsOptionsAfterInit(options: TopMostOptions) {
        with(ApplicationHelper.instance) {
            if (options.sticky && getActivationPolicy() != macOSState.activationPolicy.value) {
                setActivationPolicy(macOSState.activationPolicy.value)
            }
        }
    }

    protected data class MacOSState(
        var activationPolicy: NSApplicationActivationPolicy =
            NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular
    )
}

actual open class TopMostImpl : TopMost, TopMostBase() {
    actual companion object : TopMostCompanion, TopMostCompanionImpl()

    override fun setWindowOptionsInit() {
        setMacOsOptionsInit()
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
}
