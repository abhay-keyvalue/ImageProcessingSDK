<p align="center">
  <img src="https://res.cloudinary.com/crunchbase-production/image/upload/c_lpad,h_256,w_256,f_auto,q_auto:eco,dpr_1/v1441713939/o8bplvbhmr2jzkhynltt.png" alt="MSD Swift Library"/>
</p>

# Table of Contents
<!-- MarkdownTOC -->
- [Overview](#overview)
- [Quick Start Guide](#quick-start-guide)
    - [Install MSD](#1-install-msd)
    - [Image Blur Detection](#3-discover-events)
    - [Image to PDF](#4-track-event)
    - [Complete Code Example](#complete-code-example)
- [FAQ](#faq)
- [I want to know more!](#i-want-to-know-more)

<!-- /MarkdownTOC -->


<a name="introduction"></a>
## Overview

Welcome to the official MSD React Native Library

## Quick Start Guide

### 1. Install MSD

#### Prerequisites
- React Native v0.6+
#### Steps
1. Under your app's root directory, install MSD React Native SDK. 
```
npm install msd-react-native
```


### 2. Image Blur Detection

To leverage the image blur detection capabilities of our SDK, you can utilize the isImageBlurred function. This powerful function allows you to determine whether an image is blurred or not. It accepts an image URL as input and provides a boolean response to indicate the blur status.

When you invoke the isImageBlurred function with the URL of the image you want to analyze, it performs an advanced analysis and returns a boolean value. A true response signifies that the uploaded image is blurred. Conversely, a false response signifies that the image is not blurred and possesses clear, defined details.

```js

import { isImageBlurred } from 'image-processing-sdk';

// ...

const imageBlur = async () => {
  const result = await isImageBlurred('some url');
  console.log('result', result)
}
```

### 3. Image to PDF

To use the image to PDF conversion capabilities of our SDK, you can utilize the createImagesToPdf function. This function allows you to effortlessly convert a collection of images into a single PDF document. Simply provide an array of image URLs as input, and upon invoking this function, the uploaded images will be seamlessly merged and transformed into a downloadable PDF file.

```js
import { createImagesToPdf } from 'image-processing-sdk';

// ...
 createImagesToPdf(['array of image urls'])
        .then((path: any) => console.log(`PDF created successfully: ${path}`))
        .catch((error: any) => console.log(`Failed to create PDF: ${error}`));

```

When you invoke the createImagesToPdf function with the array of image URLs, the SDK performs a seamless conversion process, combining the individual images into a cohesive PDF document.You can additionally also pass outputFileName and outputDirectory as input to define the name of the downloaded PDF and its location. Once the conversion process is complete, the resulting PDF file will be made available for download on the user's system.

<!-- TABLE_GENERATE_START -->

| key           | Description                            | Example Value
| ------------- | -------------------------------------  | ---------------------------- |
| formats    | Only image formats will be supported       | .jpg, .png, .jpeg
| platform      | Platform of the user                   | android

<!-- TABLE_GENERATE_END -->

### I want to know more!

No worries, here are some links that you will find useful:
* **[Advanced React Native Guide](https://www.madstreetden.com/industry-solutions/)**
* **[Sample app](https://github.com/abhay-keyvalue/MSD-sample-app)**

Have any questions? Reach out to MSD [Support](https://www.madstreetden.com/contact-us/) to speak to someone smart, quickly.