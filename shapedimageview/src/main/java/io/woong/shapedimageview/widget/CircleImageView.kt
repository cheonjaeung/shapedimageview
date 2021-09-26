package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import io.woong.shapedimageview.SquareShapedImageView

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SquareShapedImageView(context, attrs, defStyle) {

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled) {
            val cx = shadowRect.centerX()
            val cy = shadowRect.centerY()
            val r = shadowRect.width() / 2
            canvas.drawCircle(cx, cy, r, shadowPaint)
        }

        if (borderEnabled) {
            val cx = shadowRect.centerX()
            val cy = shadowRect.centerY()
            val r = shadowRect.width() / 2
            canvas.drawCircle(cx, cy, r, borderPaint)
        }

        val cx = shadowRect.centerX()
        val cy = shadowRect.centerY()
        val r = shadowRect.width() / 2
        canvas.drawCircle(cx, cy, r, imagePaint)
    }
}
