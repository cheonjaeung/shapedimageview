package io.woong.shapedimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.lang.IllegalArgumentException
import kotlin.math.min

/**
 * Root image view class of all shaped image views in [ShapedImageView Library](https://github.com/woongdev/ShapedImageView)
 *
 * It contains common properties and methods for implementing shaped image view.
 * But it does not provides features for all shapes.
 * So, all shaped image views should inheritances this class and implements some methods.
 *
 * It's scale type is always [center crop][android.widget.ImageView.ScaleType.CENTER_CROP] and cannot change it.
 * If try to change scale type, it will be make [IllegalArgumentException].
 *
 * It's width and height size is always same.
 */
abstract class ShapedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    /** Bitmap image for drawing shaped image. */
    private var image: Bitmap? = null
    /** Image drawable for checking is image needs update. */
    private var imageCache: Drawable? = null
    /** Paint object for drawing shaped image. */
    protected var imagePaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        alpha = 255
    }
    /** Width and height size of image. */
    protected var imageSize: Int = 0

    init {
        scaleType = ScaleType.CENTER_CROP
    }

    /**
     * Set scale type of this image view.
     *
     * Only can accept [CENTER_CROP][android.widget.ImageView.ScaleType.CENTER_CROP],
     * Other scale types will be denied.
     *
     * @param scaleType A scale type mode.
     *
     * @throws IllegalArgumentException When given scale type is not supported.
     */
    override fun setScaleType(scaleType: ScaleType) {
        when (scaleType) {
            ScaleType.CENTER_CROP -> {
                super.setScaleType(scaleType)
            }
            else -> throw IllegalArgumentException("ScaleType $scaleType not supported.")
        }
    }

    /**
     * Measure and set view size and call [postOnMeasure] method.
     *
     * It makes view size to square size.
     * It means width and height size is same.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(width, height)
        setMeasuredDimension(size, size)

        val usableWidth = width - paddingLeft - paddingRight
        val usableHeight = height - paddingTop - paddingBottom
        imageSize = min(usableWidth, usableHeight)

        postOnMeasure(widthMeasureSpec, heightMeasureSpec, size)
    }

    /**
     * This method is invoked after [onMeasure].
     *
     * @param widthMeasureSpec Specs of width.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param heightMeasureSpec Specs of height.
     * You can access mode and size as [MeasureSpec][android.view.View.MeasureSpec].
     * @param size Size of view. (width and height is same)
     */
    protected abstract fun postOnMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int, size: Int)

    /**
     * Check image is outdated and update it if it needs. And call [postOnDraw] method.
     *
     * For drawing something, use [postOnDraw].
     */
    override fun onDraw(canvas: Canvas) {
        updateImage()
        postOnDraw(canvas)
    }

    /**
     * Update image and paint shader.
     *
     * It loads drawable from original drawable of AppCompatImageView
     * and convert it to bitmap and save it.
     * And also apply shader on paint object.
     *
     * **Note: This method should be called before drawing image.**
     */
    private fun updateImage() {
        if (drawable != null || imageCache != drawable) {
            imageCache = drawable
            image = drawableToBitmap(drawable)
        }
    }

    /**
     * Convert drawable to bitmap object.
     *
     * @param drawable Source drawable.
     *
     * @return A bitmap image from given drawable.
     */
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.apply {
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
            }
            bitmap
        }
    }

    /**
     * Update paint shader.
     *
     * It sets shader of [imagePaint] and set matrix depending on scale type.
     *
     * @param imageWidth Width size of displaying image for setting scale type matrix.
     * @param imageHeight Height size of displaying image for setting scale type matrix.
     */
    protected fun updateShader(imageWidth: Int, imageHeight: Int) {
        image?.let { img ->
            val shader = BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP).apply {
                setLocalMatrix(
                    when (scaleType) {
                        ScaleType.CENTER_CROP -> createCenterCropMatrix(img, imageWidth, imageHeight)
                        else -> Matrix()
                    }
                )
            }
            imagePaint.shader = shader
        }
    }

    /**
     * Create a bitmap shader matrix for center-crop scale type.
     *
     * @param image A bitmap image to display.
     * @param width Width size to display.
     * @param height Height size to display.
     *
     * @return Scaled and translated matrix.
     *
     * @see android.widget.ImageView.ScaleType.CENTER_CROP
     */
    private fun createCenterCropMatrix(image: Bitmap, width: Int, height: Int): Matrix {
        return Matrix().apply {
            val scale: Float
            val dx: Float
            val dy: Float

            if (image.width * width > image.height * height) {
                scale = height / image.height.toFloat()
                dx = (width - image.width * scale) * 0.5f
                dy = 0f
            } else {
                scale = width / image.width.toFloat()
                dx = 0f
                dy = (height - image.height * scale) * 0.5f
            }

            setScale(scale, scale)
            postTranslate(dx, dy)
        }
    }

    /**
     * This method is invoked after [onDraw].
     *
     * @param canvas Canvas to draw image view.
     */
    protected abstract fun postOnDraw(canvas: Canvas)
}
