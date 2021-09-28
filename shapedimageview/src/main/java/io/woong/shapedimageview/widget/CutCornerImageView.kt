@file:Suppress("MemberVisibilityCanBePrivate")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import io.woong.shapedimageview.R
import io.woong.shapedimageview.ShapedImageView
import io.woong.shapedimageview.util.drawCutCornerRect

/**
 * The shaped image view that draw image in corner-cut rectangle shape.
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
            invalidate()
        }

    /** The cut size of the imageview's top-right in pixel unit. */
    var topRightCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The cut size of the imageview's bottom-right in pixel unit. */
    var bottomRightCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The cut size of the imageview's bottom-left in pixel unit. */
    var bottomLeftCutSize: Float = defaultCutSize
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The cut size of the border's top-left in pixel unit. */
    private var borderTopLeftCutSize: Float = defaultCutSize

    /** The cut size of the border's top-right in pixel unit. */
    private var borderTopRightCutSize: Float = defaultCutSize

    /** The cut size of the border's bottom-right in pixel unit. */
    private var borderBottomRightCutSize: Float = defaultCutSize

    /** The cut size of the border's bottom-left in pixel unit. */
    private var borderBottomLeftCutSize: Float = defaultCutSize

    /** The cut size of the shadow's top-left in pixel unit. */
    private var shadowTopLeftCutSize: Float = defaultCutSize

    /** The cut size of the shadow's top-right in pixel unit. */
    private var shadowTopRightCutSize: Float = defaultCutSize

    /** The cut size of the shadow's bottom-right in pixel unit. */
    private var shadowBottomRightCutSize: Float = defaultCutSize

    /** The cut size of the shadow's bottom-left in pixel unit. */
    private var shadowBottomLeftCutSize: Float = defaultCutSize

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CutCornerImageView, defStyle, 0)

        try {
            val c = a.getDimension(R.styleable.CutCornerImageView_cut_size, DEFAULT_CUT_SIZE)
            if (c != DEFAULT_CUT_SIZE) {
                this.topLeftCutSize = c
                this.topRightCutSize = c
                this.bottomRightCutSize = c
                this.bottomLeftCutSize = c
            }

            val bs = if (borderEnabled) {
                this.borderSize / 2
            } else {
                0f
            }
            this.borderTopLeftCutSize = this.topLeftCutSize + bs
            this.borderTopRightCutSize = this.topRightCutSize + bs
            this.borderBottomRightCutSize = this.bottomRightCutSize + bs
            this.borderBottomLeftCutSize = this.bottomLeftCutSize + bs

            if (shadowEnabled) {
                this.shadowTopLeftCutSize = this.borderTopLeftCutSize
                this.shadowTopRightCutSize = this.borderTopRightCutSize
                this.shadowBottomRightCutSize = this.borderBottomRightCutSize
                this.shadowBottomLeftCutSize = this.borderBottomLeftCutSize
            } else {
                this.shadowTopLeftCutSize = this.topLeftCutSize
                this.shadowTopRightCutSize = this.topRightCutSize
                this.shadowBottomRightCutSize = this.bottomRightCutSize
                this.shadowBottomLeftCutSize = this.bottomLeftCutSize
            }

            val ctl = a.getDimension(R.styleable.CutCornerImageView_top_left_cut_size, DEFAULT_CUT_SIZE)
            if (ctl != DEFAULT_CUT_SIZE) {
                this.topLeftCutSize = ctl
                this.borderTopLeftCutSize = if (borderEnabled) {
                    ctl + (this.borderSize / 2)
                } else {
                    ctl
                }
                this.shadowTopLeftCutSize = if (shadowEnabled) {
                    this.borderTopLeftCutSize
                } else {
                    ctl
                }
            }

            val ctr = a.getDimension(R.styleable.CutCornerImageView_top_right_cut_size, DEFAULT_CUT_SIZE)
            if (ctr != DEFAULT_CUT_SIZE) {
                this.topRightCutSize = ctr
                this.borderTopRightCutSize = if (borderEnabled) {
                    ctr + (this.borderSize / 2)
                } else {
                    ctr
                }
                this.shadowTopRightCutSize = if (shadowEnabled) {
                    this.borderTopRightCutSize
                } else {
                    ctr
                }
            }

            val cbr = a.getDimension(R.styleable.CutCornerImageView_bottom_right_cut_size, DEFAULT_CUT_SIZE)
            if (cbr != DEFAULT_CUT_SIZE) {
                this.bottomRightCutSize = cbr
                this.borderBottomRightCutSize = if (borderEnabled) {
                    cbr + (this.borderSize / 2)
                } else {
                    cbr
                }
                this.shadowBottomRightCutSize = if (shadowEnabled) {
                    this.borderBottomRightCutSize
                } else {
                    cbr
                }
            }

            val cbl = a.getDimension(R.styleable.CutCornerImageView_bottom_left_cut_size, DEFAULT_CUT_SIZE)
            if (cbl != DEFAULT_CUT_SIZE) {
                this.bottomLeftCutSize = cbl
                this.borderBottomLeftCutSize = if (borderEnabled) {
                    cbl + (this.borderSize / 2)
                } else {
                    cbl
                }
                this.shadowBottomLeftCutSize = if (shadowEnabled) {
                    this.borderBottomLeftCutSize
                } else {
                    cbl
                }
            }
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled) {
            canvas.drawCutCornerRect(
                shadowRect,
                shadowTopLeftCutSize,
                shadowTopRightCutSize,
                shadowBottomRightCutSize,
                shadowBottomLeftCutSize,
                shadowPaint
            )
        }

        if (borderEnabled) {
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