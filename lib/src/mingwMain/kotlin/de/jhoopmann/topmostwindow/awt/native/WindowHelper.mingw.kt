package de.jhoopmann.topmostwindow.awt.native

import java.awt.Component

val LibraryPath: String = "libwindow_helper.dll"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WindowHelper {
    actual companion object {
        actual val instance: WindowHelper = WindowHelper()
    }

    init {
        System.load(NativeLibraryResolver(LibraryPath).path)
    }

    actual external fun setWindowTopMost(windowHandle: Long)

    actual external fun setWindowSticky(windowHandle: Long)

    actual external fun findWindowForComponent(component: Component): Long

    actual fun addWindowMode(windowHandle: Long, windowMode: Array<String>) {
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