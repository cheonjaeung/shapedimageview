package io.woong.shapedimageview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import java.lang.IllegalArgumentException

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

    init {
        scaleType = ScaleType.CENTER_CROP
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
    protected fun updateImage() {
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
}
