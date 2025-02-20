package de.jhoopmann.topmostwindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ApplicationHelper() {
    companion object {
        val instance: ApplicationHelper
    }

    fun isActive(): Boolean
    fun activate()
    fun deactivate()
    fun setActivationPolicy(applicationActivationPolicy: Int)
    fun getActivationPolicy(): Int
}
