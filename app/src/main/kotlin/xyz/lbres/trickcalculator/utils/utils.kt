package xyz.lbres.trickcalculator.utils

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.string.ext.countElement

/**
 * Determine if a string can be parsed to a number.
 * Number is defined as an [ExactFraction].
 *
 * @param value [String]: value to check
 * @return `true` if value can be parsed to a ExactFraction, `false` otherwise
 */
fun isNumber(value: String): Boolean {
    if (value.countElement('-') > 1 || value.countElement('.') > 1) {
        return false
    }

    return try {
        ExactFraction(value)
        true
    } catch (_: Exception) {
        false
    }
}

/**
 * Determine if a string consists of a single character that is part of a number, meaning a digit or decimal
 *
 * @param value [String]: value to check
 * @return true if value is a single digit or decimal, false otherwise
 */
fun isNumberChar(value: String): Boolean = value.length == 1 && (value == "." || value[0].isDigit())
