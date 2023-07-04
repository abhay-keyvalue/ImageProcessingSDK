package com.jain.ullas.imageblurdetection

import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat


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