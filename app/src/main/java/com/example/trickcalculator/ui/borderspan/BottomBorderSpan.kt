package com.example.trickcalculator.ui.borderspan

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

class BottomBorderSpan(private val textColor: Int, private val parentWidth: Int) : BorderSpan(textColor, parentWidth) {
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

        val path = Path()
        path.moveTo(bounds.left.toFloat(), bounds.top.toFloat())
        path.lineTo(bounds.left.toFloat(), bounds.bottom.toFloat())
        path.lineTo(bounds.right.toFloat(), bounds.bottom.toFloat())
        path.lineTo(bounds.right.toFloat(), bounds.top.toFloat())

        val remainingBottom = parentWidth - bounds.width()
        path.lineTo(bounds.right.toFloat() + remainingBottom, bounds.top.toFloat())

        canvas.drawPath(path, borderPaint)
    }
}