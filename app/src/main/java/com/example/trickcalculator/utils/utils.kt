package com.example.trickcalculator.utils

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.isZero
import com.example.trickcalculator.ext.max
import com.example.trickcalculator.ext.min
import java.math.BigInteger
import kotlin.math.max

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

/**
 * Get greatest common divisor of 2 numbers using Euclidean algorithm
 */
fun getGCD(val1: BigInteger, val2: BigInteger): BigInteger {
    if (val1 == BigInteger.ZERO || val2 == BigInteger.ZERO || val1 == val2) {
        return BigInteger.ONE
    }

    if (val1 == val2) {
        return val1
    }

    var sum = max(val1, val2)
    var value = min(val1, val2)
    var finished = false

    while (!finished) {
        val remainder = sum % value

        if (remainder.isZero()) {
            finished = true
        } else {
            sum = value
            value = remainder
        }
    }

    return value
}
