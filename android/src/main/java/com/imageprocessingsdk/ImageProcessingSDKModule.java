package com.imageprocessingsdk;

import androidx.annotation.NonNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = ImageProcessingSDKModule.NAME)
public class ImageProcessingSDKModule extends ReactContextBaseJavaModule {
  public static final String NAME = "ImageProcessingSDK";

  public ImageProcessingSDKModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android

  @ReactMethod
  public void isImageBlurred(String imagePath, Promise promise) {
    try {
      String[] command = {"python", "path/to/your/package/image_processing.py", imagePath};
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String output = reader.readLine();
      boolean isBlurred = Boolean.parseBoolean(output);
      promise.resolve(isBlurred);
    } catch (IOException e) {
      promise.reject("PROCESS_FAILED", e.getMessage());
    }
  }

}
