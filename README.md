<img alt="logo" src="./image/logo/shapedimageview_logo_squircle_dark.png" align="right" width="150">

# ShapedImageView

![platform-android](https://img.shields.io/badge/Platform-Android-green?logo=android&logoColor=green) ![android-sdk](https://img.shields.io/badge/SDK-16%2B-green?logo=android&logoColor=green) [![build-status](https://github.com/woongdev/ShapedImageView/actions/workflows/build.yml/badge.svg)](https://github.com/woongdev/ShapedImageView/actions/workflows/build.yml) [![maven-central](https://img.shields.io/maven-central/v/io.woong.shapedimageview/shapedimageview?label=Maven%20Central&logo=apache%20maven&logoColor=orange)](https://search.maven.org/artifact/io.woong.shapedimageview/shapedimageview) [![license](https://img.shields.io/badge/License-MIT-blue?logo=apache&logoColor=blue)](./LICENSE) [![google-dev-library](https://img.shields.io/badge/Google%20Dev%20Library-4285F4?logo=google&logoColor=white)](https://devlibrary.withgoogle.com/products/android/repos/woongdev-ShapedImageView) [![maintainer](https://img.shields.io/badge/%3C%2F%3E%20by-Jaewoong%20Cheon-A97BFF.svg)](https://github.com/woongdev)

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

<img alt="preview1" src="./image/preview/getting_started.jpeg" width="300">

Let's create a imageview like sample image.
You can get this shape in simple way using below code.

```xml
<io.woong.shapedimageview.widget.OvalImageView
    android:layout_width="300dp"
    android:layout_height="200dp"
    android:src="@drawable/sample" />
```

## Documentation

- [Shapes](#shapes)
- [Common attributes](#common-attributes)
- [Attributes for specific view](#attributes-of-specific-view)
- [Custom shape](#custom-shape)
- [Predefined formula](#predefined-formula)

### Shapes

In this library, there are various predefined shaped imageviews.
They are `OvalImageView`, `RoundImageView` and `CutCornerImageView`.

- **OvalImageView:** oval (ellipse) shape.
- **RoundImageView:** rounded rectangle shape.
- **CutCornerImageView:** corner cut rectangle shape.

All imageview's scale type is always `center-crop`.
If you try to change it, the view will make exception.

### Common attributes

All shaped imageviews have common attributes.

| Attribute | Type | Default |
| --- | --- | --- |
| border_size | Dimension | 0 |
| border_color | Color | #444444 |
| border_enabled | Boolean | true |
| shadow_size | Dimension | 0 |
| shadow_color | Color | #888888 |
| shadow_enabled | Boolean | true |

By using attributes, you can set border or shadow of imageview.

### Attributes of specific view

Some imageviews have other attributes.

#### RoundImageView

| Attribute | Type | Default |
| --- | --- | --- |
| radius | Dimension | 16dp |
| top_left_radius | Dimension | 16dp |
| top_right_radius | Dimension | 16dp |
| bottom_right_radius| Dimension | 16dp |
| bottom_left_radius | Dimension | 16dp |

`radius` has the lowest priority.
It means you can override radius value using specific corner radius attribute.

#### CutCornerImageView

| Attribute | Type | Default |
| --- | --- | --- |
| cut_size | Dimension | 16dp |
| top_left_cut_size | Dimension | 16dp |
| top_right_cut_size | Dimension | 16dp |
| bottom_right_cut_size| Dimension | 16dp |
| bottom_left_cut_size | Dimension | 16dp |

`cut_size` has the lowest priority.
It means you can override cut size value using specific corner cut size attribute.

### Custom Shape

ShapedImageView support custom shape imageview.
There is `FormulableImageView` and `Formula` to draw custom shape.

The Formula interface is a kind of mathematical function.
It can accept angle and it has a method that returns the x and y coordinate position for current angle.

FormulableImageView draw a shape while changing the angle from 0 to 360 degrees in the given Formula.

To use Formula, create a class that inherit from Formula interface and set it to FormulableImageView.

```kotlin
class CustomFormula : Formula {
    override var degree: Float = 0f
        set(value) {
            field = if (value > 360) {
                value % 360
            } else {
                value
            }
        }

    override var rect: RectF = RectF()

    override fun getX(): Float {
        ...
    }

    override fun getY(): Float {
        ...
    }
}
```

```kotlin
val iv = findViewById<FormulableImageView>(R.id.iv)
iv.formula = CustomFormula()
```

### Predefined formula

This library provide `SuperEllipseFormula`.
It is a formula for drawing [super ellipse](https://en.wikipedia.org/wiki/Superellipse) shape.
You can apply it to `FormulableImageView`.

## License

ShapedImageView is licensed under the [MIT License](./LICENSE).