package de.jhoopmann.topmostwindow.awt.native

internal enum class WMState(val value: String)
{
    SKIP_TASKBAR("_NET_WM_STATE_SKIP_TASKBAR"),
    STICKY("_NET_WM_STATE_STICKY")
}