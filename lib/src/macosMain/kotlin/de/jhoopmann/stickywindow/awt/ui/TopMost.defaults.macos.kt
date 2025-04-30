package de.jhoopmann.stickywindow.awt.ui

import de.jhoopmann.stickywindow.awt.native.CGWindowLevelKey
import de.jhoopmann.stickywindow.awt.native.NSWindowCollectionBehavior
import de.jhoopmann.stickywindow.awt.native.WindowHelper

actual val DefaultPlatformInitializeParent: InitializeParentFunction = { window ->
    DefaultInitializeParent.invoke(this, window).apply {
        with(WindowHelper.instance) {
            if (options.topMost) {
                setWindowLevel(
                    windowHandle,
                    getCGWindowLevelForKey(CGWindowLevelKey.kCGAssistiveTechHighWindowLevelKey.value)
                )
            }

            if (options.sticky) {
                setWindowCollectionBehavior(
                    windowHandle,
                    NSWindowCollectionBehavior.NSWindowCollectionBehaviorCanJoinAllSpaces.value or
                            NSWindowCollectionBehavior.NSWindowCollectionBehaviorFullScreenAuxiliary.value or
                            NSWindowCollectionBehavior.NSWindowCollectionBehaviorStationary.value                )
            }
        }
    }
}

actual val DefaultPlatformBeforeInitialization: InitializationEvent = DefaultBeforeInitialization

actual val DefaultPlatformAfterInitialization: InitializationEvent = DefaultAfterInitialization
