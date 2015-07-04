/*
 * Compile with following command : 
 * 
 * gcc -Wl,--add-stdcall-alias -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" -shared -o winCommunicator.dll WinCommunicator.c
 */

#include <jni.h>
#include <stdio.h>
#include <windows.h>
#include "imageembedder_WinSettings.h"

JNIEXPORT void JNICALL Java_imageembedder_WinSettings_broadcastChange(JNIEnv * env, jobject obj){
    char filepath[225];
    //SystemParametersInfo(SPI_GETDESKWALLPAPER, sizeof (filepath) - 1, filepath, SPIF_SENDCHANGE);
    SendNotifyMessageA()
    SendNotifyMessage(HWND_BROADCAST);
}
