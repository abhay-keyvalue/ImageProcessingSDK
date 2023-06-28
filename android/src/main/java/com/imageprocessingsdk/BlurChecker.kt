package com.example.ndksample

class BlurChecker {
    companion object {
        init {
            System.loadLibrary("native_lib")
        }
    }
    external fun detectBlur(image: ByteArray, authenticator: Any): Boolean
}