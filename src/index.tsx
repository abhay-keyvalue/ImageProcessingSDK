import { NativeModules } from 'react-native';
const { ImageProcessingSDK } = NativeModules;

export function isImageBlurred(imageUrl: string): Promise<string> {
  return ImageProcessingSDK.isImageBlurred(imageUrl);
}
