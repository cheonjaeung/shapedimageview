package io.woong.shapedimageview.util

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * Formula of [superellipse](https://en.wikipedia.org/wiki/Superellipse).
 *
 * **Formula:**
 * |(x / (0.5 * r)) - a|^n + |(y / (0.5 * r)) - b|^n = 1^n
 * - a: center x.
 * - b: center y.
 * - n: degree.
 * - r: radius.
 *
 * **Cases**:
 * - 0 < n < 1: shape is looks like [astroid](https://en.wikipedia.org/wiki/Astroid).
 * - n = 1: diamond shape.
 * - 1 < n < 2: shape is looks like mix of oval and rectangle. but it rotated 45 degree.
 * - n = 2: circle shape.
 * - n > 2: shape is looks like mix of oval and rectangle.
 *
 * @param cx a of formula.
 * @param cy b of formula.
 * @param r radius.
 * @param n n of formula.
 */
internal class SuperellipseFormula(
    private val cx: Float,
    private val cy: Float,
    private val r: Float,
    private val n: Float
) {

    var angle: Int = 0
        set(value) {
            field = value
            if (field > 360) {
                field %= 360
            }
        }

    private val radian: Double
        get() = radian(angle)

    val x: Float
        get() = (abs(cos(radian)).pow(2.0 / n) * r * sign(cos(radian))).toFloat() + cx

    val y: Float
        get() = (abs(sin(radian)).pow(2.0 / n) * r * sign(sin(radian))).toFloat() + cy
}