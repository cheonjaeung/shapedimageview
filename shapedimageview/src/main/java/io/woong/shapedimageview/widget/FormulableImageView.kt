@file:Suppress("MemberVisibilityCanBePrivate")

package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import io.woong.shapedimageview.ShapedImageView
import io.woong.shapedimageview.Formula

/**
 * The shaped image view that draw image in specified formula shape.
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

    /**
     * Draw a specified shape of given formula using the specified paint.
     *
     * @param formula The shape formula used to draw the shape.
     * @param paint The paint used to draw the shape.
     */
    private fun Canvas.drawFormula(formula: Formula, paint: Paint) {
        val path = Path()

        path.apply {
            formula.degree = 0f
            moveTo(formula.getX(), formula.getY())

            for (d in 1..360) {
                formula.degree = d.toFloat()
                lineTo(formula.getX(), formula.getY())
            }
        }.close()

        this.drawPath(path, paint)
    }
}