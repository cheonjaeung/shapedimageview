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
    private var imageRadius: Float = 0f

    init {
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
     * This method is invoked after [onMeasure].
     *
     * @param widthMeasureSpec Specs of width.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param heightMeasureSpec Specs of height.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param size Size of view. (width and height is same)
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int) {
        imageRect.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (paddingLeft + imageSize).toFloat(),
            (paddingTop + imageSize).toFloat()
        )
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        canvas.drawRoundRect(imageRect, imageRadius, imageRadius, imagePaint)
    }
}
