package com.example.trickcalculator.ui.borderspan

import android.graphics.Canvas
import android.graphics.Paint

class SingleBorderSpan(private val textColor: Int, private val parentWidth: Int) : BorderSpan(textColor, parentWidth) {
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
        canvas.drawRect(bounds, borderPaint)

        // val staticLayout = StaticLayout.Builder
        //     .obtain(text!!, start, end, TextPaint(paint), parentWidth)
        //     .build()
        // staticLayout.draw(canvas)
    }
}