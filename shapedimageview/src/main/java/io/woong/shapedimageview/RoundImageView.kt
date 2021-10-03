@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.util.drawRoundRect

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

    /** The radius of the imageview's top-left in pixel unit. */
    var topLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The radius of the imageview's top-right in pixel unit. */
    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The radius of the imageview's bottom-right in pixel unit. */
    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The radius of the imageview's bottom-left in pixel unit. */
    var bottomLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * Set the all corner's radius size of the imageview in pixel unit.
     *
     * @param radius The pixel radius of the imageview's corners.
     */
    fun setRadii(radius: Float) {
        this.topLeftRadius = radius
        this.topRightRadius = radius
        this.bottomRightRadius = radius
        this.bottomLeftRadius = radius
    }

    /**
     * Set the all corner's radius size of the imageview in pixel unit.
     *
     * @param topLeft The top-left corner radius size in pixel.
     * @param topRight The top-right corner radius size in pixel.
     * @param bottomRight The bottom-right corner radius size in pixel.
     * @param bottomLeft The bottom-left corner radius size in pixel.
     */
    fun setRadii(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        this.topLeftRadius = topLeft
        this.topRightRadius = topRight
        this.bottomRightRadius = bottomRight
        this.bottomLeftRadius = bottomLeft
    }

    /**
     * Set the all corner's radius size of the imageview in pixel unit.
     *
     * @param radii The array of the 4 corner's radius.
     * The order is left, top, right and bottom.
     */
    fun setRadii(radii: FloatArray) {
        if (radii.size != 4) {
            throw IllegalArgumentException("setRadii's parameter should be an array or list that has 4 items.")
        }
        this.topLeftRadius = radii[0]
        this.topRightRadius = radii[1]
        this.bottomRightRadius = radii[2]
        this.bottomLeftRadius = radii[3]
    }

    /** The radius of the border's top-left in pixel unit. */
    private var borderTopLeftRadius: Float = defaultRadius

    /** The radius of the border's top-right in pixel unit. */
    private var borderTopRightRadius: Float = defaultRadius

    /** The radius of the border's bottom-right in pixel unit. */
    private var borderBottomRightRadius: Float = defaultRadius

    /** The radius of the border's bottom-left in pixel unit. */
    private var borderBottomLeftRadius: Float = defaultRadius

    /** The radius of the shadow's top-left in pixel unit. */
    private var shadowTopLeftRadius: Float = defaultRadius

    /** The radius of the shadow's top-right in pixel unit. */
    private var shadowTopRightRadius: Float = defaultRadius

    /** The radius of the shadow's bottom-right in pixel unit. */
    private var shadowBottomRightRadius: Float = defaultRadius

    /** The radius of the shadow's bottom-left in pixel unit. */
    private var shadowBottomLeftRadius: Float = defaultRadius

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0)

        try {
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
}
