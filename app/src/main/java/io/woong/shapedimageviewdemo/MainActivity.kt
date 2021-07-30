package io.woong.shapedimageviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        findViewById<AppCompatSeekBar>(R.id.padding_seekbar).setOnSeekBarChangeListener(this)
        findViewById<AppCompatSeekBar>(R.id.shadow_seekbar).setOnSeekBarChangeListener(this)
        findViewById<AppCompatSeekBar>(R.id.border_seekbar).setOnSeekBarChangeListener(this)
        findViewById<AppCompatSeekBar>(R.id.values_seekbar).setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
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
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) { /* Not needed. */ }

    override fun onStopTrackingTouch(seekBar: SeekBar?) { /* Not needed. */ }

    private fun changePaddings(padding: Int) {
        circleView.setPadding(padding)
        roundedSquareView.setPadding(padding)
        squareView.setPadding(padding)
        squircleView.setPadding(padding)
    }

    private fun changeShadows(shadowSize: Float) {
        circleView.shadowSize = shadowSize
        roundedSquareView.shadowSize = shadowSize
        squareView.shadowSize = shadowSize
        squircleView.shadowSize = shadowSize
    }

    private fun changeBorders(borderSize: Float) {
        circleView.borderSize = borderSize
        roundedSquareView.borderSize = borderSize
        squareView.borderSize = borderSize
        squircleView.borderSize = borderSize
    }

    private fun changeValues(value: Float) {
        roundedSquareView.imageRadius = value
        squircleView.imageCurvature = value
    }
}
