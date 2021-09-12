package com.clonecoding.recorder_clonecoding

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

/**
 *
 */
class SoundVisualizerView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    /**
     * Paint
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth: Int = 0

    private var drawingHeight: Int = 0

    private var drawingAmplitudes: List<Int> = emptyList()

    private var isReplaying: Boolean = false

    var onRequestCurrentAmplitude: (() -> Int)? = null

    private var replayingPosition: Int = 0

    /**
     * Visualize sound
     */
    private val visualizeRepeatAction: Runnable = object : Runnable {

        override fun run() {

            if(isReplaying == false) {

                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0

                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            invalidate()

            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    /**
     * Size init
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.drawingWidth = w
        this.drawingHeight = h
    }

    /**
     * onDraw
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = this.drawingHeight / 2f
        var offsetX = this.drawingWidth.toFloat()

        this.drawingAmplitudes
            .let {
                if (this.isReplaying) {
                    it.takeLast(this.replayingPosition)
                } else {
                    it
                }
            }
            .forEach {
            val lineLength = it / MAX_AMPLITUDE * drawingHeight * 0.8F

            offsetX -= LINE_SPACE
            if (offsetX < 0) {
                return@forEach
            }

            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2F,
                offsetX,
                centerY + lineLength / 2F,
                this.amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {

        this.isReplaying = isReplaying
        this.handler?.post(this.visualizeRepeatAction)
    }

    fun stopVisualizing() {

        this.handler?.removeCallbacks(this.visualizeRepeatAction)
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}