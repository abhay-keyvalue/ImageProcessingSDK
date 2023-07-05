package com.imageprocessingsdk

import com.imageprocessingsdk.opencv.android.OpenCVLoader
import com.imageprocessingsdk.opencv.core.CvType
import com.imageprocessingsdk.opencv.core.Mat


object OpenCVInitializer {

    private var initialized = false

    @Synchronized
    fun initialize() {
        if (!initialized) {
            System.loadLibrary("opencv_java3")
            OpenCVLoader.initDebug()
            initialized = true
        }
    }

    fun createMat(): Mat? {
        return Mat(3, 3, CvType.CV_8UC1)
    }

}