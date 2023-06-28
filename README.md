# image-processing-sdk

image processing

## Installation

```sh
npm install image-processing-sdk
```

## Usage

```js
import { isImageBlurred } from 'image-processing-sdk';

// ...

const imageBlur = async () => {
  const result = await isImageBlurred('some url');
  console.log('result', result)
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
