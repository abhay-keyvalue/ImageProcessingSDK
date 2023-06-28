@objc(ImageProcessingSDK)
class ImageProcessingSDK: NSObject {

  @objc
  func isImageBlurred(_ imageUrl: String, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
    resolve(true)
  }
}
