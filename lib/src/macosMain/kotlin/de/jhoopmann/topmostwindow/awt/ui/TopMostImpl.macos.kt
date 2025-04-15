package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.*

actual open class TopMostImpl : TopMost, TopMostBase() {
    protected open val macOSState: MacOSState = MacOSState()

    actual override fun setPlatformOptionsBeforeInit(options: TopMostOptions?) {
        setMacOsOptionsBeforeInit(options!!)
    }

    actual override fun setPlatformOptionsAfterInit(options: TopMostOptions?) {
        setMacOsOptionsAfterInit(options!!)
    }

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
