# Changelog

# 0.3.1 - August 2nd, 2021

### Added

- So far, there's only way to access imageview's attributes is XML.
Now can get/set them in programmatic way.
    - `shadowEnabled`, `shadowSize`, `shadowAdjustEnabled`, `shadowColor` and `shadowGravity` to control shadow.
    - `borderEnabled`, `borderSize` and `borderColor` to control border.
    - `imageRadius` to control radius value of `RoundedSquareImageView`.
    - `imageCurvature` to control curvature of `SquircularImageView`.

### Fixed

- Imageview values wasn't applied after initialized.
Now it is fixed.
    - Image size wasn't applied when change shadow size after initialized.
    - Border size wasn't applied when change it's size after initialized.
    - Shadow and border shape wasn't applied when change radius or curvature after initialized in RoundedImageView and SquircularImageView.

## 0.3.0 - July 14th, 2021

### Added

- Shadow color attribute: Now can change shadow color using `shaped_imageview_shadow_color` attribute.
- Shadow gravity attribute: Now can change gravity of shadow using `shaped_imageview_shadow_gravity`.
    - There are 9 options (center, left, top, right, bottom, top left, top right, bottom left, bottom right).
- Border attributes: Now can use imageview border. You can use 3 attributes: `shaped_imageview_border_enabled`, `shaped_imageview_border_size` and `shaped_imageview_border_color`.

### Fixed

- Scale type center crop: Image paint shader's width and height was view size. It fixed to image size.

## 0.2.0 - July 10th, 2021

- Update Kotlin version to 1.5.20
- Update Android Gradle Plugin version to 4.2.2

### Added

- Imageview shadow
    - `shaped_imageview_shadow_enabled`, `shaped_imageview_shadow_size` and `shaped_imageview_shadow_adjust_enabled` attribute for controling shadow.

## 0.1.0 - July 6th, 2021

Initial public release.

### Added

- CircularImageView
- SquareImageView
- RoundedSquareImageView
- SquircularImageView