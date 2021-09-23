package io.woong.shapedimageview.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import io.woong.shapedimageview.R
import io.woong.shapedimageview.util.createCenterCropMatrix
import io.woong.shapedimageview.util.toBitmap

/**
 * The shaped image view that draw image in oval shape.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class OvalImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_BORDER_SIZE: Float = 0f
        @ColorInt
        const val DEFAULT_BORDER_COLOR: Int = 0xFF444444.toInt()
        const val DEFAULT_BORDER_ENABLED: Boolean = true

        const val DEFAULT_SHADOW_SIZE: Float = 0f
        @ColorInt
        const val DEFAULT_SHADOW_COLOR: Int = 0xFF888888.toInt()
        const val DEFAULT_SHADOW_ENABLED: Boolean = true
    }

    /** Image bitmap to draw in this imageview. */
    private var image: Bitmap? = null

    /** Image drawable to check this imageview need to update bitmap. */
    private var imageCache: Drawable? = null

    /** Paint object for drawing image. */
    private val imagePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** Rectangle bounds of image to be drawn. */
    private val imageRect: RectF = RectF()

    /** Paint object for drawing border. */
    private val borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** Rectangle bounds of border to be drawn. */
    private val borderRect: RectF = RectF()

    /** Border size of this imageview in pixel. */
    var borderSize: Float = 0f
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * Set border size of this imageview in dp.
     *
     * @param size The size value in dp unit.
     */
    fun setBorderSizeInDp(size: Int) {
        this.setBorderSizeInDp(size.toFloat())
    }

    /**
     * Set border size of this imageview in dp.
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

    /** Integer color code of this imageview's border.*/
    @ColorInt
    var borderColor: Int = 0xFF888888.toInt()
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Enabled status of this imageview's border. */
    var borderEnabled: Boolean = true
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Paint object for drawing shadow. */
    private val shadowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** Rectangle bounds of shadow to be drawn. */
    private val shadowRect: RectF = RectF()

    /** Shadow size of this imageview in pixel. */
    var shadowSize: Float = 0f
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /**
     * Set shadow size of this imageview in dp.
     *
     * @param size The size value in dp unit.
     */
    fun setShadowSizeInDp(size: Int) {
        this.setShadowSizeInDp(size.toFloat())
    }

    /**
     * Set shadow size of this imageview in dp.
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

    /** Integer color code of this imageview's shadow. */
    @ColorInt
    var shadowColor: Int = 0xFF888888.toInt()
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    /** Enabled status of this imageview's shadow. */
    var shadowEnabled: Boolean = true
        set(value) {
            field = value
            measureBounds(this.width.toFloat(), this.height.toFloat())
            invalidate()
        }

    init {
        setDefaultScaleType()
        applyAttributes(attrs, defStyle)
    }

    /**
     * Set scale type to center crop.
     */
    private fun setDefaultScaleType() {
        this.scaleType = ScaleType.CENTER_CROP
    }

    /**
     * Obtain XML attributes from context and apply them to this imageview.
     */
    private fun applyAttributes(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.OvalImageView, defStyle, 0)

        try {
            this.borderSize = a.getDimension(R.styleable.OvalImageView_border_size, DEFAULT_BORDER_SIZE)
            this.borderColor = a.getColor(R.styleable.OvalImageView_border_color, DEFAULT_BORDER_COLOR)
            this.borderEnabled = a.getBoolean(R.styleable.OvalImageView_border_enabled, DEFAULT_BORDER_ENABLED)
            this.shadowSize = a.getDimension(R.styleable.OvalImageView_shadow_size, DEFAULT_SHADOW_SIZE)
            this.shadowColor = a.getColor(R.styleable.OvalImageView_shadow_color, DEFAULT_SHADOW_COLOR)
            this.shadowEnabled = a.getBoolean(R.styleable.OvalImageView_shadow_enabled, DEFAULT_SHADOW_ENABLED)
        } finally {
            a.recycle()
        }
    }

    /**
     * A lifecycle method for measuring this view's size.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, h)
        measureBounds(w.toFloat(), h.toFloat())
    }

    /**
     * Measure a rectangle drawing bounds of this imageview's image, border and shadow.
     */
    private fun measureBounds(width: Float, height: Float) {
        val shadowAdjust = if (shadowEnabled) shadowSize else 0f
        val borderAdjust = if (borderEnabled) borderSize else 0f
        val adjustSum = shadowAdjust + borderAdjust

        if (shadowEnabled) {
            this.shadowRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                width - this.paddingRight - shadowAdjust,
                height - this.paddingBottom - shadowAdjust
            )
        }

        if (borderEnabled) {
            this.borderRect.set(
                this.paddingLeft.toFloat() + shadowAdjust,
                this.paddingTop.toFloat() + shadowAdjust,
                width - this.paddingRight - shadowAdjust,
                height - this.paddingBottom - shadowAdjust
            )
        }

        this.imageRect.set(
            this.paddingLeft.toFloat() + adjustSum,
            this.paddingTop.toFloat() + adjustSum,
            width - this.paddingRight - adjustSum,
            height - this.paddingBottom - adjustSum
        )
    }

    /**
     * A lifecycle method for drawing image to this view.
     */
    override fun onDraw(canvas: Canvas) {
        updateShader()
        updateShadowLayer()

        if (shadowEnabled) {
            canvas.drawOval(shadowRect, shadowPaint)
        }

        if (borderEnabled) {
            canvas.drawOval(borderRect, borderPaint)
        }

        canvas.drawOval(imageRect, imagePaint)
    }

    /**
     * Update shader and apply it to [imagePaint].
     * If image bitmap need to update, update it before shader.
     */
    private fun updateShader() {
        if (needToUpdateBitmap()) {
            this.imageCache = this.drawable
            this.image = this.drawable.toBitmap()
        }

        this.image?.let {
            val shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shader.setLocalMatrix(
                when (this.scaleType) {
                    ScaleType.CENTER_CROP -> createCenterCropMatrix(
                        it,
                        this.imageRect.width(),
                        this.imageRect.height(),
                        this.paddingLeft,
                        this.paddingTop
                    )

                    else -> Matrix()
                }
            )
            imagePaint.shader = shader
        }
    }

    /**
     * Check the necessity to update bitmap cache.
     */
    private fun needToUpdateBitmap(): Boolean = this.drawable != null && this.drawable != this.imageCache

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
