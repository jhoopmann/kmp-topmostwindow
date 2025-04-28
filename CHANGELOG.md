# Changelog

## [UNRELEASED]
### Version notes

### Added

### Changed

### Removed

## [1.2.0]
### Version notes
remove TopMostCompanion, refactor some methods

### Added
typealias InitializeParentFunction = TopMost.(Window) -> Unit
typealias InitializationEvent = TopMost.() -> Unit
val initialized: Boolean
val options: TopMostOptions
var windowHandle: Long

### Changed
fun setWindowOptionsBeforeInit()
fun setWindowOptionsAfterInit()
 fun initialize(
    initializeParent: InitializeParentFunction = DefaultInitializeParent,
    beforeInitialization: InitializationEvent = DefaultPlatformBeforeInitializationEvent,
    afterInitialization: InitializationEvent = DefaultPlatformAfterInitializationEvent
)
fun findWindowHandle(window: Window): Long

### Removed
TopMostCompanion
TopMostCompanion fun setPlatformOptionsBeforeInit(options: TopMostOptions?)
TopMostCompanion fun setPlatformOptionsAfterInit(options: TopMostOptions?)

## [1.0.0] 2025-02-21
### Version notes
initial release

### Added

### Changed

### Removed