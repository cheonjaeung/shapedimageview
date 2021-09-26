@file:Suppress("unused")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import io.woong.shapedimageview.R
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

    /** Rectangle bounds of image to be drawn. */
    private val imageRect: RectF = RectF()

    /** Rectangle bounds of border to be drawn. */
    private val borderRect: RectF = RectF()

    /** Rectangle bounds of shadow to be drawn. */
    private val shadowRect: RectF = RectF()

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyle, 0)

        try {
            this.borderSize = a.getDimension(R.styleable.SquareImageView_border_size, DEFAULT_BORDER_SIZE)
            this.borderColor = a.getColor(R.styleable.SquareImageView_border_color, DEFAULT_BORDER_COLOR)
            this.borderEnabled = a.getBoolean(R.styleable.SquareImageView_border_enabled, DEFAULT_BORDER_ENABLED)
            this.shadowSize = a.getDimension(R.styleable.SquareImageView_shadow_size, DEFAULT_SHADOW_SIZE)
            this.shadowColor = a.getColor(R.styleable.SquareImageView_shadow_color, DEFAULT_SHADOW_COLOR)
            this.shadowEnabled = a.getBoolean(R.styleable.SquareImageView_shadow_enabled, DEFAULT_SHADOW_ENABLED)
        } finally {
            a.recycle()
        }
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

    override fun measureBounds(viewWidth: Float, viewHeight: Float) {
        val shadowAdjust = if (shadowEnabled) shadowSize else 0f
        val borderAdjust = if (borderEnabled) borderSize else 0f
        val adjustSum = shadowAdjust + borderAdjust

        if (shadowEnabled) {
            this.shadowRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                viewWidth - this.paddingRight - shadowAdjust,
                viewHeight - this.paddingBottom - shadowAdjust
            )
        }

        if (borderEnabled) {
            this.borderRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                viewWidth - this.paddingRight - shadowAdjust,
                viewHeight - this.paddingBottom - shadowAdjust
            )
        }

        this.imageRect.set(
            this.paddingLeft.toFloat() + adjustSum,
            this.paddingTop.toFloat() + adjustSum,
            viewWidth - this.paddingRight - adjustSum,
            viewHeight - this.paddingBottom - adjustSum
        )
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
