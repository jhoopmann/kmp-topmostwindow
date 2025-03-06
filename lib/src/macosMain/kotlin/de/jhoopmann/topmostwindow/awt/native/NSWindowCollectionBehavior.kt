package de.jhoopmann.topmostwindow.awt.native

enum class NSWindowCollectionBehavior(val value: Int) {
    NSWindowCollectionBehaviorDefault(0),
    NSWindowCollectionBehaviorCanJoinAllSpaces(1 shl 0),
    NSWindowCollectionBehaviorMoveToActiveSpace(1 shl 1),
    NSWindowCollectionBehaviorManaged(1 shl 2),
    NSWindowCollectionBehaviorTransient(1 shl 3), // macOS 10.6+
    NSWindowCollectionBehaviorStationary(1 shl 4), // macOS 10.6+
    NSWindowCollectionBehaviorParticipatesInCycle(1 shl 5), // macOS 10.6+
    NSWindowCollectionBehaviorIgnoresCycle(1 shl 6), // macOS 10.6+
    NSWindowCollectionBehaviorFullScreenPrimary(1 shl 7), // macOS 10.7+
    NSWindowCollectionBehaviorFullScreenAuxiliary(1 shl 8), // macOS 10.7+
    NSWindowCollectionBehaviorFullScreenNone(1 shl 9), // macOS 10.7+
    NSWindowCollectionBehaviorFullScreenAllowsTiling(1 shl 11), // macOS 10.11+
    NSWindowCollectionBehaviorFullScreenDisallowsTiling(1 shl 12), // macOS 10.11+
    NSWindowCollectionBehaviorPrimary(1 shl 16), // macOS 13.0+
    NSWindowCollectionBehaviorAuxiliary(1 shl 17), // macOS 13.0+
    NSWindowCollectionBehaviorCanJoinAllApplications(1 shl 18) // macOS 13.0+
}
