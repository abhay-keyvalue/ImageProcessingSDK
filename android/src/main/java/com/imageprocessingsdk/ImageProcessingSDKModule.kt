package com.imageprocessingsdk

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class ImageProcessingSDKModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun isImageBlurred(imageUrl: String, promise: Promise) {
    promise.resolve(imageUrl)
  }

  companion object {
    const val NAME = "ImageProcessingSDK"
  }
}
