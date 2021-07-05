package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

class SquircularImageView @JvmOverloads constructor(
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
     * @param usableSize Usable size of view. (width and height is same)
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int, usableSize: Int) {}

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        TODO("Not yet implemented")
    }
}
