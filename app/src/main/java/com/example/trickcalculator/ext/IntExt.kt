package com.example.trickcalculator.ext

import kotlin.math.pow

fun Int.pow(exp: Int): Int {
    val result: Double = toDouble().pow(exp)
    return result.toInt()
}
