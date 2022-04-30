package com.example.trickcalculator.ui.borderspan

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan

// TODO handle multiline box

// adapted from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
open class BorderSpan protected constructor(private val textColor: Int, private val parentWidth: Int) : ReplacementSpan() {
    protected val border: Paint = Paint()
    protected var bounds = Rect()

    protected val innerPadding = 12f
    protected val outerPadding = 0f // 12f

    protected var width = 0

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
        val s = text!!.substring(start, end)
        val bounds = Rect()
        paint.getTextBounds(s, 0, s.length, bounds)
        width = bounds.width()
        return (width + 4 * innerPadding + outerPadding).toInt()
//        val s = text!!.substring(start, end)
//        if (s != currentText) {
//            setNewText(s, paint)
//        }
//
//        return currentWidth + 2 * innerPadding.toInt() + outerPadding.toInt()
    }

    private fun setNewText(s: String, paint: Paint) {
        val rows = mutableListOf<String>()
        var currentRow = ""
        for (digit in s) {
            val currentBounds = Rect()
            paint.getTextBounds(currentRow + digit, 0, currentRow.length + 1, currentBounds)

            if (currentBounds.width() + 2 * innerPadding < parentWidth) {
                currentRow += digit
            } else {
                rows.add(currentRow)
                currentRow = digit.toString()
            }
        }

        if (currentRow.isNotEmpty()) {
            rows.add(currentRow)
        }

        val maxWidth: Int = rows.map {
            val bounds = Rect()
            paint.getTextBounds(it, 0, it.length, bounds)
            bounds.width()
        }.maxOrNull() ?: 0

        val lastWidth = if (rows.isEmpty()) 0
        else {
            val bounds = Rect()
            paint.getTextBounds(rows.last(), 0, rows.last().length, bounds)
            bounds.width()
        }

        currentText = s
        currentRows = rows
        currentWidth = maxWidth + 2 * innerPadding.toInt()
        // currentWidth = lastWidth + 2 * innerPadding.toInt()
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

    fun oldDraw(
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
        if (s != currentText) {
            setNewText(s, paint)
        }

        val bounds = Rect()
        paint.getTextBounds(s, 0, s.length, bounds)
        val startX = bounds.left + outerPadding

        var currentY = y.toFloat()
        for (row in currentRows) {
            canvas.drawText(row, 0, row.length, startX, currentY, paint)
            currentY += bounds.height()
        }

        val boxHeight = currentRows.size * bounds.height()

        paint.color = textColor
        canvas.drawText(currentText, start, end, startX, y.toFloat(), paint)

        val topY = bounds.top + y - innerPadding
        // val bottomY = bounds.bottom + y + innerPadding
        val bottomY = topY + boxHeight + innerPadding
        val endX = startX + currentWidth - innerPadding
        canvas.drawRect(startX - innerPadding, topY, endX, bottomY, border)
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

    companion object {
        private var currentText = ""
        private var currentRows: List<String> = listOf()
        private var currentWidth = -1
    }
}