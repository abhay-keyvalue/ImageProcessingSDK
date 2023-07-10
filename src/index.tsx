import { NativeModules } from 'react-native';
import { ERROR_CODES, validateImageUrl } from './utils';
import { validateImageExtension } from './utils/checks';
const { ImageProcessingSDK } = NativeModules;

export type CreatePdfOptions = {
  images: Array<string>;
  outputDirectory?: string;
  outputFilename?: string;
};

export function isImageBlurred(imageUrl: string): Promise<boolean> {
  return new Promise((resolve, reject) => {
    if (validateImageUrl(imageUrl)) {
      if (validateImageExtension(imageUrl)) {
        resolve(ImageProcessingSDK.isImageBlurred(imageUrl.slice(8)));
      } else {
        reject(new Error(JSON.stringify(ERROR_CODES.ERR002)));
      }
    } else {
      reject(new Error(JSON.stringify(ERROR_CODES.ERR001)));
    }
  });
}

export function createImagesToPdf(options: CreatePdfOptions): Promise<string> {
  const { images, ...opts } = options;
  const internalPages = images?.map((url) => {
    return { imagePath: url };
  });
  return ImageProcessingSDK.createPdf({
    ...opts,
    images: internalPages,
  });
}
