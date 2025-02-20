package de.jhoopmann.topmostwindow.awt.native

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WindowHelper() {
    companion object {
        val instance: WindowHelper
    }

    // linux
    fun findWindowForName(windowName: String): Long
    fun addWindowMode(windowHandle: Long, windowMode: Array<String>)

    // windows
//    fun findLastWindow(): Long
    fun setWindowSticky(windowHandle: Long)
    fun setWindowTopMost(windowHandle: Long)

    // macos
    fun findLastWindow(): Long
    fun setWindowLevel(windowHandle: Long, windowLevel: Int)
    fun setWindowCollectionBehavior(windowHandle: Long, windowCollectionBehavior: Int)
    fun getCGWindowLevelForKey(cGWindowLevelKeyValue: Int): Int
}