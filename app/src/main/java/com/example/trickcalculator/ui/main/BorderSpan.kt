package com.example.trickcalculator.ui.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan
import android.util.Log

// copied from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
class BorderSpan(private val textColor: Int) : ReplacementSpan() {
    private val border: Paint = Paint() // = null
    private var width: Int  = 0

    private val padding = 8f
    private val postPadding = 20f

    init {
        border.style = Paint.Style.STROKE
        border.isAntiAlias = true
        // border.color = textColor
        border.color = Color.RED
        border.strokeWidth = 10f
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        width = paint.measureText(text.toString(), start, end).toInt() + 2 * padding.toInt()
        return width + postPadding.toInt()
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
        val textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent
        val startY = bottom - textHeight - padding
        val endY = startY + textHeight

        Log.e("text", textHeight.toString())
        Log.e("top", top.toString())
        Log.e("bottom", bottom.toString())
        Log.e("y", y.toString())
        Log.e("startY", startY.toString())
        Log.e("endY", endY.toString())

        val endX = x + width + 2 * padding
        // canvas.drawRect(x, top.toFloat(), endX, bottom.toFloat(), border)
        canvas.drawRect(x, startY, endX, endY, border)

        // paint.color = textColor //use the default text paint to preserve font size/style
        paint.color = textColor
        canvas.drawText(text!!, start, end, x + padding, y.toFloat(), paint)
    }
}