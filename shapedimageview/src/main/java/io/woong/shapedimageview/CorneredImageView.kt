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

    var topLeftRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    var topRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    var bottomRightRadius: Float = defaultRadius
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

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

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle)

    @Suppress("MemberVisibilityCanBePrivate")
    final override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CorneredImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.CorneredImageView_radius)) {
                val r = a.getDimension(R.styleable.CorneredImageView_radius, defaultRadius)
                setRadius(r)
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

    /**
     * Change corner radius of the imageview.
     *
     * @param radius Radius size in pixel unit.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun setRadius(radius: Float) {
        topLeftRadius = radius
        topRightRadius = radius
        bottomRightRadius = radius
        bottomLeftRadius = radius
        measureBounds()
        invalidate()
    }

    /**
     * Change corner radius of the imageview to different size.
     *
     * @param topLeft top left radius size in pixel unit.
     * @param topRight Top right radius size in pixel unit.
     * @param bottomRight Bottom right radius size in pixel unit.
     * @param bottomLeft Bottom left radius size in pixel unit.
     */
    fun setRadius(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        topLeftRadius = topLeft
        topRightRadius = topRight
        bottomRightRadius = bottomRight
        bottomLeftRadius = bottomLeft
        measureBounds()
        invalidate()
    }
}