#include <jni.h>
#include <string>
#include <atomic>
#include <thread>
#include <android/log.h>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,"edge_native",__VA_ARGS__)

static std::atomic<bool> running(false);
static std::thread worker;

extern "C"
JNIEXPORT void JNICALL
Java_com_example_edgeapp_MainActivity_nativeInit(JNIEnv* env, jclass clazz, jint w, jint h) {
    if (running.load()) return;
    running = true;
    LOGI("nativeInit: %d x %d", w, h);
    worker = std::thread([w,h](){
        // Placeholder loop - in real code, grab camera frames and process with OpenCV
        int counter = 0;
        while (running.load() && counter < 5) {
            __android_log_print(ANDROID_LOG_INFO,"edge_native","processing frame %d", counter);
            std::this_thread::sleep_for(std::chrono::milliseconds(500));
            counter++;
        }
        __android_log_print(ANDROID_LOG_INFO,"edge_native","worker exiting");
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_edgeapp_MainActivity_nativeStop(JNIEnv* env, jclass clazz) {
    if (!running.load()) return;
    running = false;
    if (worker.joinable()) worker.join();
    LOGI("nativeStop");
}
