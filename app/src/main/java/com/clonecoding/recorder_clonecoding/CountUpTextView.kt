package com.clonecoding.recorder_clonecoding

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class CountUpTextView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var startTimeStamp: Long = 0L

    private val countUpAction: Runnable = object : Runnable {

        override fun run() {

            val currentTimeStamp = SystemClock.elapsedRealtime()

            val countTimeSeconds = ((currentTimeStamp - startTimeStamp) / 1000L).toInt()

            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp() {

        this.startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(this.countUpAction)
    }

    fun stopCountUp() {

        handler?.removeCallbacks(this.countUpAction)
    }

    private fun updateCountTime(countTimeSeconds: Int) {

        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }
}