package com.example.trickcalculator.utils

import exactfraction.ExactFraction

/**
 * Determine if a string can be parsed to an Int
 *
 * @param value [String]: string to check
 * @return true if value can be parsed to an Int, false otherwise
 */
fun isInt(value: String): Boolean {
    return try {
        Integer.parseInt(value)
        true
    } catch (e: Exception) {
        false
    }
}

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
 * Determine if a string is in the process of being built to a decimal number.
 * Partial decimal is defined as a string which may start with a negative sign but is otherwise made of digits and decimal points.
 * Not necessarily a value that can be parsed to a number.
 *
 * @param value [String]: string to check
 * @return true if value is a partial decimal as defined above, false otherwise
 */
fun isPartialDecimal(value: String): Boolean {
    // remove negative sign if it exists
    val positiveString = if (value.startsWith('-') && value.length > 1) {
        value.substring(1)
    } else {
        value
    }

    return positiveString.isNotEmpty() && positiveString.all { it.isDigit() || it == '.' }
}
