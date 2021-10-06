package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.RequiresApi

/**
 * Create a bitmap shader matrix for fit-xy scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for fit-xy scale type.
 */
internal fun createFitXYMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)

    val sx = w / img.width
    val sy = h / img.height
    setScale(sx, sy)

    val dx = b.paddingLeft + b.borderSize + b.shadowSize
    val dy = b.paddingTop + b.borderSize + b.shadowSize
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for fit-start scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for fit-start scale type.
 */
@RequiresApi(api = 31)
internal fun createFitStartMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()
    val ratio = w / h
    val imgRatio = iw / ih

    val scale = if (ratio > imgRatio) {
        h / ih
    } else {
        w / iw
    }
    setScale(scale, scale)

    val dx = b.paddingLeft + b.borderSize + b.shadowSize
    val dy = b.paddingTop + b.borderSize + b.shadowSize
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for fit-center scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for fit-center scale type.
 */
@RequiresApi(api = 31)
internal fun createFitCenterMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()
    val ratio = w / h
    val imgRatio = iw / ih

    val scale: Float
    val dx: Float
    val dy: Float
    if (ratio > imgRatio) {
        scale = h / ih
        dx = (w - (iw * scale)) / 2 + b.paddingRight + b.borderSize + b.shadowSize
        dy = b.paddingTop + b.borderSize + b.shadowSize
    } else {
        scale = w / iw
        dx = b.paddingLeft + b.borderSize + b.shadowSize
        dy = (h - (ih * scale)) / 2 + b.paddingBottom + b.borderSize + b.shadowSize
    }
    setScale(scale, scale)
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for fit-end scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for fit-end scale type.
 */
@RequiresApi(api = 31)
internal fun createFitEndMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()
    val ratio = w / h
    val imgRatio = iw / ih

    val scale: Float
    val dx: Float
    val dy: Float
    if (ratio > imgRatio) {
        scale = h / ih
        dx = w - (iw * scale) + b.paddingRight + b.borderSize + b.shadowSize
        dy = b.paddingTop + b.borderSize + b.shadowSize
    } else {
        scale = w / iw
        dx = b.paddingLeft + b.borderSize + b.shadowSize
        dy = h - (ih * scale) + b.paddingBottom + b.borderSize + b.shadowSize
    }
    setScale(scale, scale)
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for center scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for center scale type.
 */
@RequiresApi(api = 31)
internal fun createCenterMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()

    val dx = (w - iw) / 2 + b.paddingLeft + b.borderSize + b.shadowSize
    val dy = (h - ih) / 2 + b.paddingTop + b.borderSize + b.shadowSize
    postScale(1f, 1f)
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for center-crop scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for center-crop scale type.
 */
internal fun createCenterCropMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()
    val ratio = w / h
    val imgRatio = iw / ih

    val shouldScaleByWidth = if (ratio > imgRatio) {
        iw > ih
    } else {
        iw <= ih
    }

    val scale: Float
    val dx: Float
    val dy: Float
    if (shouldScaleByWidth) {
        scale = w / iw
        dx = b.paddingLeft + b.borderSize + b.shadowSize
        dy = (h - (ih * scale)) / 2 + b.paddingTop + b.borderSize + b.shadowSize
    } else {
        scale = h / ih
        dx = (w - (iw * scale)) / 2 + b.paddingLeft + b.borderSize + b.shadowSize
        dy = b.paddingTop + b.borderSize + b.shadowSize
    }
    postScale(scale, scale)
    postTranslate(dx, dy)
}

/**
 * Create a bitmap shader matrix for center-inside scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for center-inside scale type.
 */
@RequiresApi(api = 31)
internal fun createCenterInsideMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val w = b.width - b.paddingHorizontal - (b.borderSize * 2) - (b.shadowSize * 2)
    val h = b.height - b.paddingVertical - (b.borderSize * 2) - (b.shadowSize * 2)
    val iw = img.width.toFloat()
    val ih = img.height.toFloat()
    val ratio = w / h
    val imgRatio = iw / ih

    val scale: Float
    val dx: Float
    val dy: Float
    when {
        ratio < imgRatio && iw > w -> {
            scale = w / iw
            dx = b.paddingLeft + b.borderSize + b.shadowSize
            dy = (h - (ih * scale)) / 2 + b.paddingTop + b.borderSize + b.shadowSize
        }
        ratio > imgRatio && ih > h -> {
            scale = h / ih
            dx = (w - (iw * scale)) / 2 + b.paddingLeft + b.borderSize + b.shadowSize
            dy = b.paddingTop + b.borderSize + b.shadowSize
        }
        else -> {
            scale = 1f
            dx = (w - iw) / 2 + b.paddingLeft + b.borderSize + b.shadowSize
            dy = (h - ih) / 2 + b.paddingTop + b.borderSize + b.shadowSize
        }
    }
    postScale(scale, scale)
    postTranslate(dx, dy)
}
