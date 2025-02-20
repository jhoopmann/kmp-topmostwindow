#!/bin/bash
mkdir -p build

g++ -fPIC -shared -o build/libwindow_helper.so window_helper.cpp \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/linux" \
    -static-libgcc -static-libstdc++
