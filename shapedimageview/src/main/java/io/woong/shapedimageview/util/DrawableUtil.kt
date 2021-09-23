package io.woong.shapedimageview.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

/**
 * Convert this drawable to bitmap object.
 *
 * @return A converted bitmap object from drawable.
 * `null` if this drawable is null or failed to convert.
 */
internal fun Drawable?.toBitmap(): Bitmap? {
    return when {
        this == null -> {
            null
        }
        this is BitmapDrawable -> {
            this.bitmap
        }
        else -> {
            val bitmap = Bitmap.createBitmap(
                this.intrinsicWidth,
                this.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)

            this.apply {
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
            }

            bitmap
        }
    }
}
