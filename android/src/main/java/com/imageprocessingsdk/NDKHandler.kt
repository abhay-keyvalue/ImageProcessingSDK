package com.example.ndksample

import java.io.File

class NDKHandler {
    val blurChecker = BlurChecker()
    fun getBlurredImage(imagePath: String): Boolean{
        val image = File(imagePath)
        image.readBytes().let {
            return blurChecker.detectBlur(it, 4)
        }
    }
}