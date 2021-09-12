package com.clonecoding.recorder_clonecoding

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Count up text view
 */
class CountUpTextView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    /**
     * Start real time
     */
    private var startTimeStamp: Long = 0L

    /**
     * Count up runnable
     */
    private val countUpAction: Runnable = object : Runnable {

        override fun run() {

            val currentTimeStamp = SystemClock.elapsedRealtime()

            val countTimeSeconds = ((currentTimeStamp - startTimeStamp) / 1000L).toInt()

            updateCountTime(countTimeSeconds)

            handler?.postDelayed(this, 1000L)
        }
    }

    /**
     * Start count up
     */
    fun startCountUp() {

        this.startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(this.countUpAction)
    }

    /**
     * Stop count up
     */
    fun stopCountUp() {

        handler?.removeCallbacks(this.countUpAction)
    }

    /**
     * Update time
     */
    private fun updateCountTime(countTimeSeconds: Int) {

        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }

    /**
     * Clear time
     */
    fun clearCountTime() {

        this.updateCountTime(0)
    }
}