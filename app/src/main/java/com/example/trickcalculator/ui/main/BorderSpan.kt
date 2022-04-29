package com.example.trickcalculator.ui.main

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.ReplacementSpan
import android.util.Log

// TODO handle multiline box

// adapted from https://stackoverflow.com/questions/16026577/border-in-clickable-object-in-spannablestring
class BorderSpan(private val textColor: Int, private val parentWidth: Int) : ReplacementSpan() {
    private val border: Paint = Paint()

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
        val s = text!!.substring(start, end)
        if (s != currentText) {
            setNewText(s, paint)
        }

        return currentWidth + 2 * innerPadding.toInt() + outerPadding.toInt()
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

        currentText = s
        currentRows = rows
        currentWidth = maxWidth + 2 * innerPadding.toInt()
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

    companion object {
        private var currentText = ""
        private var currentRows: List<String> = listOf()
        private var currentWidth = -1
    }
}