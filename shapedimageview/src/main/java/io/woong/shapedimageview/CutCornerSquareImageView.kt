package io.woong.shapedimageview

import android.content.Context
import android.util.AttributeSet

/**
 * The shaped imageview that draws image in corner-cut square shape.
 *
 * The difference between [CutCornerImageView] and this view is that
 * it has same with and height size.
 *
 * If width or height size is smaller than another,
 * this view's size will be set to the smaller one.
 *
 * Aspect ratio setting is not working for this view
 * because this view always has same width and height.
 *
 * To use this imageview in xml, you can add this view simply like below code.
 *
 * ```
 *      <io.woong.shapedimageview.CutCornerSquareImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample" />
 * ```
 *
 * In [CutCornerSquareImageView], there are some attributes to configure this imageview.
 * The most important attribute is `cut size`.
 *
 * ```
 *      <!-- To apply all corner cut-size in one line. -->
 *      <io.woong.shapedimageview.CutCornerSquareImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:cut_size="32dp" />
 *
 *      <!-- To apply each corner cut-size. -->
 *      <io.woong.shapedimageview.CutCornerSquareImageView
 *          android:layout_width="200dp"
 *          android:layout_height="200dp"
 *          android:src="@drawable/sample"
 *          app:top_left_cut_size="8dp"
 *          app:top_right_cut_size="12dp"
 *          app:bottom_right_cut_size="16dp"
 *          app:bottom_left_cut_size="24dp" />
 * ```
 */
class CutCornerSquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CutCornerImageView(context, attrs, defStyle) {

    init {
        applyAttributes(attrs, defStyle)
        this.isRegularShape = true
    }
}
