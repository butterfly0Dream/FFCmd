/**
 * Created by jianxi on 2017/6/4..
 * https://github.com/mabeijianxi
 * mabeijianxi@gmail.com
 */

#include "ffmpeg.h"
#include <jni.h>
#include <android/log.h>

#define logDebug(...) __android_log_print(ANDROID_LOG_DEBUG,"MainActivity",__VA_ARGS__)
#define logError(...) __android_log_print(ANDROID_LOG_ERROR,"MainActivity",__VA_ARGS__)

//java 对象 回调给 jni 层的接口

JNIEnv *c_env;// jvm 虚拟机
jobject mapping_onprogress;//c 对于 java 的映射
jclass c_onprogress;// 真实 的 java 类
jmethodID c_progress; //真实 的 java 类 的 方法

void log_handle(char *ret)
{
    //总进度输出
//    char headStr[12] = "format";//检索 这个关键字
//    char zongStr[16] = "duration";//检索 这个关键字
    char *head = strstr(ret,"format");
    char *zong = strstr(ret, "duration");

    //获取总进度
    if(head != NULL && zong!= NULL)
    {
        //将日志 回调给 java层 该日志信息包含总进度
        jstring j_zong = (*c_env)->NewStringUTF(c_env,ret);
        (*c_env)->CallVoidMethod(c_env,mapping_onprogress, c_progress,j_zong);
    }

    //当前进度捕获
//    char timeStr[10] = "time=";//检索 这个关键字
    char *now = strstr(ret, "time=");
    if(now != NULL)
    {
        //将 日志 回调给 java 层 该日志信息包含当前进度
        jstring j_now = (*c_env)->NewStringUTF(c_env,ret);
        (*c_env)->CallVoidMethod(c_env,mapping_onprogress, c_progress,j_now);
    }
}

void log_callback_cpp(void *ptr, int level, const char *fmt, va_list vl) {

    //自定义的日志
    static int print_prefix = 1;
    static char prev[1024];
    char line[1024];
    av_log_format_line(ptr, level, fmt, vl, line, sizeof(line), &print_prefix);
    strcpy(prev, line);

    if (level <= AV_LOG_WARNING) {
        logError("FF-EOR：%s", line);
    } else {
        logDebug("FF-LOG：%s", line);
    }

    //处理日志
    log_handle(line);

}






JNIEXPORT jint JNICALL
Java_com_jsx_libffmpegcmd_FFmpegCmd_ffmpegRun(JNIEnv *env, jobject type,jobjectArray commands){
    int argc = (*env)->GetArrayLength(env,commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env,commands, i);
        argv[i] = (char *) (*env)->GetStringUTFChars(env,js, 0);
    }
    return jxRun(argc,argv);
}

JNIEXPORT jstring JNICALL
Java_com_jsx_libffmpegcmd_FFmpegCmd_getFFmpegConfig(JNIEnv *env, jobject instance) {

    char info[10000] = {0};
    sprintf(info, "%s\n", avcodec_configuration());
    return (*env)->NewStringUTF(env,info);

}

JNIEXPORT void JNICALL
Java_com_jsx_libffmpegcmd_FFmpegCmd_ffmpegCancel(JNIEnv *env, jobject instance) {
    quit_runnable();
}

JNIEXPORT jint JNICALL
Java_com_jsx_libffmpegcmd_FFmpegCmd_ffmpegRunPro(JNIEnv *env, jobject type,jobjectArray commands,jobject onprogress){


    //先获取进度回调
    if(onprogress != NULL){

        //获取 jvm
        c_env = env;

        //获取 java 类 的 映射
        mapping_onprogress = onprogress;

        //获取 java 类
        c_onprogress = (*env)->GetObjectClass(env,onprogress);

        //获取 java 类 里的 方法
        c_progress = (*env)->GetMethodID(c_env,c_onprogress,"progress","(Ljava/lang/String;)V");

    }

    //处理命令行
    int argc = (*env)->GetArrayLength(env,commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env,commands, i);
        argv[i] = (char *) (*env)->GetStringUTFChars(env,js, 0);
    }

    //执行
    return jxRunPro(argc,argv,log_callback_cpp);
}