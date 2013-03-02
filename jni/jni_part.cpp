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

JNIEXPORT void JNICALL Java_co_mwater_opencvapp_Process(JNIEnv* env, jobject bitmap) {
	AndroidBitmapInfo  bitmap_info;
	void*              bitmap_pixels;
	int ret;

	if ((ret = AndroidBitmap_getInfo(env, bitmap, &bitmap_info)) < 0) {
		LOGE("AndroidBitmap_getInfo() failed ! error=%d", ret);
	    return;
	}


}

JNIEXPORT void JNICALL Java_org_opencv_samples_tutorial3_Sample3Native_FindFeatures(JNIEnv*, jobject, jlong addrGray, jlong addrRgba);

JNIEXPORT void JNICALL Java_org_opencv_samples_tutorial3_Sample3Native_FindFeatures(JNIEnv*, jobject, jlong addrGray, jlong addrRgba)
{
    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    // Get input and output arrays
//	jbyte* _yuv = env->GetByteArrayElements(yuv, 0);
//	jint* _bgra = env->GetIntArrayElements(bgra, 0);
//
//	Mat myuv(height + height / 2, width, CV_8UC1, (unsigned char *) _yuv);
//	Mat mbgra(height, width, CV_8UC4, (unsigned char *) _bgra);
//
//	// Please pay attention to BGRA byte order
//	// ARGB stored in java as int array becomes BGRA at native level
//	cvtColor(myuv, mbgra, CV_YUV420sp2BGR, 4);
//
//	createPreview(mbgra);
//
//	env->ReleaseIntArrayElements(bgra, _bgra, 0);
//	env->ReleaseByteArrayElements(yuv, _yuv, 0);

    FastFeatureDetector detector(50);
    detector.detect(mGr, v);
    for( unsigned int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 10, Scalar(255,0,0,255));
    }
}
}
