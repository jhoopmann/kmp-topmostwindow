#!/bin/bash
mkdir -p build

clang++ -dynamiclib -o build/libwindow_helper.dylib window_helper.mm \
    -framework System -framework Foundation -framework Cocoa -framework QuartzCore \
    -I "$JAVA_HOME/include" \
    -I "$JAVA_HOME/include/darwin"

clang++ -dynamiclib -o build/libapplication_helper.dylib application_helper.mm \
    -framework System -framework Foundation -framework Cocoa -framework QuartzCore \
    -I "$JAVA_HOME/include" \
    -I "$JAVA_HOME/include/darwin"
