package de.jhoopmann.stickywindow.awt.ui

import java.awt.Window

class WindowHandleException(window: Window) : Exception("Failed to find windowHandle for $window")
class TopMostWindowNotInitializedException : Exception("TopMostWindow not initialized yet")
