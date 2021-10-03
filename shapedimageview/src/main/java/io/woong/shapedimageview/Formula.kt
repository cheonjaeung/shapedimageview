package io.woong.shapedimageview

import android.graphics.RectF

/**
 * The shape formula interface to draw custom shape using [FormulableImageView][io.woong.shapedimageview.FormulableImageView].
 *
 * In the imageview, the view creates shape using this formula object in 3 steps.
 *
 * First, the view pass the drawing bounds to [rect] property.
 * Therefore, it is guaranteed that [rect] always contains bounds to be drawn.
 *
 * Second, the view create path object by the formula.
 * In this step, the view draws a path starting from the 3 o'clock and turning around.
 * (The 3 o'clock is 0 degree and the 9 o'clock is 180 degree. The maximum degree is 360.)
 *
 * Finally, the view draw the shape using the path that created in previous step.
 *
 * You can inherit from this interface to create your custom formula.
 * Or you can use a predefined formula.
 *
 * @see io.woong.shapedimageview.FormulableImageView
 * @see io.woong.shapedimageview.formula.EllipseFormula
 * @see io.woong.shapedimageview.formula.SuperEllipseFormula
 */
interface Formula {

    /** The value stores current degree. */
    var degree: Float

    /** The rectangular bounds of shape to be drawn. */
    var rect: RectF

    /**
     * Get x position on current degree.
     */
    fun getX(): Float

    /**
     * Get y position on current degree.
     */
    fun getY(): Float
}
