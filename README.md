<div align="center">
    <img alt="platform-android" src="https://img.shields.io/badge/Platform-Android-green?logo=android&logoColor=green">
    <img alt="sdk-16-plus" src="https://img.shields.io/badge/SDK-16%2B-green?logo=android&logoColor=green">
    <a href="./LICENSE"><img alt="license-mit" src="https://img.shields.io/badge/License-MIT-blue?logo=apache&logoColor=blue"></a>
    <h1>ShapedImageView</h1>
    <p>ShapedImageView is an open source Android library for using various shape image views easily.</p>
    <p><img alt="preview" src="./art/preview.png"></p>
</div>

## Installation

```groovy
implementation "io.woong.shapedimageview:shapedimageview:$version"
```

## Shapes

Currently, there are 4 shapes (circle, square, rounded square, squircle).

Each shaped image views are implemented in `CircularImageView`, `SquareImageView`, `RoundedSquareImageView` and `SquircularImageView`.

| Shape | View Class | Preview |
| --- | --- | --- |
| Circle | CircularImageView | <img alt="timg1" src="./art/circular.png" width="196"> |
| Square | SquareImageView | <img alt="timg2" src="./art/square.png" width="196"> |
| Rounded Square | RoundedSquareImageView | <img alt="timg3" src="./art/rounded_square.png" width="196"> |
| Squircle | SquircularImageView | <img alt="timg4" src="./art/squircular.png" width="196"> |

## Getting Started

### Layout

```xml
<io.woong.shapedimageview.widget.CircularImageView
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:src="@drawable/sample" />

<io.woong.shapedimageview.widget.SquareImageView
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:src="@drawable/sample" />

<io.woong.shapedimageview.widget.RoundedSquareImageView
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:src="@drawable/sample"
    app:shaped_imageview_radius="16dp" />

<io.woong.shapedimageview.widget.SquircularImageView
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:src="@drawable/sample"
    app:shaped_imageview_curvature="50" />
```

### Attributes

All attributes have `shaped_imageview` prefix.

| View | Attribute | Type | Default | Limitation |
| --- | --- | --- | --- | --- |
| RoundedSquareImageView | shaped_imageview_radius | Dimension | 0dp | |
| SquircularImageView | shaped_imageview_curvature | Integer | 50 | 0 ~ 100 |

## Limitations

- Scale type: Only can use `CENTER_CROP`.
- Size: Width and height size always same.

## Contributing

If you find bugs or want to suggest ideas or want to request features? Please create issues!

And always welcome to pull requests!

## Changelog

Detail changes is written in [changelog file](./CHANGELOG.md).

## License

ShapedImageView by [Jaewoong Cheon](https://github.com/woongdev) is licensed under the [MIT License](./LICENSE).
