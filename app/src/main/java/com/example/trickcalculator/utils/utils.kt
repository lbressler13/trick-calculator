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
 * Get positive greatest common divisor of 2 numbers using Euclidean algorithm
 */
fun getGCD(val1: BigInteger, val2: BigInteger): BigInteger {
    val aval1 = val1.abs()
    val aval2 = val2.abs()

    when {
        aval1.isZero() && aval2.isZero() -> return BigInteger.ONE
        aval1.isZero() -> return aval2
        aval2.isZero() -> return aval1
        aval1 == aval2 -> return aval1
    }

    var sum = max(aval1, aval2)
    var value = min(aval1, aval2)
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

// guaranteed returns a positive value
fun getListGCD(values: List<BigInteger>): BigInteger {
    when {
        values.isEmpty() -> return BigInteger.ONE
        values.size == 1 && values[0].isZero() -> return BigInteger.ONE
        values.size == 1 -> return values[0].abs()
        values.size == 2 -> return getGCD(values[0], values[1])
    }

    var current: BigInteger = values[0]
    for (value in values) {
        current = getGCD(value, current)
        if (current == BigInteger.ONE) {
            return BigInteger.ONE
        }
    }

    return current
}
