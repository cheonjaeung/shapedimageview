package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import io.woong.shapedimageview.R
import io.woong.shapedimageview.ShapedImageView

/**
 * An image view that displaying image in rounded square shape.
 *
 * Scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
 * And also it's width and height size is same.
 */
class RoundedSquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /** Rect object of image. */
    private val imageRect: RectF = RectF()
    /** Radius size of image. */
    var imageRadius: Float = 0f
        set(value) {
            field = value
            remeasure()
            invalidate()
        }

    /** Rect object of border. */
    private val borderRect: RectF = RectF()
    /** Radius size of border. */
    private var borderRadius: Float = 0f

    init {
        applyCommonAttributes(attrs, defStyle)
        applyAttributes(attrs, defStyle)
    }

    /**
     * Apply custom attributes.
     */
    private fun applyAttributes(attributes: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attributes,
            R.styleable.RoundedSquareImageView,
            defStyle,
            0
        )

        try {
            imageRadius = attrs.getDimension(R.styleable.RoundedSquareImageView_shaped_imageview_radius, 0f)
        } finally {
            attrs.recycle()
        }
    }

    /**
     * Update sizes and values like image size, shadow and border.
     */
    override fun remeasure() {
        val shadowAdjustment = if (shadowEnabled && shadowAdjustEnabled) shadowSize * 2 else 0f
        val borderAdjustment = if (borderEnabled) borderSize else 0f

        val left = paddingLeft.toFloat() + shadowAdjustment + borderAdjustment
        val top = paddingTop.toFloat() + shadowAdjustment + borderAdjustment
        val right = (paddingLeft + imageSize).toFloat() - shadowAdjustment - borderAdjustment
        val bottom = (paddingTop + imageSize).toFloat() - shadowAdjustment - borderAdjustment

        imageRect.set(left, top, right, bottom)

        borderRect.set(
            left - borderAdjustment,
            top - borderAdjustment,
            right + borderAdjustment,
            bottom + borderAdjustment
        )

        borderRadius = if (borderEnabled) imageRadius + borderSize else 0f
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        if (shadowEnabled) {
            canvas.drawRoundRect(borderRect, borderRadius, borderRadius, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawRoundRect(borderRect, borderRadius, borderRadius, borderPaint)
        }

        canvas.drawRoundRect(imageRect, imageRadius, imageRadius, imagePaint)
    }
}
