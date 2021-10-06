package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.RequiresApi
import kotlin.math.abs

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
 * Create a bitmap shader matrix for center-crop scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for center-crop scale type.
 */
internal fun createCenterCropMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
//    val width = b.usableWidth
//    val height = b.usableHeight
//    val ratio = width / height
//
//    val bitmapWidth = img.width.toFloat()
//    val bitmapHeight = img.height.toFloat()
//    val bitmapRatio = bitmapWidth / bitmapHeight
//
//    val scale: Float
//    val dx: Float
//    val dy: Float
//
//    /**
//     * A local function for checking is this bitmap need to scale by width.
//     *
//     * Basically, when bitmap height is smaller than width, it need to scale by height.
//     * Because bitmap height is smaller.
//     * Likewise, when width is smaller, it need to scale by width.
//     *
//     * But there are 2 exceptions.
//     * When bitmap height is smaller than width, but imageview's width is too wide,
//     * empty spaces are created.
//     * In this case, bitmap needs to scale by width, not height.
//     *
//     * Like above case, bitmap width is smaller than height but imageview's height is too big,
//     * the bitmap needs to scale by height.
//     */
//    @Suppress("RedundantIf")
//    fun needToScaleByWidth(): Boolean {
//        return if (bitmapWidth > bitmapHeight) {
//            if (ratio > bitmapRatio) {
//                true
//            } else {
//                false
//            }
//        } else {
//            if (ratio > bitmapRatio) {
//                false
//            } else {
//                true
//            }
//        }
//    }
//
//    if (needToScaleByWidth()) {
//        scale = width / bitmapWidth
//        dx = 0f
//        dy = (height - bitmapHeight * scale) * 0.5f
//    } else {
//        scale = height / bitmapHeight
//        dx = (width - bitmapWidth * scale) * 0.5f
//        dy = 0f
//    }
//
//    setScale(scale, scale)
//
//    val dxAdjustment = b.paddingLeft + b.borderAdjustment + b.shadowAdjustment
//    val dyAdjustment = b.paddingTop + b.borderAdjustment + b.shadowAdjustment
//    postTranslate(dx + dxAdjustment, dy + dyAdjustment)
}
