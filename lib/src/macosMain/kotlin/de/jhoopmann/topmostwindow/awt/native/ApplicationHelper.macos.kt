package de.jhoopmann.topmostwindow.awt.native

private val LibraryPath: String = "libapplication_helper.dylib"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ApplicationHelper {
    actual companion object {
        actual val instance: ApplicationHelper = ApplicationHelper()
    }

    init {
        System.load(NativeLibraryResolver(LibraryPath).path)
    }

    actual external fun isActive(): Boolean
    actual external fun activate()
    actual external fun deactivate()
    actual external fun setActivationPolicy(applicationActivationPolicy: Int)
    actual external fun getActivationPolicy(): Int
}
