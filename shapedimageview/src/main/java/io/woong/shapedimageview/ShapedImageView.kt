@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import io.woong.shapedimageview.util.Bounds
import io.woong.shapedimageview.util.createCenterCropMatrix
import io.woong.shapedimageview.util.createFitXYMatrix
import io.woong.shapedimageview.util.toBitmap

/**
 * The parent class of all shaped image view.
 *
 * This class cannot be used alone cause it is abstract class.
 * To use shaped imageview, use classes that inherit from this class.
 *
 * To inherit from this class, you should call [applyAttributes] on init
 * for obtaining common attributes from context.
 *
 * ```
 *      class CustomImageView @JvmOverloads constructor(
 *      context: Context,
 *      attrs: AttributeSet? = null,
 *      defStyle: Int = 0
 *      ) : ShapedImageView(context, attrs, defStyle) {
 *
 *          init {
 *              applyAttributes(attrs, defStyle)
 *          }
 *
 *      ...
 *      }
 * ```
 *
 * If your imageview has custom attributes, you can override [applyAttributes] method.
 * In this case, you should call super method.
 *
 * ```
 *      init {
 *          applyAttributes(attrs, defStyle)
 *      }
 *
 *      override fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
 *          super.applyAttributes(attrs, defStyle)
 *          val a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, defStyle, 0)
 *          try {
 *              // Obtaining custom attributes.
 *          } finally {
 *              a.recycle()
 *          }
 *      }
 * ```
 *
 * To draw shape, override [onDraw] method.
 * When override [onDraw], you should call super method for updating bitmap shader to image paint
 * and adding shadow layer to shadow paint.
 *
 * It is recommended that draw shadow first, border next and image at last.
 *
 * ```
 *      override fun onDraw(canvas: Canvas) {
 *          super.onDraw(canvas)
 *
 *          if (shadowEnabled) {
 *              canvas.drawOval(shadowRect, shadowPaint)
 *          }
 *
 *          if (borderEnabled) {
 *              canvas.drawOval(borderRect, borderPaint)
 *          }
 *
 *          canvas.drawOval(imageRect, imagePaint)
 *      }
 * ```
 *
 * @see io.woong.shapedimageview.CutCornerImageView
 * @see io.woong.shapedimageview.FormulableImageView
 * @see io.woong.shapedimageview.OvalImageView
 * @see io.woong.shapedimageview.RoundImageView
 */
abstract class ShapedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    companion object {
        /** The default value of [ShapedImageView]'s border size. */
        const val DEFAULT_BORDER_SIZE: Float = 0f

        /** The default value of [ShapedImageView]'s border color. */
        @JvmStatic @ColorInt
        val DEFAULT_BORDER_COLOR: Int = Color.parseColor("#444444")

        /** The default value of [ShapedImageView]'s border enabled status. */
        const val DEFAULT_BORDER_ENABLED: Boolean = true

        /** The default value of [ShapedImageView]'s shadow size. */
        const val DEFAULT_SHADOW_SIZE: Float = 0f

        /** The default value of [ShapedImageView]'s shadow color. */
        @JvmStatic @ColorInt
        val DEFAULT_SHADOW_COLOR: Int = Color.parseColor("#888888")

        /** The default value of [ShapedImageView]'s shadow enabled status. */
        const val DEFAULT_SHADOW_ENABLED: Boolean = true
    }

    /**
     * The maximum drawable width pixel size of this imageview.
     * This value is calculated according to the width of view, paddings, border and shadow.
     * And also, it equals to width of image to drawn.
     */
    protected var usableWidth: Float = 0f

    /**
     * The maximum drawable height pixel size of this imageview.
     * This value is calculated according to the width of view, paddings, border and shadow.
     * And also, it equals to height of image to drawn.
     */
    protected var usableHeight: Float = 0f

    /** The bitmap image to draw in this imageview. */
    protected var image: Bitmap? = null

    /** The drawable image to check this imageview needs to update [image] property. */
    protected var imageCache: Drawable? = null

    /** Rectangle bounds of image to be drawn. */
    protected val imageRect: RectF = RectF()

    /** The paint object to draw [image]. */
    protected val imagePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** The border size of this imageview in pixel unit. */
    var borderSize: Float = DEFAULT_BORDER_SIZE
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The border color of this imageview. */
    @ColorInt
    var borderColor: Int = DEFAULT_BORDER_COLOR
        set(value) {
            field = value
            this.borderPaint.color = field
            measureBounds()
            invalidate()
        }

    /** The enabled status of border. */
    open var borderEnabled: Boolean = DEFAULT_BORDER_ENABLED
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** Rectangle bounds of border to be drawn. */
    protected val borderRect: RectF = RectF()

    /** The paint object to draw border. */
    protected val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** The shadow size of this imageview in pixel unit. */
    var shadowSize: Float = DEFAULT_SHADOW_SIZE
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The shadow color of this imageview. */
    @ColorInt
    var shadowColor: Int = DEFAULT_SHADOW_COLOR
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** The enabled status of shadow. */
    open var shadowEnabled: Boolean = DEFAULT_SHADOW_ENABLED
        set(value) {
            field = value
            measureBounds()
            invalidate()
        }

    /** Rectangle bounds of shadow to be drawn. */
    protected val shadowRect: RectF = RectF()

    /** The paint object to draw shadow. */
    protected val shadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        this.scaleType = ScaleType.CENTER_CROP
    }

    /**
     * Obtain XML attributes from context and apply them to this imageview.
     * This method have to be called at initializing.
     */
    @CallSuper
    protected open fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShapedImageView, defStyle, 0)

        try {
            if (a.hasValue(R.styleable.ShapedImageView_android_scaleType)) {
                val type = a.getInt(R.styleable.ShapedImageView_android_scaleType, ScaleType.CENTER_CROP.ordinal)
                this.scaleType = when (type) {
                    ScaleType.MATRIX.ordinal -> ScaleType.MATRIX
                    ScaleType.FIT_XY.ordinal -> ScaleType.FIT_XY
                    ScaleType.FIT_START.ordinal -> ScaleType.FIT_START
                    ScaleType.FIT_CENTER.ordinal -> ScaleType.FIT_CENTER
                    ScaleType.FIT_END.ordinal -> ScaleType.FIT_END
                    ScaleType.CENTER.ordinal -> ScaleType.CENTER
                    ScaleType.CENTER_CROP.ordinal -> ScaleType.CENTER_CROP
                    ScaleType.CENTER_INSIDE.ordinal -> ScaleType.CENTER_INSIDE
                    else -> throw IllegalArgumentException("$type is not a scale type.")
                }
            }

            if (a.hasValue(R.styleable.ShapedImageView_border_size)) {
                this.borderSize = a.getDimension(R.styleable.ShapedImageView_border_size, DEFAULT_BORDER_SIZE)
            }

            if (a.hasValue(R.styleable.ShapedImageView_border_color)) {
                this.borderColor = a.getColor(R.styleable.ShapedImageView_border_color, DEFAULT_BORDER_COLOR)
            }

            if (a.hasValue(R.styleable.ShapedImageView_border_enabled)) {
                this.borderEnabled = a.getBoolean(R.styleable.ShapedImageView_border_enabled, DEFAULT_BORDER_ENABLED)
            }

            if (a.hasValue(R.styleable.ShapedImageView_shadow_size)) {
                this.shadowSize = a.getDimension(R.styleable.ShapedImageView_shadow_size, DEFAULT_SHADOW_SIZE)
            }

            if (a.hasValue(R.styleable.ShapedImageView_shadow_color)) {
                this.shadowColor = a.getColor(R.styleable.ShapedImageView_shadow_color, DEFAULT_SHADOW_COLOR)
            }

            if (a.hasValue(R.styleable.ShapedImageView_shadow_enabled)) {
                this.shadowEnabled = a.getBoolean(R.styleable.ShapedImageView_shadow_enabled, DEFAULT_SHADOW_ENABLED)
            }
        } finally {
            a.recycle()
        }
    }

    /**
     * A lifecycle method for measuring this view's size.
     *
     * In this method, it measures the views width and height and call [measureBounds].
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)

        val borderAdjustment = if (borderEnabled) borderSize else 0f
        val shadowAdjustment = if (shadowEnabled) shadowSize else 0f
        val adjustmentSum = borderAdjustment + shadowAdjustment

        this.usableWidth = w - this.paddingLeft - this.paddingRight - adjustmentSum
        this.usableHeight = h - this.paddingTop - this.paddingBottom - adjustmentSum

        measureBounds(w.toFloat(), h.toFloat())
    }

    /**
     * Measure drawing bounds of this imageview's image, border and shadow.
     *
     * @param w The width size of this imageview. Default value is current width size.
     * @param h The height size of this imageview. Default value is current height size.
     */
    protected fun measureBounds(w: Float = this.width.toFloat(), h: Float = this.height.toFloat()) {
        val shadowAdjust = if (shadowEnabled) shadowSize else 0f
        val borderAdjust = if (borderEnabled) borderSize else 0f
        val adjustSum = shadowAdjust + borderAdjust

        if (shadowEnabled) {
            this.shadowRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                w - this.paddingRight - shadowAdjust,
                h - this.paddingBottom - shadowAdjust
            )
        }

        if (borderEnabled) {
            this.borderRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                w - this.paddingRight - shadowAdjust,
                h - this.paddingBottom - shadowAdjust
            )
        }

        this.imageRect.set(
            this.paddingLeft.toFloat() + adjustSum,
            this.paddingTop.toFloat() + adjustSum,
            w - this.paddingRight - adjustSum,
            h - this.paddingBottom - adjustSum
        )
    }

    /**
     * A lifecycle method for drawing image to this imageview.
     *
     * To override this method,
     * you should call super before drawing for update [imagePaint]'s bitmap shader
     * and shadow layer of [shadowPaint].
     */
    @CallSuper
    override fun onDraw(canvas: Canvas) {
        updateShader()
        updateShadowLayer()
    }

    /**
     * Update shader and apply it to [imagePaint].
     * If image bitmap need to update, update it before applying shader.
     */
    private fun updateShader() {
        /**
         * A local function for checking the necessity to update bitmap cache.
         */
        fun needToUpdateBitmap(): Boolean = this.drawable != null && this.drawable != this.imageCache

        val bounds = Bounds(
            usableWidth = this.usableWidth,
            usableHeight = this.usableHeight,
            paddingLeft = this.paddingLeft,
            paddingTop = this.paddingTop,
            paddingRight = this.paddingRight,
            paddingBottom = this.paddingBottom,
            borderAdjustment = if (borderEnabled) borderSize else 0f,
            shadowAdjustment = if (shadowEnabled) shadowSize else 0f
        )

        if (needToUpdateBitmap()) {
            this.imageCache = this.drawable
            this.image = this.drawable.toBitmap()
        }

        this.image?.let {
            val shader = if (Build.VERSION.SDK_INT >= 31) {
                BitmapShader(it, Shader.TileMode.DECAL, Shader.TileMode.DECAL)
            } else {
                BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }

            shader.setLocalMatrix(
                if (Build.VERSION.SDK_INT >= 31) {
                    when (this.scaleType) {
                        ScaleType.MATRIX -> this.imageMatrix
                        ScaleType.FIT_XY -> Matrix()
                        ScaleType.FIT_START -> Matrix()
                        ScaleType.FIT_CENTER -> Matrix()
                        ScaleType.FIT_END -> Matrix()
                        ScaleType.CENTER -> Matrix()
                        ScaleType.CENTER_CROP -> createCenterCropMatrix(it, bounds)
                        ScaleType.CENTER_INSIDE -> Matrix()
                        null -> throw IllegalArgumentException("Cannot apply null matrix to BitmapShader.")
                    }
                } else {
                    when (this.scaleType) {
                        ScaleType.MATRIX -> this.imageMatrix
                        ScaleType.FIT_XY -> createFitXYMatrix(it, bounds)
                        ScaleType.CENTER_CROP -> createCenterCropMatrix(it, bounds)
                        else -> Matrix()
                    }
                }
            )

            imagePaint.shader = shader
        }
    }

    /**
     * Update [shadowPaint]'s shadow layer.
     */
    private fun updateShadowLayer() {
        this.shadowPaint.setShadowLayer(shadowSize, 0f, shadowSize / 2, shadowColor)
    }

    /**
     * Controls how the image should be resized or moved to match the size of this ImageView.
     *
     * Android 31 and later versions support all scale types.
     * But previous versions support
     * [matrix][android.widget.ImageView.ScaleType.MATRIX],
     * [fit-xy][android.widget.ImageView.ScaleType.FIT_XY] and
     * [center-crop][android.widget.ImageView.ScaleType.CENTER_CROP],
     *
     * @param scaleType [android.widget.ImageView.ScaleType.CENTER_CROP]
     *
     * @throws IllegalArgumentException When given scale type is not supported.
     */
    override fun setScaleType(scaleType: ScaleType) {
        if (Build.VERSION.SDK_INT >= 31) {
            super.setScaleType(scaleType)
        } else {
            when (scaleType) {
                ScaleType.MATRIX,
                ScaleType.FIT_XY,
                ScaleType.CENTER_CROP -> super.setScaleType(scaleType)
                else -> throw IllegalArgumentException("ShapedImageView does not support ${scaleType.name}.")
            }
        }
    }
}
