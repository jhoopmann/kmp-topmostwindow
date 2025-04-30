package de.jhoopmann.stickywindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ApplicationHelper() {
    companion object {
        val instance: ApplicationHelper
    }

    fun setActivationPolicy(applicationActivationPolicy: Int): Boolean
    fun getActivationPolicy(): Int
}
