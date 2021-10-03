@file:Suppress("MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import io.woong.shapedimageview.util.drawFormula

/**
 * The shaped image view that draw image in specified formula shape.
 *
 * To use this imageview, you should add this imageview in your xml.
 *
 * ```
 *      <io.woong.shapedimageview.FormulableImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 *
 * And you should apply [Formula] to shape this imageview.
 *
 * ```
 *      val iv = findViewById<FormulableImageView>(R.id.formulable)
 *      iv.formula = MyCustomFormula()
 * ```
 *
 * You can create your custom formula or use predefined formula likes
 * [EllipseFormula][io.woong.shapedimageview.formula.EllipseFormula]
 * or [SuperEllipseFormula][io.woong.shapedimageview.formula.SuperEllipseFormula].
 *
 * @see io.woong.shapedimageview.Formula
 */
class FormulableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ShapedImageView(context, attrs, defStyle) {

    /**
     * The [Formula] of this imageview's shape.
     * If it is `null`, this view doesn't draw image.
     *
     * [FormulableImageView] creates path by given formula and draw the path.
     * You can inherit the [Formula] interface to create custom shape formula.
     *
     * @see Formula
     */
    var formula: Formula? = null

    init {
        applyAttributes(attrs, defStyle)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        formula?.let {
            if (shadowEnabled) {
                it.rect = shadowRect
                canvas.drawFormula(it, shadowPaint)
            }

            if (borderEnabled) {
                it.rect = borderRect
                canvas.drawFormula(it, borderPaint)
            }

            it.rect = imageRect
            canvas.drawFormula(it, imagePaint)
        }
    }
}