package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import io.woong.shapedimageview.ShapedImageView

/**
 * The shaped image view that draw image in oval (ellipse) shape.
 */
class OvalImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled) {
            canvas.drawOval(shadowRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawOval(borderRect, borderPaint)
        }

        canvas.drawOval(imageRect, imagePaint)
    }
}
