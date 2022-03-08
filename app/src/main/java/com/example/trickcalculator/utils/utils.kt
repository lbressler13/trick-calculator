package com.example.trickcalculator.utils

import com.example.trickcalculator.bigfraction.BigFraction

fun isInt(value: String): Boolean {
    return try {
        Integer.parseInt(value)
        true
    } catch (e: Exception) {
        false
    }
}

fun isNumber(value: String): Boolean {
    return try {
        if (value.count { it == '-' } > 1 || value.count { it == '.' } > 1) {
            false
        } else {
            BigFraction(value)
            true
        }
    } catch (e: Exception) {
        false
    }
}

fun isPartialDecimal(value: String): Boolean {
    // remove negative sign if it exists
    val positiveString = if (value.startsWith('-') && value.length > 1) {
        value.substring(1)
    } else {
        value
    }

    return positiveString.isNotEmpty() && positiveString.all { it.isDigit() || it == '.' }
}
