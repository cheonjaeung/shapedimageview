package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Matrix
import io.woong.shapedimageview.ShapedImageView

/**
 * A data class for containing position related values of imageview.
 * The values are used for calculating scale and delta of bitmap shader matrix.
 *
 * @param width The view's width size.
 * @param height The view's height size.
 * @param paddings The array of the view's padding.
 * @param borderSize The view's border size. If border is disabled, set it 0.
 * @param shadowSize The view's shadow size. If shadow is disabled, set it 0.
 */
internal data class Bounds(
    val width: Float,
    val height: Float,
    val paddings: FloatArray,
    val borderSize: Float,
    val shadowSize: Float
) {
    companion object {
        /**
         * Create a new [Bounds] from given [ShapedImageView].
         */
        fun from(view: ShapedImageView): Bounds = Bounds(
            view.width.toFloat(),
            view.height.toFloat(),
            floatArrayOf(
                view.paddingLeft.toFloat(),
                view.paddingTop.toFloat(),
                view.paddingRight.toFloat(),
                view.paddingBottom.toFloat()
            ),
            if (view.borderEnabled) view.borderSize else 0f,
            if (view.shadowEnabled) view.shadowSize else 0f
        )
    }

    val paddingLeft: Float
        get() = paddings[0]

    val paddingTop: Float
        get() = paddings[1]

    val paddingRight: Float
        get() = paddings[2]

    val paddingBottom: Float
        get() = paddings[3]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bounds

        if (width != other.width) return false
        if (height != other.height) return false
        if (!paddings.contentEquals(other.paddings)) return false
        if (borderSize != other.borderSize) return false
        if (shadowSize != other.shadowSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + paddings.contentHashCode()
        result = 31 * result + borderSize.hashCode()
        result = 31 * result + shadowSize.hashCode()
        return result
    }
}

/**
 * Create a bitmap shader matrix for fit-xy scale type.
 *
 * @param img The bitmap object to be drawn.
 * @param b The [Bounds] object that containing bounds of imageview.
 *
 * @return A matrix object for fit-xy scale type.
 */
internal fun createFitXYMatrix(img: Bitmap, b: Bounds): Matrix = Matrix().apply {
    val width = b.usableWidth - (b.borderAdjustment * 2) - (b.shadowAdjustment * 2)
    val height = b.usableHeight - (b.borderAdjustment * 2) - (b.shadowAdjustment * 2)
    val bitmapWidth = img.width.toFloat()
    val bitmapHeight = img.height.toFloat()

    val scaleX = width / bitmapWidth
    val scaleY = height / bitmapHeight
    setScale(scaleX, scaleY)

    val dx = (b.paddingLeft + (b.borderAdjustment * 2) + (b.shadowAdjustment * 2)) * scaleX
    val dy = (b.paddingTop + (b.borderAdjustment * 2) + (b.shadowAdjustment * 2)) * scaleY
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
    val width = b.usableWidth
    val height = b.usableHeight
    val ratio = width / height

    val bitmapWidth = img.width.toFloat()
    val bitmapHeight = img.height.toFloat()
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

    val dxAdjustment = b.paddingLeft + b.borderAdjustment + b.shadowAdjustment
    val dyAdjustment = b.paddingTop + b.borderAdjustment + b.shadowAdjustment
    postTranslate(dx + dxAdjustment, dy + dyAdjustment)
}
