package com.example.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout

class ColorSelector @JvmOverloads
constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0)
: LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {


    private val colorSelectorArrowLeft: ImageView by lazy {findViewById(R.id.colorSelectorArrowLeft)}
    private val colorSelectorArrowRight: ImageView by lazy { findViewById(R.id.colorSelectorArrowRight)}
    private val colorEnabled: CheckBox by lazy { findViewById(R.id.colorEnabled)}
    private val selectedColor: View by lazy { findViewById(R.id.selectedColor)}

    private var listOfColors = listOf(Color.BLUE, Color.RED, Color.GREEN)
    private var selectedColorIndex = 0
    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorSelector)
        listOfColors = typedArray.getTextArray(R.styleable.ColorSelector_colors)
            .map {
                Color.parseColor(it.toString())
            }
        typedArray.recycle()

        orientation = HORIZONTAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector, this)

        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])

        colorSelectorArrowLeft.setOnClickListener {
            selectPreviousColor()
        }

        colorSelectorArrowRight.setOnClickListener {
            selectNextColor()
        }

        colorEnabled.setOnCheckedChangeListener { buttonView, isChecked ->
            broadcastColor()
        }
    }

    var selectedColorValue : Int = android.R.color.transparent
    set(value) {
        var index = listOfColors.indexOf(value)
        if (index == -1) {
            colorEnabled.isChecked = false
            index = 0
        } else {
            colorEnabled.isChecked = true
        }
        selectedColorIndex = index
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
    }
    private var colorSelectedListeners: ArrayList<(Int) -> Unit> = arrayListOf()

    fun addListener(color: (Int) -> Unit) {
        colorSelectedListeners.add(color)
    }

    private fun selectNextColor() {
        if (selectedColorIndex == listOfColors.lastIndex) {
            selectedColorIndex = 0
        } else {
            selectedColorIndex++
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun selectPreviousColor() {
        if (selectedColorIndex == 0) {
            selectedColorIndex = listOfColors.lastIndex
        } else {
            selectedColorIndex--
        }
        selectedColor.setBackgroundColor(listOfColors[selectedColorIndex])
        broadcastColor()
    }

    private fun broadcastColor() {
        val color = if (colorEnabled.isChecked)
            listOfColors[selectedColorIndex]
        else
            Color.TRANSPARENT
        colorSelectedListeners.forEach{ function ->
            function(color)
        }
    }
}