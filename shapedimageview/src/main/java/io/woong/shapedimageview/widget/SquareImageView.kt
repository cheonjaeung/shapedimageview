package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import io.woong.shapedimageview.ShapedImageView
import kotlin.math.min

/**
 * The shaped image view that draw image in square shape.
 * It is just an rectangular shape imageview but it has same width and height size.
 */
class SquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    init {
        applyAttributes(attrs, defStyle)
    }

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled) {
            canvas.drawRect(shadowRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawRect(borderRect, borderPaint)
        }

        canvas.drawRect(imageRect, imagePaint)
    }
}
