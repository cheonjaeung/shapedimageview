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

    override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        super.applyAttributes(attrs, defStyle)

        val a = context.obtainStyledAttributes(attrs, R.styleable.FormulableImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.FormulableImageView_shape_formula)) {
                val fstr = a.getString(R.styleable.FormulableImageView_shape_formula)
                val f = inflateFormulaFromString(fstr)
                f?.let {
                    this.formula = it
                } ?: run {
                    throw IllegalArgumentException("$fstr is not a kind of Formula class.")
                }
            }
        } finally {
            a.recycle()
        }
    }

    /**
     * Parse given formula class string and return matched formula class.
     * If given path is illegal or `null`, this method returns `null`.
     *
     * @param formulaString The path of formula.
     * @return The matched [Formula] class or `null`.
     * @throws RuntimeException When something wrong while parsing formula string.
     */
    private fun inflateFormulaFromString(formulaString: String?): Formula? {
        if (formulaString.isNullOrBlank()) {
            return null
        } else {
            try {
                val formulaPath = when {
                    // Relative path
                    formulaString.startsWith(".") -> {
                        val packageName = context.packageName
                        packageName + formulaString
                    }
                    // Absolute path
                    formulaString.indexOf('.') > 0 -> {
                        formulaString
                    }
                    // Predefined formula name
                    else -> {
                        val libPackageName = ShapedImageView::class.java.`package`?.name
                        "$libPackageName.formula.$formulaString"
                    }
                }

                // Inflate Formula class using reflection.
                val clazz = Class.forName(formulaPath, false, context.classLoader)
                val f = clazz.newInstance()
                return if (f is Formula) {
                    f
                } else {
                    null
                }
            } catch (e: Exception) {
                throw RuntimeException("Cannot inflate Formula class $formulaString", e)
            }
        }
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