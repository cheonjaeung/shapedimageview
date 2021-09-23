package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Create a bitmap shader matrix for center crop scale type.
 *
 * @param bitmap The bitmap object to be drawn.
 * @param width Width size of image shape in pixel.
 * @param height Height size of image shape in pixel.
 *
 * @return A matrix object for center crop.
 */
internal fun createCenterCropMatrix(
    bitmap: Bitmap,
    width: Float,
    height: Float,
    paddingLeft: Int = 0,
    paddingTop: Int = 0
): Matrix = Matrix().apply {
    val ratio = width / height

    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()
    val bitmapRatio = bitmapWidth / bitmapHeight

    val scale: Float
    val dx: Float
    val dy: Float

    /**
     * A local function for checking is this bitmap need to scale by width.
     *
     * Basically, when bitmap height is smaller than width, it need to scale by height.
     * Because bitmap height is smaller.
     * Likewise, when width is smaller, it need to scale by width.
     *
     * But there are 2 exceptions.
     * When bitmap height is smaller than width, but imageview's width is too wide,
     * empty spaces are created.
     * In this case, bitmap needs to scale by width, not height.
     *
     * Like above case, bitmap width is smaller than height but imageview's height is too big,
     * the bitmap needs to scale by height.
     */
    @Suppress("RedundantIf")
    fun needToScaleByWidth(): Boolean {
        return if (bitmapWidth > bitmapHeight) {
            if (ratio > bitmapRatio) {
                true
            } else {
                false
            }
        } else {
            if (ratio > bitmapRatio) {
                false
            } else {
                true
            }
        }
    }

    if (needToScaleByWidth()) {
        scale = width / bitmapWidth
        dx = 0f
        dy = (height - bitmapHeight * scale) * 0.5f
    } else {
        scale = height / bitmapHeight
        dx = (width - bitmapWidth * scale) * 0.5f
        dy = 0f
    }

    setScale(scale, scale)
    postTranslate(dx + paddingLeft, dy + paddingTop)
}
