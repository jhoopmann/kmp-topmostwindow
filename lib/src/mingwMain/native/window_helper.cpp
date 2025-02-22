#include <jni.h>
#include <windows.h>
#include <psapi.h>
#include <iostream>

struct WindowSearch {
    DWORD processId;
    HWND window;
};

static JavaVM* jvm = nullptr;

static const char* taskbarWindowClassName = "Shell_TrayWnd";
static HHOOK taskbarWindowHook = nullptr;

static const char* libWindowProperty = "windowHelperLibWindow";
static HWND libWindow = nullptr;

static HINSTANCE libModule = nullptr;

static void removeWindowHook() {
  	if (taskbarWindowHook != nullptr) {
        UnhookWindowsHookEx(taskbarWindowHook);

		taskbarWindowHook = nullptr;
  	}
}

BOOL APIENTRY DllMain(HANDLE hModule,
                      DWORD  dwReason,
                      LPVOID lpReserved)
{
   	switch (dwReason)
   	{
   	case DLL_PROCESS_ATTACH :
      	libWindow = reinterpret_cast<HWND>(GetProp(GetDesktopWindow(), libWindowProperty));
		libModule = reinterpret_cast<HINSTANCE>(hModule);

      	break;

   case DLL_THREAD_ATTACH :
      	break;

   case DLL_THREAD_DETACH :
      	break;

   case DLL_PROCESS_DETACH :
        removeWindowHook();
      	break;
   }
   return TRUE;
}

static bool setWindowStickyOptions(HWND window) {
    return SetWindowLong(window, GWL_EXSTYLE, GetWindowLong(window, GWL_EXSTYLE) | WS_EX_TOOLWINDOW) != 0 &&
        SetWindowPos(window, NULL, 0,0,0,0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOZORDER | SWP_NOACTIVATE | SWP_FRAMECHANGED);
}

static bool setWindowTopMostOptions(HWND window) {
    return SetWindowLong(window, GWL_EXSTYLE, GetWindowLong(window, GWL_EXSTYLE) | WS_EX_TOPMOST) != 0 &&
           SetWindowPos(window, HWND_TOPMOST, 0,0,0,0, SWP_NOMOVE | SWP_NOSIZE | SWP_NOACTIVATE | SWP_FRAMECHANGED);
}

static BOOL CALLBACK WindowEnumeratorProc(HWND hwnd, LPARAM windowSearch) {
    WindowSearch* search = reinterpret_cast<WindowSearch*>(windowSearch);

    DWORD processId;
    GetWindowThreadProcessId(hwnd, static_cast<LPDWORD>(&processId));

    if (processId == search->processId) {
		search->window = hwnd;

    	return false;
    }

    return true;
}

static LRESULT CALLBACK windowHookProc(int code, WPARAM wParam, LPARAM lParam) {
    if (libWindow != nullptr) {
		setWindowTopMostOptions(libWindow);
    }

    return CallNextHookEx(nullptr, code, wParam, lParam);
}

static DWORD findWindowThreadId(const char* windowClassName) {
	HWND window = FindWindowExA(nullptr, nullptr, windowClassName, nullptr);
	if (window == NULL) {
         std::cerr << "Failed to find Window " << windowClassName << std::endl;
    } else {
      	DWORD windowThreadId = GetWindowThreadProcessId(window, nullptr);
        if (windowThreadId) {
          	return windowThreadId;
        }

        std::cerr << "Failed to find taskbar window thread id" << std::endl;
   	}

    return 0;
}

static bool setupWindowHook() {
	DWORD windowThreadId = findWindowThreadId(taskbarWindowClassName);
    if (windowThreadId != 0) {
     	HHOOK windowHook = SetWindowsHookEx(WH_CALLWNDPROCRET, windowHookProc, libModule, windowThreadId);
	    if (windowHook != NULL) {
    	    taskbarWindowHook = windowHook;

            return true;
    	}

        std::cerr << "Failed to set window listener hook: " << GetLastError() << std::endl;
    }

    return true;
}

static HWND findLastWindow() {
  	DWORD processId = GetCurrentProcessId();
    WindowSearch windowSearch = { processId, nullptr };

    EnumChildWindows(GetDesktopWindow(), WindowEnumeratorProc, reinterpret_cast<LPARAM>(&windowSearch));
    if(windowSearch.window == nullptr) {
    	std::cerr << "Failed to find last window" << std::endl;
	}

    return windowSearch.window;
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
        jvm = vm;
        return JNI_VERSION_1_6;
    }

    JNIEXPORT jlong JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_findLastWindow(JNIEnv *env, jobject obj) {
        HWND windowHandle = findLastWindow();

        return reinterpret_cast<jlong>(windowHandle);
    }

    JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowSticky(JNIEnv *env, jobject obj, jlong windowHandle) {
        HWND window = reinterpret_cast<HWND>(windowHandle);

        if (!setWindowStickyOptions(window)) {
            std::cerr << "Failed to set window as sticky" << std::endl;
        }
    }

    JNIEXPORT void JNICALL Java_de_jhoopmann_topmostwindow_awt_native_WindowHelper_setWindowTopMost(JNIEnv *env, jobject obj, jlong windowHandle) {
		removeWindowHook();

        HWND window = reinterpret_cast<HWND>(windowHandle);
        SetProp(GetDesktopWindow(), libWindowProperty, window);

        if(!setupWindowHook() || !setWindowTopMostOptions(window)) {
            std::cerr << "Failed to set window as topmost" << std::endl;
        }
    }
}
