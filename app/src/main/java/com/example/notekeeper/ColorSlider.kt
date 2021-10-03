package com.example.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar
import androidx.core.content.ContextCompat

class ColorSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle,
    defStyleRes: Int = 0
) : SeekBar(context, attrs, defStyleAttr, defStyleRes) {
    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    val w = getPixelValueFromDP(16f) //width of color swatch
    private val h = getPixelValueFromDP(16f) //height of color swatch
    private val halfW = if (w >= 0) w / 2f else 1f
    private val halfH = if (h >= 0) h / 2f else 1f
    private val paint = Paint()
    private var noColorDrawable: Drawable? = null
        set(value) {
            w2 = value?.intrinsicWidth ?: 0
            h2 = value?.intrinsicHeight ?: 0
            halfW2 = if (w2 >= 0) w2 / 2 else 1
            halfH2 = if (h2 >= 0) h2 / 2 else 1
            value?.setBounds(-halfW2, -halfH2, halfW2, halfH2)
            field = value
        }
    var w2 = 0
    private var h2 = 0
    private var halfW2 = 1
    private var halfH2 = 1

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map { Color.parseColor(it.toString()) } as ArrayList<Int>
        } finally {
            typedArray.recycle()
        }
        colors.add(0, Color.TRANSPARENT) //could also use android.R.color.transparent
        max = colors.size - 1
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false
        setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom + getPixelValueFromDP(16f).toInt())
        thumb = context.getDrawable(R.drawable.ic_color_slider_thumb)
        noColorDrawable = context.getDrawable(R.drawable.ic_baseline_clear_24)

        setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach {
                    it(colors[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    var selectedColorValue: Int = Color.TRANSPARENT
    set(value) {
        val index = colors.indexOf(value)
        progress = if (index == -1) {
            0
        } else {
            index
        }
    }

    private val listeners: ArrayList<(Int) -> Unit> = arrayListOf()
    fun addListener (function: (Int) -> Unit) {
        listeners.add(function)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMark(canvas)
    }

    private fun drawTickMark(canvas: Canvas?) {
        canvas?.let {
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingStart.toFloat(), (height / 2).toFloat() + getPixelValueFromDP(16f))
            val spacing: Float = (width - paddingStart - paddingEnd) / (count - 1).toFloat()

            if (count > 1) {
                for (i in 0 until count) {
                    if (i == 0) {
                        noColorDrawable?.draw(canvas)
                    } else {
                        paint.color = colors[i]
                        canvas.drawRect(-halfW, -halfH, halfW, halfH, paint)
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    private fun getPixelValueFromDP(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)
    }
}