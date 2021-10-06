package io.woong.shapedimageview.util

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

    val paddingHorizontal: Float
        get() = paddingLeft + paddingRight

    val paddingVertical: Float
        get() = paddingTop + paddingBottom

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
