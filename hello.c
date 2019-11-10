#include <jni.h>
#include "PlayerConnection.h"
#include <stdio.h>

JNIEXPORT jstring JNICALL
Java_PlayerConnection_hello(JNIEnv *env, jobject obj, jint winTotal, jint collectedGold) {
	return "GOLD: " + (winTotal - collectedGold);
}