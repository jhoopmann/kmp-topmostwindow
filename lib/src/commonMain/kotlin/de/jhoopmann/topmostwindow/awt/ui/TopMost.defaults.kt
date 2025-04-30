package de.jhoopmann.topmostwindow.awt.ui

val DefaultInitializeParent: InitializeParentFunction = { window ->
    if (!window.isDisplayable) {
        window.addNotify()
    }

    windowHandle = findWindowHandle(window)
}

val DefaultBeforeInitialization: InitializationEvent = {
    setWindowOptionsBeforeInit()
}

val DefaultAfterInitialization: InitializationEvent = {
    setWindowOptionsAfterInit()
}

expect val DefaultPlatformInitializeParent: InitializeParentFunction

expect val DefaultPlatformBeforeInitialization: InitializationEvent

expect val DefaultPlatformAfterInitialization: InitializationEvent
