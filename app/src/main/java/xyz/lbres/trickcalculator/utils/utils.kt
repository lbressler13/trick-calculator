package xyz.lbres.trickcalculator.utils

import xyz.lbres.exactnumbers.exactfraction.ExactFraction

/**
 * Determine if a string can be parsed to a number.
 * Number is defined as a ExactFraction.
 *
 * @param value [String]: value to check
 * @return true if value can be parsed to a ExactFraction, false otherwise
 */
fun isNumber(value: String): Boolean {
    return try {
        if (value.count { it == '-' } > 1 || value.count { it == '.' } > 1) {
            false
        } else {
            ExactFraction(value)
            true
        }
    } catch (e: Exception) {
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
