package com.example.trickcalculator.ext

import kotlin.math.absoluteValue
import kotlin.math.pow

fun Int.pow(exp: Int): Int {
    val result: Double = toDouble().pow(exp)
    return result.toInt()
}

fun Int.length(): Int {
    return absoluteValue.toString().length
}
