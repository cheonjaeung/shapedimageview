<img alt="logo" src="./image/logo/shapedimageview_logo_squircle_dark.png" align="right" width="150">

# ShapedImageView &middot; ![platform-android](https://img.shields.io/badge/Platform-Android-green?logo=android&logoColor=green) ![android-sdk](https://img.shields.io/badge/SDK-16%2B-green?logo=android&logoColor=green) [![build-status](https://github.com/woongdev/ShapedImageView/actions/workflows/build.yml/badge.svg)](https://github.com/woongdev/ShapedImageView/actions/workflows/build.yml) [![maven-central](https://img.shields.io/maven-central/v/io.woong.shapedimageview/shapedimageview?label=Maven%20Central&logo=apache%20maven&logoColor=orange)](https://search.maven.org/artifact/io.woong.shapedimageview/shapedimageview) [![license](https://img.shields.io/badge/License-MIT-blue?logo=apache&logoColor=blue)](./LICENSE) [![google-dev-library](https://img.shields.io/badge/Google%20Dev%20Library-4285F4?logo=google&logoColor=white)](https://devlibrary.withgoogle.com/products/android/repos/woongdev-ShapedImageView) [![maintainer](https://img.shields.io/badge/%3C%2F%3E%20by-Jaewoong%20Cheon-A97BFF.svg)](https://github.com/woongdev)

ShapedImageView is an Android library which provides a simple way to use various shapes of imagview.
It contains some predefined shapes like oval, round-rectangle or cut-corner-rectangle.
And it support custom shape API.

## Features

- **Various Shape ImageViews:** There are various imageviews.
For instance, OvalImageView or CutCornerImageView.
- **Custom Shape Support:** This library contains FormulableImageView.
It can accept formula to draw custom image shape.
You can use predefined formula or you can create your custom formula by inherit interface.
- **Border and Shadow:** All imageviews in this library have border and shadow related attributes.

## Limitation

- **Only Center-Crop Scale Type Support:** This library support only `center crop` scale type.
All imageviews in this library set scale type to `center crop` at initializing.
If you try to change scale type, it will make an exception.

## Installation

```groovy
implementation "io.woong.shapedimageview:shapedimageview:$version"
```

## Getting Started

WIP

## Documentation

WIP

## License

ShapedImageView by [Jaewoong Cheon](https://github.com/woongdev) is licensed under the [MIT License](./LICENSE).