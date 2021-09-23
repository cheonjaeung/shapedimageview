@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.R
import io.woong.shapedimageview.ShapedImageView

/**
 * The shaped image view that draw image in round rectangle shape.
 */
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    companion object {
        /** The default value of [RoundImageView]'s radius. */
        const val DEFAULT_RADIUS: Float = -1f
    }

    /** The value for using when radius is [DEFAULT_RADIUS]. */
    private val defaultRadius: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        16f,
        this.resources.displayMetrics
    )

    /**
     * The radius of this imageview.
     * Its unit is pixel.
     */
    var radius: Float = 0f
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Rectangle bounds of image to be drawn. */
    private val imageRect: RectF = RectF()

    /**
     * The radius of this imageview's border.
     * Its unit is pixel.
     */
    var borderRadius: Float = 0f
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Rectangle bounds of border to be drawn. */
    private val borderRect: RectF = RectF()

    /**
     * The radius of this imageview's shadow.
     * Its unit is pixel.
     */
    var shadowRadius: Float = 0f
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Rectangle bounds of shadow to be drawn. */
    private val shadowRect: RectF = RectF()

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0)

        try {
            this.borderSize = a.getDimension(R.styleable.RoundImageView_border_size, DEFAULT_BORDER_SIZE)
            this.borderColor = a.getColor(R.styleable.RoundImageView_border_color, DEFAULT_BORDER_COLOR)
            this.borderEnabled = a.getBoolean(R.styleable.RoundImageView_border_enabled, DEFAULT_BORDER_ENABLED)
            this.shadowSize = a.getDimension(R.styleable.RoundImageView_shadow_size, DEFAULT_SHADOW_SIZE)
            this.shadowColor = a.getColor(R.styleable.RoundImageView_shadow_color, DEFAULT_SHADOW_COLOR)
            this.shadowEnabled = a.getBoolean(R.styleable.RoundImageView_shadow_enabled, DEFAULT_SHADOW_ENABLED)

            val r = a.getDimension(R.styleable.RoundImageView_radius, DEFAULT_RADIUS)
            this.radius = if (r == DEFAULT_RADIUS) {
                defaultRadius
            } else {
                r
            }

            this.borderRadius = if (borderEnabled) {
                this.radius + this.borderSize
            } else {
                0f
            }

            this.shadowRadius = if (shadowEnabled) {
                this.borderRadius + this.shadowSize
            } else {
                this.radius
            }
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
            canvas.drawRoundRect(shadowRect, shadowRadius, shadowRadius, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint)
        }

        canvas.drawRoundRect(imageRect, radius, radius, imagePaint)
    }
}
