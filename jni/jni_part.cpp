#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/features2d/features2d.hpp>
#include <vector>

#include <android/log.h>
#include <android/bitmap.h>

using namespace std;
using namespace cv;

#define LOG_TAG "co.mwater.opencvapp"
#ifdef ANDROID
#include <android/log.h>
#  define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#  define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#else
#include <stdio.h>
#  define QUOTEME_(x) #x
#  define QUOTEME(x) QUOTEME_(x)
#  define LOGI(...) {printf(__VA_ARGS__); printf("\r\n");}
#  define LOGE(...) printf("E/" LOG_TAG "(" ")" __VA_ARGS__)
#endif


extern "C" {

JNIEXPORT void JNICALL Java_co_mwater_opencvapp_OpenCVActivity_runProcess(JNIEnv* env, jobject activity, jobject screen_bitmap) {
	AndroidBitmapInfo  screen_bitmap_info;
	void*              screen_bitmap_pixels;
	int ret;

	jclass cls = env->FindClass("android/graphics/Bitmap");
	jmethodID mth = env->GetMethodID(cls, "getWidth", "()I");
	int width = env->CallIntMethod(screen_bitmap, mth);
	LOGI("width=%d", width);

	if ((ret = AndroidBitmap_getInfo(env, screen_bitmap, &screen_bitmap_info)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	    return;
	}
	LOGI("ret=%d", ret);
	LOGI("info=%d, %d : %d", screen_bitmap_info.width, screen_bitmap_info.height, screen_bitmap_info.stride);
	if (screen_bitmap_info.width == 0)
		return;

	if ((ret = AndroidBitmap_lockPixels(env, screen_bitmap, &screen_bitmap_pixels)) < 0) {
		LOGE("AndroidBitmap_lockPixels() failed ! error=%d", ret);
	}

	// Create Mat
	Mat screen(
			Size(screen_bitmap_info.width, screen_bitmap_info.height),
			CV_8UC4, screen_bitmap_pixels);// , screen_bitmap_info.stride);

	LOGI("Drawing on mat %d, %d", screen.cols, screen.rows);
	//LOGI("Val = %d", screen.at<Vec4d>(10, 10)[0]);
	// Draw circle
	circle(screen, Point(100,100), 20, Scalar(255, 0, 0, 255), 5);

	LOGI("unlocking pixels");
	AndroidBitmap_unlockPixels(env, screen_bitmap);
}
}
