package com.example.trickcalculator.ext

import android.graphics.Canvas
import android.graphics.Paint

fun Canvas.drawLine(startX: Int, startY: Int, endX: Int, endY: Int, paint: Paint) =
    drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), paint)