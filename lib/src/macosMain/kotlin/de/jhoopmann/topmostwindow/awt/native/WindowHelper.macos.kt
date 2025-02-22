package de.jhoopmann.topmostwindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WindowHelper {
    actual companion object {
        actual val instance: WindowHelper = WindowHelper()
    }

    init {
        System.load(NativeLibraryResolver("libwindow_helper.dylib").path)
    }

    actual external fun findWindowForName(windowName: String): Long

    actual external fun findLastWindow(): Long

    actual external fun setWindowLevel(windowHandle: Long, windowLevel: Int)

    actual external fun setWindowCollectionBehavior(windowHandle: Long, windowCollectionBehavior: Int)

    actual external fun getCGWindowLevelForKey(cGWindowLevelKeyValue: Int): Int

    actual fun addWindowMode(windowHandle: Long, windowMode: Array<String>) {
        throw NotImplementedError()
    }

    actual fun setWindowTopMost(windowHandle: Long) {
        throw NotImplementedError()
    }

    actual fun setWindowSticky(windowHandle: Long) {
        throw NotImplementedError()
    }
}