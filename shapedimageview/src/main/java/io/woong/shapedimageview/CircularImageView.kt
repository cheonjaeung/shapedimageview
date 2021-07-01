package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import java.lang.IllegalArgumentException
import kotlin.math.min

class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    private var imageCenterX: Float = 0f
    private var imageCenterY: Float = 0f
    private var imageRadius: Float = 0f

    init {
        scaleType = ScaleType.CENTER_CROP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val s = min(w, h)
        setMeasuredDimension(s, s)

        val uw = w - paddingLeft - paddingRight
        val uh = h - paddingTop - paddingBottom
        val us = min(uw, uh)
        imageRadius = us / 2f

        imageCenterX = (paddingLeft + uw) / 2f
        imageCenterY = (paddingTop + uh) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        updateImage()
        updateShader((imageRadius * 2).toInt(), (imageRadius * 2).toInt())

        canvas.drawCircle(imageCenterX, imageCenterY, imageRadius, imagePaint)
    }

    /**
     * Set scale type of this image view.
     *
     * Only can accept [CENTER_CROP][android.widget.ImageView.ScaleType.CENTER_CROP],
     * Other scale types will be denied.
     *
     * @param scaleType A scale type mode.
     *
     * @throws IllegalArgumentException When given scale type is not supported.
     */
    override fun setScaleType(scaleType: ScaleType) {
        when (scaleType) {
            ScaleType.CENTER_CROP -> {
                super.setScaleType(scaleType)
            }
            else -> throw IllegalArgumentException("ScaleType $scaleType not supported.")
        }
    }
}
