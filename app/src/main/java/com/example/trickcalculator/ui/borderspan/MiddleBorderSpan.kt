package com.example.trickcalculator.ui.borderspan

import android.graphics.Canvas
import android.graphics.Paint
import com.example.trickcalculator.ext.drawLine

class MiddleBorderSpan(private val textColor: Int, private val parentWidth: Int) : BorderSpan(textColor, parentWidth) {
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
        super.draw(canvas, text, start, end, x, top, y, bottom, paint)

        canvas.drawLine(bounds.left, bounds.top, bounds.left, bounds.bottom, border)
        canvas.drawLine(bounds.right, bounds.top, bounds.right, bounds.bottom, border)
    }
}
