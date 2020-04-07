package com.wielabs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.roundToInt

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    private fun moveLeft(imageView: ImageView, targetTextView: TextView) {
        imageView.animate().setDuration(1000).translationXBy(-(imageView.x - targetTextView.x) + targetTextView.width / 2 - getDpFromPx(targetTextView.paddingStart)).start()
    }

    private fun moveRight(imageView: ImageView, targetTextView: TextView) {
        imageView.animate().setDuration(1000).translationXBy(targetTextView.x + targetTextView.width / 2 - getDpFromPx(targetTextView.paddingStart) - imageView.x).start()
    }

    private fun getDpFromPx(px: Int): Int {
        val scale = resources.displayMetrics.density
        return ((px - 0.5f) / scale).roundToInt()
    }
}
