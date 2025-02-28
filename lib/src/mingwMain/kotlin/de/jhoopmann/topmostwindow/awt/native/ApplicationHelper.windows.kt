package de.jhoopmann.topmostwindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ApplicationHelper {
    actual companion object {
        actual val instance: ApplicationHelper = ApplicationHelper()
    }

    init {
        throw NotImplementedError()
    }

    actual fun isActive(): Boolean {
        throw NotImplementedError()
    }

    actual fun activate() {
        throw NotImplementedError()
    }

    actual fun deactivate() {
        throw NotImplementedError()
    }

    actual fun setActivationPolicy(applicationActivationPolicy: Int) {
        throw NotImplementedError()
    }

    actual fun getActivationPolicy(): Int {
        throw NotImplementedError()
    }
}
