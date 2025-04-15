package de.jhoopmann.topmostwindow.awt.ui

val DefaultBeforeInitializationEvent: InitializationEvent = { topMost, options->
    topMost.setPlatformOptionsBeforeInit(options)
}
val DefaultAfterInitializationEvent: InitializationEvent = { topMost, options ->
    topMost.setPlatformOptionsAfterInit(options)
}
