@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.util.drawCutCornerRect

/**
 * The shaped image view that draw image in corner-cut rectangle shape.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 *
 * In [CutCornerImageView], there are some attributes to configure this imageview.
 * The most important attribute is `cut size`.
 *
 * ```
 *      <!-- To apply all corner cut-size in one line. -->
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:cut_size="32dp" />
 *
 *      <!-- To apply each corner cut-size. -->
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:top_left_cut_size="8dp"
 *          app:top_right_cut_size="12dp"
 *          app:bottom_right_cut_size="16dp"
 *          app:bottom_left_cut_size="24dp" />
 * ```
 */
class CutCornerImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    companion object {
        /** The default value of [CutCornerImageView]'s cut size. */
        const val DEFAULT_CUT_SIZE: Float = -1f
    }

    override var borderEnabled: Boolean = DEFAULT_BORDER_ENABLED
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    override var shadowEnabled: Boolean = DEFAULT_SHADOW_ENABLED
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    /** The value for using when cut size is [DEFAULT_CUT_SIZE]. */
    private val defaultCutSize: Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        16f,
        this.resources.displayMetrics
    )

    /** The cut size of the imageview's top-left in pixel unit. */
    var topLeftCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    /** The cut size of the imageview's top-right in pixel unit. */
    var topRightCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    /** The cut size of the imageview's bottom-right in pixel unit. */
    var bottomRightCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    /** The cut size of the imageview's bottom-left in pixel unit. */
    var bottomLeftCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            setBorderAndShadowCutSizes()
            invalidate()
        }

    /**
     * Set the all corner's cut size of the imageview in pixel unit.
     *
     * @param size The pixel size of the imageview's corners.
     */
    fun setCutSizes(size: Float) {
        this.topLeftCutSize = size
        this.topRightCutSize = size
        this.bottomRightCutSize = size
        this.bottomLeftCutSize = size
        setBorderAndShadowCutSizes()
    }

    /**
     * Set the all corner's cut size of the imageview in pixel unit.
     *
     * @param topLeft The top-left corner cut size in pixel.
     * @param topRight The top-right corner cut size in pixel.
     * @param bottomRight The bottom-right corner cut size in pixel.
     * @param bottomLeft The bottom-left corner cut size in pixel.
     */
    fun setCutSizes(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        this.topLeftCutSize = topLeft
        this.topRightCutSize = topRight
        this.bottomRightCutSize = bottomRight
        this.bottomLeftCutSize = bottomLeft
        setBorderAndShadowCutSizes()
    }

    /**
     * Set the all corner's cut size of the imageview in pixel unit.
     *
     * @param sizes The array of the 4 corner's cut size.
     * The order is left, top, right and bottom.
     */
    fun setCutSizes(sizes: FloatArray) {
        if (sizes.size != 4) {
            throw IllegalArgumentException("setCutSizes' parameter should be an array or list that has 4 items.")
        }
        this.topLeftCutSize = sizes[0]
        this.topRightCutSize = sizes[1]
        this.bottomRightCutSize = sizes[2]
        this.bottomLeftCutSize = sizes[3]
        setBorderAndShadowCutSizes()
    }

    /** The cut size of the border's top-left in pixel unit. */
    private var borderTopLeftCutSize: Float = topLeftCutSize + borderSize

    /** The cut size of the border's top-right in pixel unit. */
    private var borderTopRightCutSize: Float = topRightCutSize + borderSize

    /** The cut size of the border's bottom-right in pixel unit. */
    private var borderBottomRightCutSize: Float = bottomRightCutSize + borderSize

    /** The cut size of the border's bottom-left in pixel unit. */
    private var borderBottomLeftCutSize: Float = bottomLeftCutSize + borderSize

    /** The cut size of the shadow's top-left in pixel unit. */
    private var shadowTopLeftCutSize: Float = if (borderEnabled) {
        borderTopLeftCutSize
    } else {
        topLeftCutSize
    }

    /** The cut size of the shadow's top-right in pixel unit. */
    private var shadowTopRightCutSize: Float = if (borderEnabled) {
        borderTopRightCutSize
    } else {
        topRightCutSize
    }

    /** The cut size of the shadow's bottom-right in pixel unit. */
    private var shadowBottomRightCutSize: Float = if (borderEnabled) {
        borderBottomRightCutSize
    } else {
        bottomRightCutSize
    }

    /** The cut size of the shadow's bottom-left in pixel unit. */
    private var shadowBottomLeftCutSize: Float = if (borderEnabled) {
        borderBottomLeftCutSize
    } else {
        bottomLeftCutSize
    }

    /**
     * Change border and shadow's cut sizes.
     */
    private fun setBorderAndShadowCutSizes() {
        if (borderEnabled) {
            val topLeft = topLeftCutSize + borderSize
            borderTopLeftCutSize = topLeft
            shadowTopLeftCutSize = topLeft

            val topRight = topRightCutSize + borderSize
            borderTopRightCutSize = topRight
            shadowTopRightCutSize = topRight

            val bottomRight = bottomRightCutSize + borderSize
            borderBottomRightCutSize = bottomRight
            shadowBottomRightCutSize = bottomRight

            val bottomLeft = bottomLeftCutSize + borderSize
            borderBottomLeftCutSize = bottomLeft
            shadowBottomLeftCutSize = bottomLeft
        } else {
            shadowTopLeftCutSize = topLeftCutSize
            shadowTopRightCutSize = topRightCutSize
            shadowBottomRightCutSize = bottomRightCutSize
            shadowBottomLeftCutSize = bottomLeftCutSize
        }
    }

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CutCornerImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.CutCornerImageView_cut_size)) {
                val c = a.getDimension(R.styleable.CutCornerImageView_cut_size, defaultCutSize)
                setCutSizes(c)
            }

            if (a.hasValue(R.styleable.CutCornerImageView_top_left_cut_size)) {
                topLeftCutSize = a.getDimension(R.styleable.CutCornerImageView_top_left_cut_size, defaultCutSize)
            }

            if (a.hasValue(R.styleable.CutCornerImageView_top_right_cut_size)) {
                topRightCutSize = a.getDimension(R.styleable.CutCornerImageView_top_left_cut_size, defaultCutSize)
            }

            if (a.hasValue(R.styleable.CutCornerImageView_bottom_right_cut_size)) {
                bottomRightCutSize = a.getDimension(R.styleable.CutCornerImageView_bottom_right_cut_size, defaultCutSize)
            }

            if (a.hasValue(R.styleable.CutCornerImageView_bottom_left_cut_size)) {
                bottomLeftCutSize = a.getDimension(R.styleable.CutCornerImageView_bottom_left_cut_size, defaultCutSize)
            }
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled && shadowSize > 0f) {
            canvas.drawCutCornerRect(
                shadowRect,
                shadowTopLeftCutSize,
                shadowTopRightCutSize,
                shadowBottomRightCutSize,
                shadowBottomLeftCutSize,
                shadowPaint
            )
        }

        if (borderEnabled && borderSize > 0f) {
            canvas.drawCutCornerRect(
                borderRect,
                borderTopLeftCutSize,
                borderTopRightCutSize,
                borderBottomRightCutSize,
                borderBottomLeftCutSize,
                borderPaint
            )
        }

        canvas.drawCutCornerRect(
            imageRect,
            topLeftCutSize,
            topRightCutSize,
            bottomRightCutSize,
            bottomLeftCutSize,
            imagePaint
        )
    }
}