package com.example.trickcalculator.ui.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import android.view.MotionEvent
import android.R

import android.content.res.TypedArray
import java.lang.reflect.Method


class FirstBorderTextView(context: Context, attr: AttributeSet) : AppCompatTextView(context, attr) {
    private val borderPaint: Paint = Paint()
    private val textPaint = Paint(paint)

    private val lineSize = 14
    private val lineHeight = textPaint.fontMetrics.bottom - textPaint.fontMetrics.top

    private var numLines: Int? = null
    var firstTerm: String? = null

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = textColors.defaultColor
        borderPaint.strokeWidth = 10f

        // setWillNotDraw(false)
        isVerticalScrollBarEnabled = true
    }

    override fun computeVerticalScrollExtent(): Int = lineHeight.toInt() * 4
    override fun computeVerticalScrollOffset(): Int = lineHeight.toInt()
    override fun computeVerticalScrollRange(): Int = lineHeight.toInt() * 4

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        awakenScrollBars()
        invalidate()
        return true
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        Log.e("vert", Pair(oldVert, vert).toString())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = measuredWidth
        val minH: Int = numLines?.times(lineHeight)?.toInt() ?: 0
        val h: Int = View.resolveSizeAndState(minH, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }

    override fun draw(canvas: Canvas?) {
        textSize = 0f
        // setTextColor(Color.TRANSPARENT)
        super.draw(canvas)

        canvas?.apply {
            val lines = text.chunked(lineSize)
            numLines = lines.size

            var currentTop = top.toFloat()
            for (line in lines) {
                drawText(line, left.toFloat(), currentTop, textPaint)
                currentTop += lineHeight
            }

            // drawText(text.toString(), left.toFloat(), top.toFloat(), textPaint)
        }
    }
}