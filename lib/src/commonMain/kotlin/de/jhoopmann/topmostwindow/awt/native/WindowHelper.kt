package de.jhoopmann.topmostwindow.awt.native

import java.awt.Component

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WindowHelper() {
    companion object {
        val instance: WindowHelper
    }

    // common
    fun findWindowForComponent(component: Component): Long

    // linux
    fun addWindowMode(windowHandle: Long, windowMode: Array<String>)

    // windows
    fun setWindowSticky(windowHandle: Long)
    fun setWindowTopMost(windowHandle: Long)

    // macos
    fun setWindowLevel(windowHandle: Long, windowLevel: Int)
    fun setWindowCollectionBehavior(windowHandle: Long, windowCollectionBehavior: Int)
    fun getCGWindowLevelForKey(cGWindowLevelKeyValue: Int): Int
}