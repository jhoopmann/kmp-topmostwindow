package de.jhoopmann.topmostwindow.awt.ui

import de.jhoopmann.topmostwindow.awt.native.WindowHelper
import java.awt.EventQueue
import java.awt.Window

open class TopMostBase : TopMost {
    protected open var options: TopMostOptions? = null
    protected open var windowHandle: Long? = null

    protected open fun setWindowOptionsBeforeVisibility(visible: Boolean) {}

    protected open fun setWindowOptionsAfterVisibility(visible: Boolean) {}

    protected open fun setWindowOptionsInit() {}

    protected open fun setWindowOptionsAfterInit() {}

    protected open fun setWindowOptionsBeforeInit() {}

    override fun setPlatformOptionsAfterInit(options: TopMostOptions?) {}

    override fun setPlatformOptionsBeforeInit(options: TopMostOptions?) {}

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        parentInitialize: (() -> Long)?,
        beforeInitialization: InitializationEvent?,
        afterInitialization: InitializationEvent?
    ) {
        EventQueue.invokeLater {
            beforeInitialization?.invoke(this, options)

            this.options = options
            window.isAlwaysOnTop = options.topMost

            setWindowOptionsBeforeInit()

            windowHandle = parentInitialize?.invoke()?.takeIf { it > 0L }
                ?: run {
                    window.addNotify()

                    findWindowHandle(window)
                }

            setWindowOptionsInit()

            setWindowOptionsAfterInit()

            afterInitialization?.invoke(this, options)
        }
    }

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit) {
        EventQueue.invokeLater {
            if (options == null) {
                throw MissingInitializingOptionsException()
            }

            setWindowOptionsBeforeVisibility(visible)

            parentSetVisible.invoke(visible)

            setWindowOptionsAfterVisibility(visible)
        }
    }

    protected open fun findWindowHandle(window: Window): Long? {
        return with(WindowHelper.instance) {
            findWindowForComponent(window) // implemented on each platform
        }.takeIf { it > 0L } ?: run {
            println("Failed to get window handle for $this:${window.name}")

            null
        }
    }
}

class MissingInitializingOptionsException() : Exception("Missing TopMost initialization options")
