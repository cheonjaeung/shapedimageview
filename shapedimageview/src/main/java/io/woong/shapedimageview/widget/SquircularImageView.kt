package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import io.woong.shapedimageview.R
import io.woong.shapedimageview.ShapedImageView
import io.woong.shapedimageview.util.SuperellipseFormula
import java.lang.IllegalArgumentException

/**
 * An image view that displaying image in [squircle](https://en.wikipedia.org/wiki/Squircle) shape
 * (Squircle is a kind of [superellipse](https://en.wikipedia.org/wiki/Superellipse)).
 *
 * Scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
 * And also it's width and height size is same.
 */
class SquircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /** X position of image center. */
    private var imageCenterX: Float = 0f
    /** Y position of image center. */
    private var imageCenterY: Float = 0f
    /** Radius size of image. */
    private var imageRadius: Float = 0f
    /** Curvature of squircle shape. */
    private var imageCurvature: Float = 3f

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
            R.styleable.SquircularImageView,
            defStyle,
            0
        )

        try {
            val curvatureAttr = attrs.getInteger(R.styleable.SquircularImageView_shaped_imageview_curvature, 50)
            imageCurvature = when {
                curvatureAttr < 0 || curvatureAttr > 100 -> throw IllegalArgumentException("Curvature must be in 0 ~ 100.")
                curvatureAttr >= 0 -> {
                    (curvatureAttr.toFloat() / 25) + 1
                }
                else -> 3f
            }
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
     */
    override fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int) {
        imageRadius = imageSize / 2f
        imageCenterX = (paddingLeft + (size - paddingRight)) / 2f
        imageCenterY = (paddingTop + (size - paddingBottom)) / 2f
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    override fun postOnDraw(canvas: Canvas) {
        if (shadowEnabled) {
            canvas.drawSquircle(imageCenterX, imageCenterY, imageRadius, shadowPaint)
        }

        canvas.drawSquircle(imageCenterX, imageCenterY, imageRadius, imagePaint)
    }

    /**
     * Draw the squircle using the specified paint.
     * The squircle will be filled or framed based on the Style in the paint.
     */
    private fun Canvas.drawSquircle(cx: Float, cy: Float, radius: Float, paint: Paint) {
        val formula = SuperellipseFormula(cx, cy, radius, imageCurvature)

        val path = Path()
        path.apply {
            moveTo(formula.x, formula.y)
            for (angle in 0 until 360) {
                formula.angle = angle + 1
                val x = formula.x
                val y = formula.y
                if (x.isNaN() || y.isNaN()) break
                lineTo(x, y)
            }
        }
        path.close()

        this.drawPath(path, paint)
    }
}
