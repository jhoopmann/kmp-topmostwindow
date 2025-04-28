# Kotlin Multiplatform TopMostWindow Library

## Introduction
This library provides an interface and implementation class with functionality to set the AWT window as topmost and sticky natively above all other windows and fullscreen applications. Also you can skip the creation of the taskbar item on X11 Linux systems.

#### Wording:

**Topmost**: The window stays above all other windows including task- and menubars. \
**Sticky**: The window stays visible on every space or desktop, even on spaces of fullscreen applications in macOS. \
**SkipTaskbar**: Hides the taskbar entry for the window.

 *<sup>(SkipTaskbar has no effect in macOS because non mainWindows never appear in Dock anyway, use sticky.
 SkipTaskbar also has no effect in Windows because non toolbox windows without a parent always appear in taskbar, use sticky.) </sub>*


![TopMost as decorated on desktop space.](/doc/img/topmost-decorated.png)

![TopMost as decorated on foreign application  fullscreen space.](/doc/img/topmost-decorated-fullscreen.png)

![TopMost as undecorated on desktop space.](/doc/img/topmost.png)

![TopMost as undecorated on foreign application  fullscreen space.](/doc/img/topmost-fullscreen.png)

## Compatibility

Tested with
- JDK 21 / 23
- Kotlin Multiplatform 2.*.*-2.1.10
- macOS 14.*.*-15.3.1, Linux X11 Debian12+Gnome3, Windows 10/11

#### Under the hood:

This library makes use of various methods of the specific OS functionalities.

**Linux**: X11 setting of _NET_WM_STATE_SKIP_TASKBAR, _NET_WM_STATE_STICKY via XChangeProperty and XSendEvent after visibility change. \
\
**macOS**: Temporarely switching between NSApplicationActivationPolicy's in inialization and before and after visibility changes. Also initial setting of NSWindowLevel and NSWindowCollectionBehavior. \
\
**Windows**: Setting of EX_TOOLWINDOW on GWL_EXSTYLE in the WindowLong and HWND_TOPMOST flag in the WindowPosition. Also creating a hook to listen on taskbar events to reapply the flags to the window.

Currently, applying options is only possible in initialization. If you need to change options, you have to recreate the native instance, so by design we provide the options in the initialize function. Maybe this is going to be changed if required but it's difficult for example in macOS because of restrictions by the NSWindow consistency rules.

On macOS, if you set ```beforeInitialization``` and ```afterInitialization``` parameter values, you have to call the TopMost methods ```setPlatformOptionsBeforeInit``` and ```setPlatformOptionsAfterInit```manually to apply ```NSApplicationActivationPolicy```. This is required if you want to initialize multiple TopMostWindow at the same time because of the macOS Dock event processing (displays multiple application images if received in short sequence).

## Usage as delegate

```
class TopMostWindow : Window(null), TopMost by TopMostImpl() {
    init {
        /* initialize TopMost delegate */
        initialize(
            this,
            TopMostOptions(
                topMost = true,
                sticky = true,
                skipTaskbar = true
            ),
            { this: TopMost, Window -> // default DefaultPlatformInitializeParent
                // apply options to be set before and do native initialization call
                // function sets positive long value for windowHandle and calls window.addNotfiy()
            },
            { this: TopMost -> // default DefaultPlatformBeforeInitalizationEvent
              // apply options to be set before native window initialization
            },
            { this: topMost -> // default DefaultPlatformAfterInitializationEvent
                // apply options to be set after native window initialization
            }
        )
    }

    // override awt window setVisible
    override fun setVisible(value: Boolean) {
        // call TopMost delegate setVisible
        setVisible(value) {
            // call original awt window setVisible
            super.setVisible(value)
        }
    }
}
```