package io.woong.shapedimageview

import android.content.Context
import android.util.AttributeSet
import kotlin.math.min

/**
 * The parent imageview of all child imageview that has same width and height size.
 *
 * This class cannot be used alone cause it is abstract class.
 * To use shaped imageview, use classes that inherit from this class.
 *
 * The usage of this abstract class is same to [ShapedImageView].
 *
 * @see ShapedImageView
 */
abstract class SquareShapedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /**
     * A lifecycle method for measuring this view's size
     * and set width and height same.
     *
     * In this method, it measures the views width and height and call [measureBounds].
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val s = min(w, h)
        setMeasuredDimension(s, s)

        val borderAdjustment = if (borderEnabled) borderSize else 0f
        val shadowAdjustment = if (shadowEnabled) shadowSize else 0f
        val adjustmentSum = borderAdjustment + shadowAdjustment

        this.usableWidth = s - this.paddingLeft - this.paddingRight - adjustmentSum
        this.usableHeight = s - this.paddingTop - this.paddingBottom - adjustmentSum

        measureBounds(s.toFloat(), s.toFloat())
    }
}
