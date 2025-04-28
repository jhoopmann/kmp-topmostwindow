package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.ApplicationHelper
import de.jhoopmann.topmostwindow.awt.native.NSApplicationActivationPolicy

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TopMostImpl : TopMost, TopMostBase() {
    private val macOSState: MacOSState = MacOSState()

    actual override fun setWindowOptionsBeforeInit() {
        with(ApplicationHelper.instance) {
            macOSState.activationPolicy = NSApplicationActivationPolicy.fromValue(getActivationPolicy())
                ?: NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular

            if (options.sticky && macOSState.activationPolicy != NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory) {
                setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory.value)
            }
        }
    }

    actual override fun setWindowOptionsAfterInit() {
        with(ApplicationHelper.instance) {
            if (options.sticky && getActivationPolicy() != macOSState.activationPolicy.value) {
                setActivationPolicy(macOSState.activationPolicy.value)
            }
        }
    }

    private data class MacOSState(
        var activationPolicy: NSApplicationActivationPolicy =
            NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular
    )
}
