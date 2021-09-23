package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * A data class for containing position related values of imageview.
 * The values are used for calculating scale and delta of bitmap shader matrix.
 *
 * @param usableWidth The width size that image should be drawn.
 * @param usableHeight The height size that image should be drawn.
 * @param paddingLeft Left padding size of imageview.
 * @param paddingTop Top padding size of imageview.
 * @param paddingRight Right padding size of imageview.
 * @param paddingBottom Bottom padding size of imageview.
 * @param borderAdjustment Adjustment size for border of imageview.
 * @param shadowAdjustment Adjustment size for shadow of imageview.
 */
internal data class Bounds(
    val usableWidth: Float,
    val usableHeight: Float,
    val paddingLeft: Int = 0,
    val paddingTop: Int = 0,
    val paddingRight: Int = 0,
    val paddingBottom: Int = 0,
    val borderAdjustment: Float = 0f,
    val shadowAdjustment: Float = 0f
)

/**
 * Create a bitmap shader matrix for center crop scale type.
 *
 * @param bitmap The bitmap object to be drawn.
 * @param bounds The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for center crop.
 */
internal fun createCenterCropMatrix(
    bitmap: Bitmap,
    bounds: Bounds
): Matrix = Matrix().apply {
    val width = bounds.usableWidth
    val height = bounds.usableHeight
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

    val dxAdjustment = bounds.paddingLeft + bounds.borderAdjustment + bounds.shadowAdjustment
    val dyAdjustment = bounds.paddingTop + bounds.borderAdjustment + bounds.shadowAdjustment
    postTranslate(dx + dxAdjustment, dy + dyAdjustment)
}
