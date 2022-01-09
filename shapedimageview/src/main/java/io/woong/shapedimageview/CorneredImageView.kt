package io.woong.shapedimageview

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue

/**
 * An abstract class to define common codes of 4 cornered image views like corner radius.
 *
 * All imageviews that inherit from this class have radius related xml attribute.
 *
 * For setting all radius to same size, use `radius` attribute.
 * Or to set each radius to different size, following below:
 * - top left: `top_left_radius`
 * - top right: `top_right_radius`
 * - bottom right: `bottom_right_radius`
 * - bottom left: `bottom_left_radius`
 *
 * @see RoundImageView
 * @see RoundSquareImageView
 * @see CutCornerImageView
 * @see CutCornerSquareImageView
 */
abstract class CorneredImageView : ShapedImageView {
    private val defaultRadius: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            resources.displayMetrics
        )

    override var borderEnabled: Boolean = DEFAULT_BORDER_ENABLED
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    override var shadowEnabled: Boolean = DEFAULT_SHADOW_ENABLED
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * Top left radius of the imageview in pixel unit.
     */
    var topLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * Top right radius of the imageview in pixel unit,.
     */
    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * Bottom right radius of the imageview in pixel unit.
     */
    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /**
     * Bottom left radius of the imageview in pixel unit.
     */
    var bottomLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    protected val borderTopLeftRadius: Float
        get() {
            var radius = topLeftRadius
            if (borderEnabled) {
                radius += borderSize
            }
            return radius
        }
    protected val borderTopRightRadius: Float
        get() {
            var radius = topRightRadius
            if (borderEnabled) {
                radius += borderSize
            }
            return radius
        }
    protected val borderBottomRightRadius: Float
        get() {
            var radius = bottomRightRadius
            if (borderEnabled) {
                radius += borderSize
            }
            return radius
        }
    protected val borderBottomLeftRadius: Float
        get() {
            var radius = bottomLeftRadius
            if (borderEnabled) {
                radius += borderSize
            }
            return radius
        }

    protected val shadowTopLeftRadius: Float get() = borderTopLeftRadius
    protected val shadowTopRightRadius: Float get() = borderTopRightRadius
    protected val shadowBottomRightRadius: Float get() = borderBottomRightRadius
    protected val shadowBottomLeftRadius: Float get() = borderBottomLeftRadius

    /**
     * Radius of the imageview in pixel unit.
     *
     * It returns top left corner radius when getting.
     * And it sets all radius to given radius when setting.
     */
    var radius: Float
        get() = topLeftRadius
        set(value) {
            topLeftRadius = value
            topRightRadius = value
            bottomRightRadius = value
            bottomLeftRadius = value
            measureBounds()
            invalidate()
        }

    /**
     * Radius array of the imageview in pixel unit.
     *
     * Each index means the following:
     * - index 0: top left
     * - index 1: top right
     * - index 2: bottom right
     * - index 3: bottom left
     */
    var radii: FloatArray
        get() = floatArrayOf(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius)
        set(value) {
            topLeftRadius = value[0]
            topRightRadius = value[1]
            bottomRightRadius = value[2]
            bottomLeftRadius = value[3]
            measureBounds()
            invalidate()
        }

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)

    final override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CorneredImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.CorneredImageView_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_top_left_radius, defaultRadius)
                radius = r
            }

            if (a.hasValue(R.styleable.CorneredImageView_top_left_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_top_left_radius, defaultRadius)
                topLeftRadius = r
            }

            if (a.hasValue(R.styleable.CorneredImageView_top_right_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_top_right_radius, defaultRadius)
                topRightRadius = r
            }

            if (a.hasValue(R.styleable.CorneredImageView_bottom_right_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_bottom_right_radius, defaultRadius)
                bottomRightRadius = r
            }

            if (a.hasValue(R.styleable.CorneredImageView_bottom_left_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_bottom_left_radius, defaultRadius)
                bottomLeftRadius = r
            }
        } finally {
            a.recycle()
        }
    }
}