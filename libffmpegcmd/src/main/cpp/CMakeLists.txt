cmake_minimum_required(VERSION 3.4.1)

add_library(
        libavcodec
        SHARED
        IMPORTED
)

SET_TARGET_PROPERTIES(
        libavcodec
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libavcodec.so
)

#add_library(
#        libavdevice
#        SHARED
#        IMPORTED
#)
#
#SET_TARGET_PROPERTIES(
#        libavdevice
#        PROPERTIES IMPORTED_LOCATION
#        ${PROJECT_SOURCE_DIR}/ffmpeg/prebuilt/${ANDROID_ABI}/libavdevice.so
#)

add_library(libavfilter
        SHARED
        IMPORTED
        )

SET_TARGET_PROPERTIES(
        libavfilter
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libavfilter.so
)


add_library(libavformat
        SHARED
        IMPORTED
        )

SET_TARGET_PROPERTIES(
        libavformat
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libavformat.so
)


add_library(
        libavutil
        SHARED
        IMPORTED
)

SET_TARGET_PROPERTIES(
        libavutil
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libavutil.so
)


#add_library(libpostproc
#        SHARED
#        IMPORTED
#        )
#
#SET_TARGET_PROPERTIES(
#        libpostproc
#        PROPERTIES IMPORTED_LOCATION
#        ${PROJECT_SOURCE_DIR}/ffmpeg/prebuilt/${ANDROID_ABI}/libpostproc.so
#)


add_library(
        libswresample
        SHARED
        IMPORTED
)

SET_TARGET_PROPERTIES(
        libswresample
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libswresample.so
)


add_library(
        libswscale
        SHARED
        IMPORTED
)

SET_TARGET_PROPERTIES(
        libswscale
        PROPERTIES IMPORTED_LOCATION
        ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libswscale.so
)


include_directories(ffmpeg/)


add_library(
        jxffmpegrun

        SHARED

        ffmpeg/cmdutils.c
        ffmpeg/ffmpeg.c
        ffmpeg/ffmpeg_filter.c
        ffmpeg/ffmpeg_opt.c
        ffmpeg/jx_ffmpeg_cmd_run.c
)

find_library(
        log-lib

        log)

target_link_libraries(
        jxffmpegrun

        libavcodec
        libavfilter
        libavformat
        libavutil
        libswresample
        libswscale
        ${log-lib})