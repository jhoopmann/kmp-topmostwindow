package de.jhoopmann.topmostwindow.awt.native

import java.awt.Component

private const val LibraryPath: String = "libwindow_helper.dylib"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class WindowHelper {
    actual companion object {
        actual val instance: WindowHelper = WindowHelper()
    }

    init {
        System.load(NativeLibraryResolver(LibraryPath).path)
    }

    actual external fun findWindowForComponent(component: Component): Long

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