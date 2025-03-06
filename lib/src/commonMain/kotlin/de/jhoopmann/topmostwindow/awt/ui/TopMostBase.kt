package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WindowHelper
import java.awt.EventQueue
import java.awt.Window

abstract class TopMostBase: TopMost {
    protected open var options: TopMostOptions? = null
    protected open var windowHandle: Long? = null

    abstract protected fun setPlatformOptionsBeforeVisibility(visible: Boolean)

    abstract protected fun setPlatformOptionsAfterVisibility(visible: Boolean)

    abstract protected fun setPlatformOptionsInit()

    abstract protected fun setPlatformOptionsAfterInit()

    abstract protected fun setPlatformOptionsBeforeInit()

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        parentInitialize: (() -> Long?)?,
        initialized: ((Long?) -> Unit)?,
    ) {
        window.apply {
            name = options.name
            isAlwaysOnTop = options.topMost
        }

        this.options = options

        EventQueue.invokeLater {
            setPlatformOptionsBeforeInit()
        }

        EventQueue.invokeLater {
            windowHandle = parentInitialize?.invoke()?.takeIf { it > 0L }
                ?: run {
                    window.addNotify()

                    findPlatformWindowHandle(window)
                }

            setPlatformOptionsInit()
        }

        EventQueue.invokeLater {
            setPlatformOptionsAfterInit()

            initialized?.invoke(windowHandle)
        }
    }

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit) {
        if (options == null) {
            throw MissingInitializingOptionsException()
        }

        EventQueue.invokeLater {
            setPlatformOptionsBeforeVisibility(visible)
        }

        EventQueue.invokeLater {
            parentSetVisible.invoke(visible)
        }

        EventQueue.invokeLater {
            setPlatformOptionsAfterVisibility(visible)
        }
    }

    protected open fun findPlatformWindowHandle(window: Window): Long? {
        return with(WindowHelper.instance) {
            findWindowForComponent(window) // implemented on each platform
        }.takeIf { it > 0L } ?: run {
            println("Failed to get window handle for $this:${window.name}")

            null
        }
    }
}

class MissingInitializingOptionsException() : Exception("Missing TopMost initialization options")
