@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
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
     * The radius of the imageview's top-left.
     * Its unit is pixel.
     */
    var topLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * The radius of the imageview's top-right.
     * Its unit is pixel.
     */
    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * The radius of the imageview's bottom-right.
     * Its unit is pixel.
     */
    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * The radius of the imageview's bottom-left.
     * Its unit is pixel.
     */
    var bottomLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Rectangle bounds of image to be drawn. */
    private val imageRect: RectF = RectF()

    /**
     * The radius of the border's top-left.
     * Its unit is pixel.
     */
    private var borderTopLeftRadius: Float = defaultRadius

    /**
     * The radius of the border's top-right.
     * Its unit is pixel.
     */
    private var borderTopRightRadius: Float = defaultRadius

    /**
     * The radius of the border's bottom-right.
     * Its unit is pixel.
     */
    private var borderBottomRightRadius: Float = defaultRadius

    /**
     * The radius of the border's bottom-left.
     * Its unit is pixel.
     */
    private var borderBottomLeftRadius: Float = defaultRadius

    /** Rectangle bounds of border to be drawn. */
    private val borderRect: RectF = RectF()

    /**
     * The radius of the shadow's top-left.
     * Its unit is pixel.
     */
    private var shadowTopLeftRadius: Float = defaultRadius

    /**
     * The radius of the shadow's top-right.
     * Its unit is pixel.
     */
    private var shadowTopRightRadius: Float = defaultRadius

    /**
     * The radius of the shadow's bottom-right.
     * Its unit is pixel.
     */
    private var shadowBottomRightRadius: Float = defaultRadius

    /**
     * The radius of the shadow's bottom-left.
     * Its unit is pixel.
     */
    private var shadowBottomLeftRadius: Float = defaultRadius

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
            if (r != DEFAULT_RADIUS) {
                this.topLeftRadius = r
                this.topRightRadius = r
                this.bottomRightRadius = r
                this.bottomLeftRadius = r
            }

            val bs = if (borderEnabled) {
                this.borderSize
            } else {
                0f
            }
            this.borderTopLeftRadius = this.topLeftRadius + bs
            this.borderTopRightRadius = this.topRightRadius + bs
            this.borderBottomRightRadius = this.bottomRightRadius + bs
            this.borderBottomLeftRadius = this.bottomLeftRadius + bs

            if (shadowEnabled) {
                this.shadowTopLeftRadius = this.borderTopLeftRadius
                this.shadowTopRightRadius = this.borderTopRightRadius
                this.shadowBottomRightRadius = this.borderBottomRightRadius
                this.shadowBottomLeftRadius = this.borderBottomLeftRadius
            } else {
                this.shadowTopLeftRadius = this.topLeftRadius
                this.shadowTopRightRadius = this.topRightRadius
                this.shadowBottomRightRadius = this.bottomRightRadius
                this.shadowBottomLeftRadius = this.bottomLeftRadius
            }

            val rtl = a.getDimension(R.styleable.RoundImageView_top_left_radius, DEFAULT_RADIUS)
            if (rtl != DEFAULT_RADIUS) {
                this.topLeftRadius = rtl
                this.borderTopLeftRadius = if (borderEnabled) {
                    rtl + this.borderSize
                } else {
                    rtl
                }
                this.shadowTopLeftRadius = if (shadowEnabled) {
                    this.borderTopLeftRadius
                } else {
                    rtl
                }
            }

            val rtr = a.getDimension(R.styleable.RoundImageView_top_right_radius, DEFAULT_RADIUS)
            if (rtr != DEFAULT_RADIUS) {
                this.topRightRadius = rtr
                this.borderTopRightRadius = if (borderEnabled) {
                    rtr + this.borderSize
                } else {
                    rtr
                }
                this.shadowTopRightRadius = if (shadowEnabled) {
                    this.borderTopRightRadius
                } else {
                    rtr
                }
            }

            val rbr = a.getDimension(R.styleable.RoundImageView_bottom_right_radius, DEFAULT_RADIUS)
            if (rbr != DEFAULT_RADIUS) {
                this.bottomRightRadius = rbr
                this.borderBottomRightRadius = if (borderEnabled) {
                    rbr + this.borderSize
                } else {
                    rbr
                }
                this.shadowBottomRightRadius = if (shadowEnabled) {
                    this.borderBottomRightRadius
                } else {
                    rbr
                }
            }

            val rbl = a.getDimension(R.styleable.RoundImageView_bottom_left_radius, DEFAULT_RADIUS)
            if (rbl != DEFAULT_RADIUS) {
                this.bottomLeftRadius = rbl
                this.borderBottomLeftRadius = if (borderEnabled) {
                    rbl + this.borderSize
                } else {
                    rbl
                }
                this.shadowBottomLeftRadius = if (shadowEnabled) {
                    this.borderBottomLeftRadius
                } else {
                    rbl
                }
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
            canvas.drawRoundRect(
                shadowRect,
                shadowTopLeftRadius,
                shadowTopRightRadius,
                shadowBottomRightRadius,
                shadowBottomLeftRadius,
                shadowPaint
            )
        }

        if (borderEnabled) {
            canvas.drawRoundRect(
                borderRect,
                borderTopLeftRadius,
                borderTopRightRadius,
                borderBottomRightRadius,
                borderBottomLeftRadius,
                borderPaint
            )
        }

        canvas.drawRoundRect(
            imageRect,
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            imagePaint
        )
    }

    /**
     * Draw the specified round-rect using the specified paint.
     *
     * @param rect The rectangular bounds of the round-rect to be drawn.
     * @param rtl The radius of top-left of the round-rect.
     * @param rtr The radius of top-right of the round-rect.
     * @param rbr The radius of bottom-right of the round-rect.
     * @param rbl The radius of bottom-left of the round-rect.
     * @param paint The paint used to draw the round-rect.
     */
    private fun Canvas.drawRoundRect(rect: RectF, rtl: Float, rtr: Float, rbr: Float, rbl: Float, paint: Paint) {
        val path = Path()

        path.apply {
            moveTo(rect.left, rect.top + rtl)
            quadTo(rect.left, rect.top, rect.left + rtl, rect.top)
            lineTo(rect.right - rtr, rect.top)
            quadTo(rect.right, rect.top, rect.right, rect.top + rtr)
            lineTo(rect.right, rect.bottom - rbr)
            quadTo(rect.right, rect.bottom, rect.right - rbr, rect.bottom)
            lineTo(rect.left + rbl, rect.bottom)
            quadTo(rect.left, rect.bottom, rect.left, rect.bottom - rbl)
            lineTo(rect.left, rect.top + rtl)
        }.close()

        this.drawPath(path, paint)
    }
}
