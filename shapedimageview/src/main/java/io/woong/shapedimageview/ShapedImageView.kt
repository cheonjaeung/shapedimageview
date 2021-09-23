@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package io.woong.shapedimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import io.woong.shapedimageview.util.Bounds
import io.woong.shapedimageview.util.createCenterCropMatrix
import io.woong.shapedimageview.util.toBitmap

/**
 * The parent class of all shaped image view.
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
        @ColorInt
        const val DEFAULT_BORDER_COLOR: Int = 0xFF444444.toInt()

        /** The default value of [ShapedImageView]'s border enabled status. */
        const val DEFAULT_BORDER_ENABLED: Boolean = true

        /** The default value of [ShapedImageView]'s shadow size. */
        const val DEFAULT_SHADOW_SIZE: Float = 0f

        /** The default value of [ShapedImageView]'s shadow color. */
        @ColorInt
        const val DEFAULT_SHADOW_COLOR: Int = 0xFF888888.toInt()

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

    /** The paint object to draw [image]. */
    protected val imagePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * The border size of this imageview.
     * Its unit is pixel.
     */
    var borderSize: Float = DEFAULT_BORDER_SIZE
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * Set [borderSize] of this imageview in dp unit.
     *
     * @param size The size value in dp unit.
     */
    fun setBorderSizeInDp(size: Int) {
        this.setBorderSizeInDp(size.toFloat())
    }

    /**
     * Set [borderSize] of this imageview in dp unit.
     *
     * @param size The size value in dp unit.
     */
    fun setBorderSizeInDp(size: Float) {
        this.borderSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            size,
            this.resources.displayMetrics
        )
        measureBounds(this.width.toFloat(), this.height.toFloat())
        invalidate()
    }

    /** The border color of this imageview. */
    @ColorInt
    var borderColor: Int = DEFAULT_BORDER_COLOR
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** The enabled status of border. */
    var borderEnabled: Boolean = DEFAULT_BORDER_ENABLED
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** The paint object to draw border. */
    protected val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * The shadow size of this imageview.
     * Its unit is pixel.
     */
    var shadowSize: Float = DEFAULT_SHADOW_SIZE
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * Set shadow size of this imageview in dp unit.
     *
     * @param size The size value in dp unit.
     */
    fun setShadowSizeInDp(size: Int) {
        this.setShadowSizeInDp(size.toFloat())
    }

    /**
     * Set shadow size of this imageview in dp unit.
     *
     * @param size The size value in dp unit.
     */
    fun setShadowSizeInDp(size: Float) {
        this.shadowSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            size,
            this.resources.displayMetrics
        )
        measureBounds(this.width.toFloat(), this.height.toFloat())
        invalidate()
    }

    /** The shadow color of this imageview. */
    @ColorInt
    var shadowColor: Int = DEFAULT_SHADOW_COLOR
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** The enalbed status of shadow. */
    var shadowEnabled: Boolean = DEFAULT_SHADOW_ENABLED
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** The paint object to draw shadow. */
    protected val shadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        this.scaleType = ScaleType.CENTER_CROP
    }

    /**
     * Obtain XML attributes from context and apply them to this imageview.
     * This method should be called when initialization.
     */
    protected abstract fun applyAttributes(attrs: AttributeSet?, defStyle: Int)

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
     * @param viewWidth Width size of this imageview.
     * @param viewHeight Height size of this imageview.
     */
    protected abstract fun measureBounds(viewWidth: Float, viewHeight: Float)

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
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(
                when (this.scaleType) {
                    ScaleType.CENTER_CROP -> createCenterCropMatrix(it, bounds)
                    else -> Matrix()
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
     * [scaleType] should be [center crop][android.widget.ImageView.ScaleType.CENTER_CROP].
     * If otherwise, it will be throw [IllegalArgumentException].
     *
     * @param scaleType [android.widget.ImageView.ScaleType.CENTER_CROP]
     *
     * @throws IllegalArgumentException When given scale type is not center crop.
     */
    override fun setScaleType(scaleType: ScaleType) {
        if (scaleType == ScaleType.CENTER_CROP) {
            super.setScaleType(scaleType)
        } else {
            throw IllegalArgumentException("Scale type have to be center crop.")
        }
    }
}
