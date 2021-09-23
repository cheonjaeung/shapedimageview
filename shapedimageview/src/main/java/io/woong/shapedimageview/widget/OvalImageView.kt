package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import io.woong.shapedimageview.R
import io.woong.shapedimageview.ShapedImageView

/**
 * The shaped image view that draw image in oval shape.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class OvalImageView @JvmOverloads constructor(
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
        val a = context.obtainStyledAttributes(attrs, R.styleable.OvalImageView, defStyle, 0)

        try {
            this.borderSize = a.getDimension(R.styleable.OvalImageView_border_size, DEFAULT_BORDER_SIZE)
            this.borderColor = a.getColor(R.styleable.OvalImageView_border_color, DEFAULT_BORDER_COLOR)
            this.borderEnabled = a.getBoolean(R.styleable.OvalImageView_border_enabled, DEFAULT_BORDER_ENABLED)
            this.shadowSize = a.getDimension(R.styleable.OvalImageView_shadow_size, DEFAULT_SHADOW_SIZE)
            this.shadowColor = a.getColor(R.styleable.OvalImageView_shadow_color, DEFAULT_SHADOW_COLOR)
            this.shadowEnabled = a.getBoolean(R.styleable.OvalImageView_shadow_enabled, DEFAULT_SHADOW_ENABLED)
        } finally {
            a.recycle()
        }
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
            canvas.drawOval(shadowRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawOval(borderRect, borderPaint)
        }

        canvas.drawOval(imageRect, imagePaint)
    }
}
