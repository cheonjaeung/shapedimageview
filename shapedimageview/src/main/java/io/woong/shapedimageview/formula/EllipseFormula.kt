package io.woong.shapedimageview.formula

import android.graphics.RectF
import io.woong.shapedimageview.Formula
import kotlin.math.cos
import kotlin.math.sin

/**
 * A formula class for drawing [ellipse](https://en.wikipedia.org/wiki/Ellipse).
 */
class EllipseFormula : Formula {

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

    override fun getX(): Float = rh * sin(radian(degree)) + cx

    override fun getY(): Float = rv * cos(radian(degree)) + cy

    private fun radian(degree: Float): Float = Math.toRadians(degree.toDouble()).toFloat()
}
