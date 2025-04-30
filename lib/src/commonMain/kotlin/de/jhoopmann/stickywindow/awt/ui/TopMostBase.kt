package de.jhoopmann.stickywindow.awt.ui

import de.jhoopmann.stickywindow.awt.native.WindowHelper
import java.awt.EventQueue
import java.awt.Window
import kotlin.properties.Delegates

abstract class TopMostBase : TopMost {
    private var _initialized: Boolean = false
    override val initialized: Boolean
        get() = _initialized
    private var _options: TopMostOptions by Delegates.notNull()
    override val options: TopMostOptions
        get() = _options
    override var windowHandle: Long by Delegates.notNull()

    override fun initialize(
        window: Window,
        options: TopMostOptions,
        initializeParent: InitializeParentFunction,
        beforeInitialization: InitializationEvent,
        afterInitialization: InitializationEvent
    ) {
        EventQueue.invokeLater {
            _options = options
            window.isAlwaysOnTop = options.topMost

            beforeInitialization.invoke(this)

            initializeParent.invoke(this, window)

            afterInitialization.invoke(this)

            _initialized = true
        }
    }

    override fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit) {
        EventQueue.invokeLater {
            if (visible && !initialized) {
                throw TopMostWindowNotInitializedException()
            }

            setWindowOptionsBeforeVisibility(visible)

            parentSetVisible.invoke(visible)

            setWindowOptionsAfterVisibility(visible)
        }
    }

    override fun setWindowOptionsBeforeInit() {}

    override fun setWindowOptionsAfterInit() {}

    override fun findWindowHandle(window: Window): Long {
        return WindowHelper.instance.findWindowForComponent(window).takeIf { it > 0L }
            ?: throw WindowHandleException(window)
    }

    override fun setWindowOptionsBeforeVisibility(visible: Boolean) {}

    override fun setWindowOptionsAfterVisibility(visible: Boolean) {}
}
