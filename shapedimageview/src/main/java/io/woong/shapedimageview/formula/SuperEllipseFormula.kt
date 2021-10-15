@file:Suppress("unused")

package io.woong.shapedimageview.formula

import android.graphics.RectF
import io.woong.shapedimageview.Formula
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * A formula class for drawing [super ellipse](https://en.wikipedia.org/wiki/Superellipse).
 *
 * @param curvature The value determines how much curved this shape.
 */
class SuperEllipseFormula(
    private val curvature: Float = 3f
) : Formula {

    override var degree: Float = 0f
        set(value) {
            field = if (value > 360) {
                value % 360
            } else {
                value
            }
        }

    override var rect: RectF = RectF()

    /** The center x position of the super ellipse. */
    private val cx: Float
        get() = rect.centerX()

    /** The center y position of the super ellipse. */
    private val cy: Float
        get() = rect.centerY()

    /** The horizontal radius of the super ellipse. */
    private val rh: Float
        get() = rect.width() / 2

    /** The vertical radius of the super ellipse. */
    private val rv: Float
        get() = rect.height() / 2

    /**
     * Get x position on current degree.
     *
     * - t is radian of [degree]. (0 <= t <= π/2)
     * - n is [curvature].
     * - h is [rh].
     * - sgn(w) = -1 (w < 0) or 0 (w = 0) or 1 (w > 0)
     * - x(t) = |cos t|^(2/n) * sgn(cos t) * h
     */
    override fun getX(): Float {
        val rad = radian(degree)

        val a = abs(cos(rad))
            .pow(2.0 / curvature)

        val b = a * sgn(cos(rad)) * rh

        return b.toFloat() + cx
    }

    /**
     * Get y position on current degree.
     *
     * - t is radian of [degree]. (0 <= t <= π/2)
     * - n is [curvature].
     * - v is [rv].
     * - sgn(w) = -1 (w < 0) or 0 (w = 0) or 1 (w > 0)
     * - y(t) = |sin t|^(2/n) * sgn(sin t) * v
     */
    override fun getY(): Float {
        val rad = radian(degree)

        val a = abs(sin(rad))
            .pow(2.0 / curvature)

        val b = a * sgn(sin(rad)) * rv

        return b.toFloat() + cy
    }

    private fun radian(degree: Float): Double = Math.toRadians(degree.toDouble())

    private fun sgn(value: Double): Int = when {
        value > 0 -> 1
        value < 0 -> -1
        else -> 0
    }
}