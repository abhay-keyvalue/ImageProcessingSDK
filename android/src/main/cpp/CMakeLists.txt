cmake_minimum_required(VERSION 3.6)

#include_directories(/Users/ar1ndam/Downloads/OpenCV-android-sdk/sdk/native/jni/include)
include_directories(../opencvinclude)
add_library( lib_opencv SHARED IMPORTED )
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopencv_java3.so)

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

add_library( native_lib SHARED native_lib.cpp )

target_link_libraries( # Specifies the target library.
                       native_lib

                       # OpenCV lib
                        lib_opencv

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )