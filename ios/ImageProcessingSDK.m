#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ImageProcessingSDK, NSObject)

RCT_EXTERN_METHOD(isImageBlurred:(NSString *)imageUrl resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

@end
