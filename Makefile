all: jni copy

jni: jni/jni_part.cpp
	OPENCVROOT=/home/clayton/install/OpenCV-2.4.3.2-android-sdk /home/clayton/install/android-ndk-r8d/ndk-build all
	
copy:
	cp src/co/mwater/opencvactivity/OpenCVActivity.java plugin/src/android
	cp src/co/mwater/opencvactivity/OpenCVActivityPlugin.java plugin/src/android
	cp libs/armeabi/libopencvactivity.so plugin/src/android/armeabi/libopencvactivity.so
	cp libs/armeabi-v7a/libopencvactivity.so plugin/src/android/armeabi-v7a/libopencvactivity.so
	