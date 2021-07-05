package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import io.woong.shapedimageview.ShapedImageView

/**
 * An image view that displaying image in [squircle](https://en.wikipedia.org/wiki/Squircle) shape.
 *
 * Scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
 * And also it's width and height size is same.
 */
class SquircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /** X position of image left end. */
    private var imageLeft: Float = 0f
    /** Y position of image top end. */
    private var imageTop: Float = 0f
    /** Radius size of image. */
    private var imageRadius: Float = 0f

    /**
     * This method is invoked after [onMeasure].
     *
     * @param widthMeasureSpec Specs of width.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param heightMeasureSpec Specs of height.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int) {
        imageLeft = paddingLeft.toFloat()
        imageTop = paddingTop.toFloat()
        imageRadius = imageSize / 2f
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        canvas.drawSquircle(imageLeft, imageTop, imageSize.toFloat(), imageRadius, imagePaint)
    }

    /**
     * Draw the squircle using the specified paint.
     * The squircle will be filled or framed based on the Style in the paint.
     */
    private fun Canvas.drawSquircle(left: Float, top: Float, size: Float, radius: Float, paint: Paint) {
        val startX = left
        val startY = top + radius

        val path = Path().apply {
            moveTo(startX, startY)

            close()
        }

        this.drawPath(path, paint)
    }
}
