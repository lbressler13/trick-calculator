package com.example.trickcalculator.exactfraction

import com.example.trickcalculator.ext.substringTo
import java.math.BigInteger

/**
 * Parse a string from standard number format into a ExactFraction.
 * Standard format is a string which may start with "-", but otherwise consists of at least one digit and up to 1 "."
 *
 * @param unparsed [String]: string to parse
 * @return parsed ExactFraction
 * @throws NumberFormatException in case of improperly formatted number string
 */
fun parseDecimal(unparsed: String): ExactFraction {
    var currentState: String = unparsed.trim()

    val isNegative = unparsed.startsWith("-")
    val timesNeg = if (isNegative) -BigInteger.ONE else BigInteger.ONE
    if (isNegative) {
        currentState = currentState.substring(1)
    }

    val decimalIndex: Int = currentState.indexOf('.')

    return when (decimalIndex) {
        -1 -> {
            val numerator = BigInteger(currentState)
            ExactFraction(numerator * timesNeg)
        }
        0 -> {
            currentState = currentState.substring(1)
            val numerator = BigInteger(currentState)

            if (numerator.isZero()) {
                return ExactFraction(0)
            }

            val zeros = "0".repeat(currentState.length)
            val denomString = "1$zeros"
            val denominator = BigInteger(denomString)

            ExactFraction(numerator * timesNeg, denominator)
        }
        else -> {
            val wholeString = currentState.substringTo(decimalIndex)
            val decimalString = currentState.substring(decimalIndex + 1)
            val whole = BigInteger(wholeString)
            val decimal = BigInteger(decimalString)

            if (decimal.isZero()) {
                return ExactFraction(whole * timesNeg) // also covers the case where number is 0
            }

            val zeros = "0".repeat(decimalString.length)
            val denomString = "1$zeros"

            val denominator = BigInteger(denomString)
            val numerator = whole * denominator + decimal

            ExactFraction(numerator * timesNeg, denominator)
        }
    }
}

/**
 * Parse a string from a EF string format into an ExactFraction
 * EF string format is "EF[num denom]"
 *
 * @param unparsed [String]: string to parse
 * @return parsed ExactFraction
 * @throws NumberFormatException in case of improperly formatted number string
 */
fun parseEFString(unparsed: String): ExactFraction {
    if (!checkIsEFString(unparsed)) {
        throw NumberFormatException("Invalid EF string format")
    }

    try {
        val numbers = unparsed.substring(3, unparsed.lastIndex)
        val split = numbers.split(' ')
        val numString = split[0].trim()
        val denomString = split[1].trim()
        val numerator = BigInteger(numString)
        val denominator = BigInteger(denomString)
        return ExactFraction(numerator, denominator)
    } catch (e: Exception) {
        throw NumberFormatException("Invalid EF string format")
    }
}

/**
 * Check if a given string is in the EF string format.
 * EF string format is "EF[num denom]"
 *
 * @param s [String]: string to check
 * @return true if s is in EF string format, false otherwise
 */
fun checkIsEFString(s: String): Boolean {
    return try {
        val startEnd = s.trim().startsWith("EF[") && s.trim().endsWith("]")
        val split = s.substring(3, s.lastIndex).split(" ")
        BigInteger(split[0])
        BigInteger(split[1])
        startEnd && split.size == 2
    } catch (e: Exception) {
        false
    }
}
