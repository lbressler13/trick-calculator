package com.example.trickcalculator.ui.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log

// copied from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
class BorderSpan(private val textColor: Int) : ReplacementSpan() {
    private val border: Paint = Paint() // = null
    private val background: Paint = Paint() // = null
    private var width: Int  = 0

    init {
        Log.e(null, "init")
        border.style = Paint.Style.STROKE
        border.isAntiAlias = true
        border.color = Color.RED
        border.strokeWidth = 10f

        // background.style = Paint.Style.FILL
        // background.isAntiAlias = true
        // background.color = Color.GREEN
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        width = paint.measureText(text.toString(), start, end).toInt()
        return width
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val padding = 4f
        paint.color = Color.MAGENTA // use the default text paint to preserve font size/style
        canvas.drawRect(x, top.toFloat(), x + width + 2 * padding, bottom.toFloat(), border)

        // paint.color = textColor //use the default text paint to preserve font size/style
        canvas.drawText(text!!, start, end, x + padding, y.toFloat(), paint)
    }
}