import {
  ColorValue,
  NativeModules,
  processColor,
  ProcessedColorValue,
} from 'react-native';
const { ImageProcessingSDK } = NativeModules;

export function isImageBlurred(imageUrl: string): Promise<boolean> {
  return ImageProcessingSDK.isImageBlurred(imageUrl);
}

export function createImagesToPdf(options: CreatePdfOptions): Promise<string> {
  return createPdf(options);
}

export type ImageFit = 'none' | 'fill' | 'contain' | 'cover';

export type Page = {
  imagePath: string;
  imageFit?: ImageFit;
  width?: number;
  height?: number;
  backgroundColor?: ColorValue;
};

interface InternalPage extends Omit<Page, 'backgroundColor'> {
  backgroundColor?: ProcessedColorValue;
}

export type CreatePdfOptions =
  | {
      outputDirectory?: string;
      outputFilename?: string;
      images: Array<Page | string>;
      imagePaths?: undefined;
    }
  | {
      outputDirectory?: string;
      outputFilename?: string;
      images?: undefined;
      /**
       * @deprecated Use the `pages` property instead.
       */
      imagePaths: string[];
    };

function createPdf(options: CreatePdfOptions): Promise<string> {
  const { images, imagePaths, ...opts } = options;

  const internalPages = (imagePaths || images || []).map<InternalPage>((e) => {
    if (typeof e === 'string') {
      return {
        imagePath: e,
      };
    }

    const { backgroundColor, ...page } = e;

    return {
      backgroundColor: processColor(backgroundColor) ?? undefined,
      ...page,
    };
  });

  return ImageProcessingSDK.createPdf({
    ...opts,
    images: internalPages,
  });
}
