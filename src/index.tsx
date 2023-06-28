import { NativeModules } from 'react-native';
const { ImageProcessingSDK } = NativeModules;

export function isImageBlurred(imageUrl: string): Promise<boolean> {
  return ImageProcessingSDK.isImageBlurred(imageUrl);
}
