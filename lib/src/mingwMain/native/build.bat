rmdir /s /q build
mkdir build

g++ -fPIC -shared -o build/libwindow_helper.dll window_helper.cpp ^
    -I"%JAVA_HOME%\include" ^
    -I"%JAVA_HOME%\include\win32" ^
    -static-libgcc -static-libstdc++
