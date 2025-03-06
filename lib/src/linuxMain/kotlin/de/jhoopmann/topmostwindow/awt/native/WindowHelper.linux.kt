package de.jhoopmann.topmostwindow.awt.native

import java.awt.Component

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WindowHelper {
    actual companion object {
        actual val instance: WindowHelper = WindowHelper()
    }

    init {
        System.load(NativeLibraryResolver("libwindow_helper.so").path)
    }

    actual external fun addWindowMode(windowHandle: Long, windowMode: Array<String>)

    actual external fun findWindowForComponent(component: Component): Long

    actual fun setWindowSticky(windowHandle: Long) {
        throw NotImplementedError()
    }

    actual fun setWindowTopMost(windowHandle: Long) {
        throw NotImplementedError()
    }

    actual fun setWindowLevel(windowHandle: Long, windowLevel: Int) {
        throw NotImplementedError()
    }

    actual fun setWindowCollectionBehavior(windowHandle: Long, windowCollectionBehavior: Int) {
        throw NotImplementedError()
    }

    actual fun getCGWindowLevelForKey(cGWindowLevelKeyValue: Int): Int {
        throw NotImplementedError()
    }
}