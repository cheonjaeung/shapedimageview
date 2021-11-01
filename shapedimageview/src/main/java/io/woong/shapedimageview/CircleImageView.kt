package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * The shaped imageview that draws image in circle shape.
 *
 * The difference between [OvalImageView] and this view is that
 * it has same width and height size.
 *
 * If width or height size is smaller than another,
 * this view's size will be set to the smaller one.
 *
 * Aspect ratio setting is not working for this view
 * because this view always has same width and height.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.CircleImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 */
class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : OvalImageView(context, attrs, defStyle) {

    init {
        applyAttributes(attrs, defStyle)
        this.isRegularShape = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled && shadowSize > 0f) {
            canvas.drawCircle(
                shadowRect.centerX(),
                shadowRect.centerY(),
                shadowRect.width() / 2,
                shadowPaint
            )
        }

        if (borderEnabled && borderSize > 0f) {
            canvas.drawCircle(
                borderRect.centerX(),
                borderRect.centerY(),
                borderRect.width() / 2,
                borderPaint
            )
        }

        canvas.drawCircle(
            imageRect.centerX(),
            imageRect.centerY(),
            imageRect.width() / 2,
            imagePaint
        )
    }
}
