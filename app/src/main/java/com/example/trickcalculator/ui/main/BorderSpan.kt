package com.example.trickcalculator.ui.main

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan

// adapted from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
class BorderSpan(private val textColor: Int) : ReplacementSpan() {
    private val border: Paint = Paint() // = null
    private var width: Int  = 0

    private val innerPadding = 12f
    private val outerPadding = 12f

    init {
        border.style = Paint.Style.STROKE
        border.isAntiAlias = true
        border.color = textColor
        border.strokeWidth = 10f
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        width = paint.measureText(text.toString(), start, end).toInt() + 2 * innerPadding.toInt()
        return width + 2 * innerPadding.toInt() + outerPadding.toInt()
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
        text!!

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val startX = bounds.left + outerPadding // bounds.width() / 2f

        paint.color = textColor
        canvas.drawText(text, start, end, startX, y.toFloat(), paint)

        val topY = bounds.top + y - innerPadding
        val bottomY = bounds.bottom + y + innerPadding
        val endX = startX + width - innerPadding
        canvas.drawRect(startX - innerPadding, topY, endX, bottomY, border)
    }
}