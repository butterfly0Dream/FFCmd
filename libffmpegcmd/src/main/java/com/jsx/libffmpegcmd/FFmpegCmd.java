package com.jsx.libffmpegcmd;

/**
 * Author: JackPan
 * Date: 2020-12-15
 * Time: 16:53
 * Description:
 */
public class FFmpegCmd {

    static {
        System.loadLibrary("avutil");
        System.loadLibrary("avcodec");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("swresample");
        System.loadLibrary("avfilter");
        System.loadLibrary("jxffmpegrun");
    }

    /**
     * 开始执行 ffmpeg cmd line
     *
     * @param cmd
     * @return
     */
    public static native int ffmpegRun(String[] cmd);

    /**
     * 获取ffmpeg编译信息
     *
     * @return
     */
    public static native String getFFmpegConfig();

    /**
     * 主动终止 ffmpeg cmd line
     */
    public static native void ffmpegCancel();


    public interface OnProgress {

        //这里 直接 返回有 进度日志的信息给 java 层 在 java 层做处理，因为C不太会呀。
        void progress(String log);

    }

    /**
     * 带进度回调的 命令行工具
     *
     * @param cmd
     * @param onProgress
     * @return
     */
    public static native int ffmpegRunPro(String[] cmd, OnProgress onProgress);

}
