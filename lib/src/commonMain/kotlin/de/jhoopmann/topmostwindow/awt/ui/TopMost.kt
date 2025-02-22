package de.jhoopmann.topmostwindow.awt.ui

import java.awt.EventQueue
import java.awt.Window
import kotlin.random.Random
import kotlin.random.nextUInt
import de.jhoopmann.topmostwindow.awt.native.ApplicationHelper
import de.jhoopmann.topmostwindow.awt.native.Platform
import de.jhoopmann.topmostwindow.awt.native.WindowHelper
import de.jhoopmann.topmostwindow.awt.native.linux.WMState
import de.jhoopmann.topmostwindow.awt.native.macos.CGWindowLevelKey
import de.jhoopmann.topmostwindow.awt.native.macos.NSApplicationActivationPolicy
import de.jhoopmann.topmostwindow.awt.native.macos.NSWindowCollectionBehavior
import de.jhoopmann.topmostwindow.awt.native.platform

/**
 * topMost (Natively sets Window above all other Windows)
 * sticky (Natively sets Window to appear on all Spaces)
 * skipTaskbar (Natively hides Window from taskbar):
 *  has no effect on macOS because non mainWindows never appear in Dock anyway, use sticky.
 *  has no effect on windows because non toolbox window without parent always appear in taskbar, use sticky.
 */
data class TopMostOptions(
    val name: String = Random.nextUInt().toString(),
    val topMost: Boolean = true,
    val sticky: Boolean = true,
    val skipTaskbar: Boolean = true,
)

interface TopMost {
    fun initialize(
        window: Window,
        options: TopMostOptions = TopMostOptions(),
        parentInitialize: (() -> Long?)? = null,
        initialized: ((Long?) -> Unit)? = null,
    )

    fun setVisible(visible: Boolean, parentSetVisible: (Boolean) -> Unit)
}

open class TopMostImpl() : TopMost {
    protected open var options: TopMostOptions? = null
    protected open var windowHandle: Long? = null

    protected val macOS: MacOSSpecific = MacOSSpecific()

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

    protected open fun setPlatformOptionsInit() {
        if (platform == Platform.Mac) {
            setMacOsOptionsInit()
        } else if (platform == Platform.Windows) {
            setWindowsOptionsInit()
        }
    }

    protected open fun setPlatformOptionsAfterInit() {
        if (platform == Platform.Mac) {
            setMacOsOptionsAfterInit()
        }
    }

    protected open fun setPlatformOptionsBeforeInit() {
        if (platform == Platform.Mac) {
            setMacOsOptionsBeforeInit()
        }
    }

    protected open fun setPlatformOptionsBeforeVisibility(visible: Boolean) {
        if (platform == Platform.Mac) {
            setMacOsOptionsBeforeVisibility(visible)
        }
    }

    protected open fun setPlatformOptionsAfterVisibility(visible: Boolean) {
        if (platform == Platform.Mac) {
            setMacOsOptionsAfterVisibility()
        } else if (platform == Platform.Linux) {
            setLinuxOptionsAfterVisibility(visible)
        }
    }


    protected open fun setWindowsOptionsInit() {
        windowHandle?.takeIf { it > 0L }?.let { handle ->
            with(WindowHelper.instance) {
                if (options!!.topMost) {
                    setWindowTopMost(handle)
                }

                if (options!!.sticky) {
                    setWindowSticky(handle)
                }
            }
        }
    }

    protected open fun setMacOsOptionsInit() {
        windowHandle?.takeIf { it > 0L }?.let { handle ->
            with(WindowHelper.instance) {
                if (options!!.topMost) {
                    setWindowLevel(
                        handle,
                        getCGWindowLevelForKey(CGWindowLevelKey.kCGAssistiveTechHighWindowLevelKey.value)
                    )
                }

                if (options!!.sticky) {
                    setWindowCollectionBehavior(
                        handle,
                        NSWindowCollectionBehavior.NSWindowCollectionBehaviorCanJoinAllSpaces.value or
                                NSWindowCollectionBehavior.NSWindowCollectionBehaviorStationary.value
                    )
                }
            }
        }
    }


    protected open fun setMacOsOptionsAfterInit() {
        with(ApplicationHelper.instance) {
            if (options!!.sticky && getActivationPolicy() != macOS.activationPolicy.value) {
                setActivationPolicy(macOS.activationPolicy.value)
            }
        }
    }

    protected open fun setMacOsOptionsBeforeInit() {
        with(ApplicationHelper.instance) {
            macOS.activationPolicy = NSApplicationActivationPolicy.fromValue(getActivationPolicy())
                ?: NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular

            if (options!!.sticky && macOS.activationPolicy != NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory) {
                setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory.value)
            }
        }

    }

    protected open fun setLinuxOptionsAfterVisibility(visible: Boolean) {
        val states: MutableList<WMState> = mutableListOf()
        if (options!!.skipTaskbar) {
            states.add(WMState.SKIP_TASKBAR)
        }
        if (options!!.sticky) {
            states.add(WMState.STICKY)
        }

        states.takeIf { it.size > 0 }?.map { it.value }?.let { stateValues ->
            windowHandle?.takeIf { it > 0L && visible }?.let { handle ->
                with(WindowHelper.instance) {
                    addWindowMode(
                        handle,
                        stateValues.toTypedArray()
                    )
                }
            }
        }
    }

    protected open fun setMacOsOptionsAfterVisibility() {
        setMacOsOptionsBeforeVisibility(false)
    }

    protected open fun setMacOsOptionsBeforeVisibility(visible: Boolean) {
        with(ApplicationHelper.instance) {
            isActive().let { wasActive ->
                if (visible && options!!.sticky &&
                    macOS.activationPolicy != NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory
                ) {
                    setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyAccessory.value)
                } else if (getActivationPolicy() != macOS.activationPolicy.value) {
                    setActivationPolicy(macOS.activationPolicy.value)
                }

                if (wasActive) {
                    activate()
                } else {
                    deactivate()
                }
            }
        }
    }

    protected open fun findPlatformWindowHandle(window: Window): Long? {
        return with(WindowHelper.instance) {
            when (platform) {
                Platform.Mac -> {
                    findLastWindow()
                }

                Platform.Linux -> {
                    findWindowForName(window.name)
                }

                Platform.Windows -> {
                    findLastWindow()
                }
            }
        }.takeIf { it > 0L } ?: run {
            println("Failed to get window handle for $this:${window.name}")

            null
        }
    }

    protected data class MacOSSpecific(
        var activationPolicy: NSApplicationActivationPolicy =
            NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular,
    )
}

class MissingInitializingOptionsException() : Exception("Missing TopMost initialization options")
