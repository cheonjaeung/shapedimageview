package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import io.woong.shapedimageview.ShapedImageView

/**
 * An image view that displaying image in square shape.
 *
 * Scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
 * And also it's width and height size is same.
 */
class SquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /** Rect object of image. */
    private val imageRect: Rect = Rect()

    /** Rect object of border. */
    private val borderRect: Rect = Rect()

    init {
        applyCommonAttributes(attrs, defStyle)
    }

    /**
     * Update sizes and values like image size, shadow and border.
     */
    override fun remeasure() {
        val shadowAdjustment = if (shadowEnabled && shadowAdjustEnabled) shadowSize * 2 else 0f
        val borderAdjustment = if (borderEnabled) borderSize else 0f

        val left = (paddingLeft + shadowAdjustment + borderAdjustment).toInt()
        val top = (paddingTop + shadowAdjustment + borderAdjustment).toInt()
        val right = (paddingLeft + imageSize - shadowAdjustment - borderAdjustment).toInt()
        val bottom = (paddingTop + imageSize - shadowAdjustment - borderAdjustment).toInt()

        imageRect.set(left, top, right, bottom)

        borderRect.set(
            (left - borderAdjustment).toInt(),
            (top - borderAdjustment).toInt(),
            (right + borderAdjustment).toInt(),
            (bottom + borderAdjustment).toInt()
        )
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        if (shadowEnabled) {
            canvas.drawRect(borderRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawRect(borderRect, borderPaint)
        }

        canvas.drawRect(imageRect, imagePaint)
    }
}
