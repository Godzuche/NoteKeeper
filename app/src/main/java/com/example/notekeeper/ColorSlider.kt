package com.example.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.core.content.ContextCompat

class ColorSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle,
    defStyleRes: Int = 0
) : SeekBar(context, attrs, defStyleAttr, defStyleRes) {
    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map { Color.parseColor(it.toString()) } as ArrayList<Int>
        } finally {
            typedArray.recycle()
        }
        max = colors.size - 1
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false
        setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom + 50)
        thumb = context.getDrawable(R.drawable.ic_color_slider_thumb)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMark(canvas)
    }

    private fun drawTickMark(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingStart.toFloat(), (height / 2).toFloat() + 50f)
            if (count > 1) {
                for (i in 0 until count) {
                    val w = 48f //width of color swatch
                    val h = 48f //height of color swatch
                    val halfW = if (w >= 0) w / 2f else 1f
                    val halfH = if (h >= 0) h / 2f else 1f

                    val spacing: Float = (width - paddingStart - paddingEnd) / (count - 1).toFloat()

                    val paint = Paint()
                    paint.color = colors[i]
                    canvas.drawRect(-halfW, -halfH, halfW, halfH, paint)
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }
}