package io.woong.shapedimageview.util

/**
 * Convert angle degree to radian.
 */
internal fun radian(degree: Int): Double = Math.toRadians(degree.toDouble())

/**
 * Return 1 when value is positive, -1 when negative and 0 otherwise.
 */
internal fun sgn(value: Double): Int {
    return when {
        value > 0.0 -> 1
        value < 0.0 -> -1
        else -> 0
    }
}