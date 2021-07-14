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
     * This method is invoked after [onMeasure].
     *
     * @param widthMeasureSpec Specs of width.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param heightMeasureSpec Specs of height.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param size Size of view. (width and height is same)
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int) {
        val shadowAdjustment = if (shadowEnabled && shadowAdjustEnabled) shadowSize * 2 else 0f

        val left = (paddingLeft + shadowAdjustment).toInt()
        val top = (paddingTop + shadowAdjustment).toInt()
        val right = (paddingLeft + imageSize - shadowAdjustment).toInt()
        val bottom = (paddingTop + imageSize - shadowAdjustment).toInt()

        imageRect.set(left, top, right, bottom)

        borderRect.set(
            (left - borderSize).toInt(),
            (top - borderSize).toInt(),
            (right + borderSize).toInt(),
            (bottom + borderSize).toInt()
        )
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        if (shadowEnabled) {
            canvas.drawRect(imageRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawRect(borderRect, borderPaint)
        }

        canvas.drawRect(imageRect, imagePaint)
    }
}
