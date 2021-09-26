package io.woong.shapedimageview.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import io.woong.shapedimageview.Formula

/**
 * Draw the specified cut-corner rectangle using the specified paint.
 *
 * @param rect The rectangular bounds of the cut-corner-rect to be drawn.
 * @param ctl The cut size of top-left of the rect.
 * @param ctr The cut size of top-right of the rect.
 * @param cbr The cut size of bottom-right of the rect.
 * @param cbl The cut size of bottom-left of the rect.
 * @param paint The paint used to draw the cur-corner rectangle.
 */
internal fun Canvas.drawCutCornerRect(rect: RectF, ctl: Float, ctr: Float, cbr: Float, cbl: Float, paint: Paint) {
    val path = Path()

    path.apply {
        moveTo(rect.left, rect.top + ctl)
        lineTo(rect.left + ctl, rect.top)
        lineTo(rect.right - ctr, rect.top)
        lineTo(rect.right, rect.top + ctr)
        lineTo(rect.right, rect.bottom - cbr)
        lineTo(rect.right - cbr, rect.bottom)
        lineTo(rect.left + cbl, rect.bottom)
        lineTo(rect.left, rect.bottom - cbl)
        lineTo(rect.left, rect.top + ctl)
    }.close()

    this.drawPath(path, paint)
}

/**
 * Draw the specified round-rect using the specified paint.
 *
 * @param rect The rectangular bounds of the round-rect to be drawn.
 * @param rtl The radius of top-left of the round-rect.
 * @param rtr The radius of top-right of the round-rect.
 * @param rbr The radius of bottom-right of the round-rect.
 * @param rbl The radius of bottom-left of the round-rect.
 * @param paint The paint used to draw the round-rect.
 */
internal fun Canvas.drawRoundRect(rect: RectF, rtl: Float, rtr: Float, rbr: Float, rbl: Float, paint: Paint) {
    val path = Path()

    path.apply {
        moveTo(rect.left, rect.top + rtl)
        quadTo(rect.left, rect.top, rect.left + rtl, rect.top)
        lineTo(rect.right - rtr, rect.top)
        quadTo(rect.right, rect.top, rect.right, rect.top + rtr)
        lineTo(rect.right, rect.bottom - rbr)
        quadTo(rect.right, rect.bottom, rect.right - rbr, rect.bottom)
        lineTo(rect.left + rbl, rect.bottom)
        quadTo(rect.left, rect.bottom, rect.left, rect.bottom - rbl)
        lineTo(rect.left, rect.top + rtl)
    }.close()

    this.drawPath(path, paint)
}

/**
 * Draw a specified shape of given formula using the specified paint.
 *
 * @param formula The shape formula used to draw the shape.
 * @param paint The paint used to draw the shape.
 */
internal fun Canvas.drawFormula(formula: Formula, paint: Paint) {
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
