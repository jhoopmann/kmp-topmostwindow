# Changelog

## [UNRELEASED]
### Version notes

### Added

### Changed

### Removed

## [1.2.0]
### Version notes
Call update in setVisible of ComposeTopMostWindow, remove TopMostCompanion, refactor some methods

### Added
TopMost fun setPlatformOptionsBeforeInit(options: TopMostOptions?)
TopMost fun setPlatformOptionsAfterInit(options: TopMostOptions?)
typealias InitializationEvent

### Changed
 fun initialize(
    beforeInitialization: InitializationEvent? = DefaultBeforeInitializationEvent,
    afterInitialization: InitializationEvent? = DefaultAfterInitializationEvent
)

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