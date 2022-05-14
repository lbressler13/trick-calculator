package com.example.trickcalculator.ui.borderspan

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan

// adapted from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
open class BorderSpan protected constructor(private val textColor: Int, private val parentWidth: Int) : ReplacementSpan() {
    protected val borderPaint: Paint = Paint()
    protected var bounds = Rect()

    private val innerPadding = 12f
    private val outerPadding = 0f // 12f

    private var width = 0

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = textColor
        borderPaint.strokeWidth = 10f
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val s = text!!.substring(start, end)
        val bounds = Rect()
        paint.getTextBounds(s, 0, s.length, bounds)
        width = bounds.width()
        return (width + 4 * innerPadding + outerPadding).toInt()
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
        val s = text!!.substring(start, end)
        val bounds = getBounds(s, paint, x.toInt(), y)

        paint.color = textColor
        canvas.drawText(text, start, end, bounds.left + innerPadding, y.toFloat(), paint)
    }

    private fun getBounds(s: String, paint: Paint, x: Int, y: Int): Rect {
        val textBounds = Rect()
        paint.getTextBounds(s, 0, s.length, textBounds)

        val startX = textBounds.left + outerPadding
        val endX = startX + width + 2 * innerPadding + outerPadding
        val topY = textBounds.top + y - innerPadding
        val bottomY = topY + textBounds.height() + 2 * innerPadding

        bounds = Rect(
            startX.toInt(),
            topY.toInt(),
            endX.toInt(),
            bottomY.toInt()
        )
        return bounds
    }
}