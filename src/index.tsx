import { NativeModules } from 'react-native';
const { ImageProcessingSDK } = NativeModules;

export type ImageFit = 'none' | 'fill' | 'contain' | 'cover';

export type Page = {
  imagePath: string;
  imageFit?: ImageFit;
};

export type CreatePdfOptions = {
  outputDirectory?: string;
  outputFilename?: string;
  images: Array<string>;
};

export function isImageBlurred(imageUrl: string): Promise<boolean> {
  return ImageProcessingSDK.isImageBlurred(imageUrl);
}

export function createImagesToPdf(options: CreatePdfOptions): Promise<string> {
  const { images, ...opts } = options;

  const internalPages = images?.map((url) => {
    return {
      imagePath: url,
    };
  });

  return ImageProcessingSDK.createPdf({
    ...opts,
    images: internalPages,
  });
}
