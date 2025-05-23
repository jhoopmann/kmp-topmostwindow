package de.jhoopmann.stickywindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ApplicationHelper {
    actual companion object {
        actual val instance: ApplicationHelper = ApplicationHelper()
    }

    init {
        throw NotImplementedError()
    }

    actual fun setActivationPolicy(applicationActivationPolicy: Int): Boolean {
        throw NotImplementedError()
    }

    actual fun getActivationPolicy(): Int {
        throw NotImplementedError()
    }
}
