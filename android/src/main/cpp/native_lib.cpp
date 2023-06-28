

#include<jni.h>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include <android/log.h>

#define TAG "MY_TAG"

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,    TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,     TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,    TAG, __VA_ARGS__)

using namespace std;
using namespace cv;

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_ndksample_BlurChecker_detectBlur(JNIEnv *env, jobject thiz, jbyteArray imageArray,
                                                        jobject obj) {

    jclass cls = env->GetObjectClass(obj);

    // First get the class object
    jmethodID mid = env->GetMethodID(cls, "getClass", "()Ljava/lang/Class;");
    jobject clsObj = env->CallObjectMethod(obj, mid);

// Now get the class object's class descriptor
    cls = env->GetObjectClass(clsObj);

// Find the getName() method on the class object
    mid = env->GetMethodID(cls, "getName", "()Ljava/lang/String;");

// Call the getName() to get a jstring object back
    jstring strObj = (jstring)env->CallObjectMethod(clsObj, mid);

// Now get the c string from the java jstring object
    const char* str = env->GetStringUTFChars(strObj, NULL);

    if(strcmp(str, "co.invoid.imagehelperondevicesupport.Authenticator") != 0) {
        env->ReleaseStringUTFChars(strObj, str);
        LOGE("Not allowed to call GlareDetector!");
        return static_cast<jboolean>(false);
    }
    env->ReleaseStringUTFChars(strObj, str);


    jbyte *jbae = env->GetByteArrayElements(imageArray, 0);
    jsize len = env->GetArrayLength(imageArray);
    char *imageSource = (char *) jbae;
    vector<char> imageSourceV;
    for (int i = 0; i < len; i++) {
        imageSourceV.push_back(imageSource[i]);
    }

    Mat image_gray, laplacianImage;
    Mat image = imdecode(imageSourceV, CV_LOAD_IMAGE_COLOR);

    Size dsize = Size(512, 512);
    resize(image, image, dsize, 0, 0, CV_INTER_AREA);

    cvtColor(image, image_gray, COLOR_BGR2GRAY);
    image.release();

    Laplacian(image_gray, laplacianImage, CV_64F);
    image_gray.release();
    Scalar mean, stddev; // 0:1st channel, 1:2nd channel and 2:3rd channel

    int count = countNonZero(laplacianImage);
    meanStdDev(laplacianImage, mean, stddev, Mat());
    double variance = stddev.val[0] * stddev.val[0];

//    LOGD("variance : value: %f", variance);

    double threshold = 200;

    env->ReleaseByteArrayElements(imageArray, jbae, 0);
    return static_cast<jboolean>((variance < threshold));
}