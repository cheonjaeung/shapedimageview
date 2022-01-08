package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import io.woong.shapedimageview.util.drawCutCornerRect

/**
 * The shaped image view that draw image in corner-cut rectangle shape.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 *
 * In [CutCornerImageView], there are some attributes to configure this imageview.
 * The most important attribute is `radius`.
 *
 * ```
 *      <!-- To apply all corner radius in one line. -->
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:radius="32dp" />
 *
 *      <!-- To apply each corner radius. -->
 *      <io.woong.shapedimageview.CutCornerImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:top_left_radius="8dp"
 *          app:top_right_radius="12dp"
 *          app:bottom_right_radius="16dp"
 *          app:bottom_left_radius="24dp" />
 * ```
 */
open class CutCornerImageView : CorneredImageView {
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        applyAttributes(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int): super(context, attrs, defStyle) {
        applyAttributes(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (shadowEnabled && shadowSize > 0f) {
            canvas.drawCutCornerRect(
                shadowRect,
                shadowTopLeftRadius,
                shadowTopRightRadius,
                shadowBottomRightRadius,
                shadowBottomLeftRadius,
                shadowPaint
            )
        }

        if (borderEnabled && borderSize > 0f) {
            canvas.drawCutCornerRect(
                borderRect,
                borderTopLeftRadius,
                borderTopRightRadius,
                borderBottomRightRadius,
                borderBottomLeftRadius,
                borderPaint
            )
        }

        canvas.drawCutCornerRect(
            imageRect,
            topLeftRadius,
            topRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            imagePaint
        )
    }
}