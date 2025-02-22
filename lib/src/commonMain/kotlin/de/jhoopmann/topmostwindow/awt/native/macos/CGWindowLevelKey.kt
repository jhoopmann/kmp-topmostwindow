package de.jhoopmann.topmostwindow.awt.native.macos

enum class CGWindowLevelKey(val value: Int) {
    kCGBaseWindowLevelKey(0),
    kCGMinimumWindowLevelKey(1),
    kCGDesktopWindowLevelKey(2),
    kCGBackstopMenuLevelKey(3),
    kCGNormalWindowLevelKey(4),
    kCGFloatingWindowLevelKey(5),
    kCGTornOffMenuWindowLevelKey(6),
    kCGDockWindowLevelKey(7),
    kCGMainMenuWindowLevelKey(8),
    kCGStatusWindowLevelKey(9),
    kCGModalPanelWindowLevelKey(10),
    kCGPopUpMenuWindowLevelKey(11),
    kCGDraggingWindowLevelKey(12),
    kCGScreenSaverWindowLevelKey(13),
    kCGMaximumWindowLevelKey(14),
    kCGOverlayWindowLevelKey(15),
    kCGHelpWindowLevelKey(16),
    kCGUtilityWindowLevelKey(17),
    kCGDesktopIconWindowLevelKey(18),
    kCGCursorWindowLevelKey(19),
    kCGAssistiveTechHighWindowLevelKey(20),
    kCGNumberOfWindowLevelKeys(21)
}
