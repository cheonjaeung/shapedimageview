package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * A image view that display image in circle shape.
 *
 * Scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
 * And also it's width and height size is same.
 */
class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /**
     * This method is invoked after [onMeasure].
     *
     * @param widthMeasureSpec Specs of width.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param heightMeasureSpec Specs of height.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param size Size of view. (width and height is same)
     * @param usableSize Usable size of view. (width and height is same)
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int, usableSize: Int) {}

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        canvas.drawCircle(imageCenterX, imageCenterY, imageRadius, imagePaint)
    }
}
