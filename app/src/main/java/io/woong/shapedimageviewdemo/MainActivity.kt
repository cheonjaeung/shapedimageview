package io.woong.shapedimageviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.view.setPadding
import io.woong.shapedimageview.widget.CircularImageView
import io.woong.shapedimageview.widget.RoundedSquareImageView
import io.woong.shapedimageview.widget.SquareImageView
import io.woong.shapedimageview.widget.SquircularImageView

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var circleView: CircularImageView
    private lateinit var roundedSquareView: RoundedSquareImageView
    private lateinit var squareView: SquareImageView
    private lateinit var squircleView: SquircularImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        circleView = findViewById(R.id.circle)
        roundedSquareView = findViewById(R.id.rounded_square)
        squareView = findViewById(R.id.square)
        squircleView = findViewById(R.id.squircle)

        findViewById<AppCompatSeekBar>(R.id.padding_seekbar).apply {
            setOnSeekBarChangeListener(this@MainActivity)
            progress = 0
        }

        findViewById<AppCompatSeekBar>(R.id.shadow_seekbar).apply {
            setOnSeekBarChangeListener(this@MainActivity)
            progress = 4
        }

        findViewById<AppCompatSeekBar>(R.id.border_seekbar).apply {
            setOnSeekBarChangeListener(this@MainActivity)
            progress = 8
        }

        findViewById<AppCompatSeekBar>(R.id.values_seekbar).apply {
            setOnSeekBarChangeListener(this@MainActivity)
            progress = 75
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.padding_seekbar -> {
                changePaddings(progress)
            }
            R.id.shadow_seekbar -> {
                changeShadows(progress.toFloat())
            }
            R.id.border_seekbar -> {
                changeBorders(progress.toFloat())
            }
            R.id.values_seekbar -> {
                changeValues(progress.toFloat())
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) { /* Not needed. */ }

    override fun onStopTrackingTouch(seekBar: SeekBar?) { /* Not needed. */ }

    private fun changePaddings(padding: Int) {
        val p = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            padding.toFloat(),
            resources.displayMetrics
        ).toInt()

        circleView.setPadding(p)
        roundedSquareView.setPadding(p)
        squareView.setPadding(p)
        squircleView.setPadding(p)
    }

    private fun changeShadows(shadowSize: Float) {
        val s = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            shadowSize,
            resources.displayMetrics
        )

        circleView.shadowSize = s
        roundedSquareView.shadowSize = s
        squareView.shadowSize = s
        squircleView.shadowSize = s
    }

    private fun changeBorders(borderSize: Float) {
        val b = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            borderSize,
            resources.displayMetrics
        )

        circleView.borderSize = b
        roundedSquareView.borderSize = b
        squareView.borderSize = b
        squircleView.borderSize = b
    }

    private fun changeValues(value: Float) {
        roundedSquareView.imageRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            0.48f * value,
            resources.displayMetrics
        )

        squircleView.imageCurvature = value / 25 + 1
    }
}
