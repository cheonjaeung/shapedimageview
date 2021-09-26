package io.woong.shapedimageviewdemo

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import io.woong.shapedimageview.ShapedImageView
import io.woong.shapedimageview.formula.SuperEllipseFormula
import io.woong.shapedimageview.widget.CutCornerImageView
import io.woong.shapedimageview.widget.FormulableImageView
import io.woong.shapedimageview.widget.OvalImageView
import io.woong.shapedimageview.widget.RoundImageView

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {

    private val oval: OvalImageView by lazy { findViewById(R.id.oval) }
    private val round: RoundImageView by lazy { findViewById(R.id.round) }
    private val cutCorner: CutCornerImageView by lazy { findViewById(R.id.cut_corner) }
    private val formulable: FormulableImageView by lazy { findViewById(R.id.formulable) }

    private val shapeGroup: RadioGroup by lazy { findViewById(R.id.shape_group) }
    private val ovalShapeButton: RadioButton by lazy { findViewById(R.id.oval_shape) }
    private val roundShapeButton: RadioButton by lazy { findViewById(R.id.round_shape) }
    private val cutCornerShapeButton: RadioButton by lazy { findViewById(R.id.cut_corner_shape) }
    private val formulableShapeButton: RadioButton by lazy { findViewById(R.id.formulable_shape) }

    private val widthSeekBar: SeekBar by lazy { findViewById(R.id.width_seekbar) }
    private val heightSeekBar: SeekBar by lazy { findViewById(R.id.height_seekbar) }
    private val paddingSeekBar: SeekBar by lazy { findViewById(R.id.padding_seekbar) }
    private val borderSizeSeekBar: SeekBar by lazy { findViewById(R.id.border_size_seekbar) }
    private val shadowSizeSeekBar: SeekBar by lazy { findViewById(R.id.shadow_size_seekbar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shapeGroup.setOnCheckedChangeListener(this)

        formulable.formula = SuperEllipseFormula(3f)

        widthSeekBar.apply {
            max = 200
            progress = 100
            setOnSeekBarChangeListener(this@MainActivity)
        }
        heightSeekBar.apply {
            max = 200
            progress = 100
            setOnSeekBarChangeListener(this@MainActivity)
        }
        paddingSeekBar.apply {
            max = 16
            progress = 0
            setOnSeekBarChangeListener(this@MainActivity)
        }
        borderSizeSeekBar.apply {
            max = 8
            progress = 0
            setOnSeekBarChangeListener(this@MainActivity)
        }
        shadowSizeSeekBar.apply {
            max = 8
            progress = 0
            setOnSeekBarChangeListener(this@MainActivity)
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        if (group?.id == shapeGroup.id) {
            val visible = View.VISIBLE
            val gone = View.GONE

            when (checkedId) {
                ovalShapeButton.id -> {
                    oval.visibility = visible
                    round.visibility = gone
                    cutCorner.visibility = gone
                    formulable.visibility = gone
                }
                roundShapeButton.id -> {
                    oval.visibility = gone
                    round.visibility = visible
                    cutCorner.visibility = gone
                    formulable.visibility = gone
                }
                cutCornerShapeButton.id -> {
                    oval.visibility = gone
                    round.visibility = gone
                    cutCorner.visibility = visible
                    formulable.visibility = gone
                }
                formulableShapeButton.id -> {
                    oval.visibility = gone
                    round.visibility = gone
                    cutCorner.visibility = gone
                    formulable.visibility = visible
                }
            }
        }
    }

    // Do Nothing
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            widthSeekBar.id -> {
                val newSize = (seekBar.progress + 100).dp
                changeWidth(oval, newSize)
                changeWidth(round, newSize)
                changeWidth(cutCorner, newSize)
                changeWidth(formulable, newSize)
            }
            heightSeekBar.id -> {
                val newSize = (seekBar.progress + 100).dp
                changeHeight(oval, newSize)
                changeHeight(round, newSize)
                changeHeight(cutCorner, newSize)
                changeHeight(formulable, newSize)
            }
            paddingSeekBar.id -> {
                val newPadding = seekBar.progress.dp
                changePadding(oval, newPadding)
                changePadding(round, newPadding)
                changePadding(cutCorner, newPadding)
                changePadding(formulable, newPadding)
            }
            borderSizeSeekBar.id -> {
                val newSize = seekBar.progress.dpf
                changeBorderSize(oval, newSize)
                changeBorderSize(round, newSize)
                changeBorderSize(cutCorner, newSize)
                changeBorderSize(formulable, newSize)
            }
            shadowSizeSeekBar.id -> {
                val newSize = seekBar.progress.dpf
                changeShadowSize(oval, newSize)
                changeShadowSize(round, newSize)
                changeShadowSize(cutCorner, newSize)
                changeShadowSize(formulable, newSize)
            }
        }
    }

    // Do Nothing
    override fun onStartTrackingTouch(seekBar: SeekBar?) { }

    // Do Nothing
    override fun onStopTrackingTouch(seekBar: SeekBar?) { }

    private val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()

    private val Int.dpf: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        )

    private fun changeWidth(view: View, newSize: Int) {
        val params = view.layoutParams
        params.width = newSize
        view.layoutParams = params
    }

    private fun changeHeight(view: View, newSize: Int) {
        val params = view.layoutParams
        params.height = newSize
        view.layoutParams = params
    }

    private fun changePadding(view: View, newSize: Int) {
        view.setPadding(newSize, newSize, newSize, newSize)
    }

    private fun changeBorderSize(view: ShapedImageView, newSize: Float) {
        view.borderSize = newSize
    }

    private fun changeShadowSize(view: ShapedImageView, newSize: Float) {
        view.shadowSize = newSize
    }
}
