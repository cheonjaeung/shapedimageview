package io.woong.shapedimageview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

/**
 * The shaped image view that draw image in oval (ellipse) shape.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.OvalImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 */
open class OvalImageView : ShapedImageView {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        applyAttributes(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        applyAttributes(attrs, defStyle)
    }

    final override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled && shadowSize > 0f) {
            canvas.drawOval(shadowRect, shadowPaint)
        }

        if (borderEnabled && borderSize > 0f) {
            canvas.drawOval(borderRect, borderPaint)
        }

        canvas.drawOval(imageRect, imagePaint)
    }
}
