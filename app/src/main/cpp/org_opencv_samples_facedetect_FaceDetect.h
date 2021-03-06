/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_opencv_samples_facedetect_FaceDetect */

#ifndef _Included_org_opencv_samples_facedetect_FaceDetect
#define _Included_org_opencv_samples_facedetect_FaceDetect
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniSetUseOpenCL
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniSetUseOpenCL
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniInitDetect
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniInitDetect
  (JNIEnv *, jobject, jstring);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect * Method:    jniDetectMultiScale
 * Signature: (JJDIIDDDDF)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniDetectMultiScale
  (JNIEnv *, jobject, jlong, jlong, jdouble, jint, jint, jdouble, jdouble, jdouble, jdouble, jfloat);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniSetThreshold
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniSetThreshold
  (JNIEnv *, jobject, jdouble, jlong);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniMatDistinction
 * Signature: (JJ)F
 */
JNIEXPORT jfloat JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniMatDistinction
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniRecognizerTraining
 * Signature: (Ljava/lang/String;Ljava/lang/String;IC)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniRecognizerTraining
  (JNIEnv *, jobject, jstring, jstring, jint, jchar);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniLoadModelFile
 * Signature: (Ljava/lang/String;III)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniLoadModelFile
  (JNIEnv *, jobject, jstring, jint, jint, jint);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniSetAlpha
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniSetAlpha
  (JNIEnv *, jobject, jdouble);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniSetBeta
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniSetBeta
  (JNIEnv *, jobject, jint);

/*
 * Class:     org_opencv_samples_facedetect_FaceDetect
 * Method:    jniImgEnhance
 * Signature: (JJDI)V
 */
JNIEXPORT void JNICALL Java_org_opencv_samples_facedetect_FaceDetect_jniImgEnhance
  (JNIEnv *, jobject, jlong, jlong, jdouble, jint);

#ifdef __cplusplus
}
#endif
#endif
