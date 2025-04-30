package de.jhoopmann.stickywindow.awt.native

internal enum class NSApplicationActivationPolicy(val value: Int) {
    NSApplicationActivationPolicyRegular(0),
    NSApplicationActivationPolicyAccessory(1),
    NSApplicationActivationPolicyProhibited(2);

    companion object {
        fun fromValue(value: Int): NSApplicationActivationPolicy? {
            return entries.find { it.value == value }
        }
    }
}