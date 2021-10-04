@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.util.drawRoundRect

/**
 * The shaped image view that draw image in round rectangle shape.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.RoundImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 *
 * In [RoundImageView], there are some attributes to configure this imageview.
 * The most important attribute is `radius`.
 *
 * ```
 *      <!-- To apply all corner radius in one line. -->
 *      <io.woong.shapedimageview.RoundImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:radius="32dp" />
 *
 *      <!-- To apply each corner radius. -->
 *      <io.woong.shapedimageview.RoundImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:top_left_radius="8dp"
 *          app:top_right_radius="12dp"
 *          app:bottom_right_radius="16dp"
 *          app:bottom_left_radius="24dp" />
 * ```
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
            setBorderAndShadowRadii()
            invalidate()
        }

    /** The radius of the imageview's top-right in pixel unit. */
    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowRadii()
            invalidate()
        }

    /** The radius of the imageview's bottom-right in pixel unit. */
    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowRadii()
            invalidate()
        }

    /** The radius of the imageview's bottom-left in pixel unit. */
    var bottomLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowRadii()
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
        setBorderAndShadowRadii()
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
        setBorderAndShadowRadii()
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
        setBorderAndShadowRadii()
    }

    /** The radius of the border's top-left in pixel unit. */
    private var borderTopLeftRadius: Float = topLeftRadius + borderSize

    /** The radius of the border's top-right in pixel unit. */
    private var borderTopRightRadius: Float = topRightRadius + borderSize

    /** The radius of the border's bottom-right in pixel unit. */
    private var borderBottomRightRadius: Float = bottomRightRadius + borderSize

    /** The radius of the border's bottom-left in pixel unit. */
    private var borderBottomLeftRadius: Float = bottomLeftRadius + borderSize

    /** The radius of the shadow's top-left in pixel unit. */
    private var shadowTopLeftRadius: Float = if (borderEnabled) {
        borderTopLeftRadius
    } else {
        topLeftRadius
    }

    /** The radius of the shadow's top-right in pixel unit. */
    private var shadowTopRightRadius: Float = if (borderEnabled) {
        borderTopRightRadius
    } else {
        topRightRadius
    }

    /** The radius of the shadow's bottom-right in pixel unit. */
    private var shadowBottomRightRadius: Float = if (borderEnabled) {
        borderBottomRightRadius
    } else {
        bottomRightRadius
    }

    /** The radius of the shadow's bottom-left in pixel unit. */
    private var shadowBottomLeftRadius: Float = if (borderEnabled) {
        borderBottomLeftRadius
    } else {
        bottomLeftRadius
    }

    /**
     * Change border radii and shadow radii.
     */
    private fun setBorderAndShadowRadii() {
        if (borderEnabled) {
            val topLeft = topLeftRadius + borderSize
            borderTopLeftRadius = topLeft
            shadowTopLeftRadius = topLeft

            val topRight = topRightRadius + borderSize
            borderTopRightRadius = topRight
            shadowTopRightRadius = topRight

            val bottomRight = bottomRightRadius + borderSize
            borderBottomRightRadius = bottomRight
            shadowBottomRightRadius = bottomRight

            val bottomLeft = bottomLeftRadius + borderSize
            borderBottomLeftRadius = bottomLeft
            shadowBottomLeftRadius = bottomLeft
        } else {
            shadowTopLeftRadius = topLeftRadius
            shadowTopRightRadius = topRightRadius
            shadowBottomRightRadius = bottomRightRadius
            shadowBottomLeftRadius = bottomLeftRadius
        }
    }

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.RoundImageView_radius)) {
                val r = a.getDimension(R.styleable.RoundImageView_radius, defaultRadius)
                setRadii(r)
            }

            if (a.hasValue(R.styleable.RoundImageView_top_left_radius)) {
                topLeftRadius = a.getDimension(R.styleable.RoundImageView_top_left_radius, defaultRadius)
            }

            if (a.hasValue(R.styleable.RoundImageView_top_right_radius)) {
                topRightRadius = a.getDimension(R.styleable.RoundImageView_top_right_radius, defaultRadius)
            }

            if (a.hasValue(R.styleable.RoundImageView_bottom_right_radius)) {
                bottomRightRadius = a.getDimension(R.styleable.RoundImageView_bottom_right_radius, defaultRadius)
            }

            if (a.hasValue(R.styleable.RoundImageView_bottom_left_radius)) {
                bottomLeftRadius = a.getDimension(R.styleable.RoundImageView_bottom_left_radius, defaultRadius)
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
