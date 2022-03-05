package com.example.trickcalculator.bigfraction

import com.example.trickcalculator.ext.length
import com.example.trickcalculator.ext.pow
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.ext.toBI
import java.math.BigDecimal
import java.math.BigInteger

fun parseDecimal(unparsed: String): BigFraction {
    var currentState: String = unparsed.trim()

    val isNegative = unparsed.startsWith("-")
    val timesNeg = if (isNegative) -1 else 1
    if (isNegative) {
        currentState = currentState.substring(1)
    }

    val decimalIndex: Int = currentState.indexOf('.')

    return when (decimalIndex) {
        -1 -> {
            val numerator = BigInteger(currentState)
            BigFraction(numerator * timesNeg.toBI())
        }
        0 -> {
            currentState = currentState.substring(1)
            val numerator = BigInteger(currentState)

            if (numerator == 0.toBI()) {
                return BigFraction(0)
            }
            val denominator = (10).pow(currentState.length)

            BigFraction(numerator * timesNeg.toBI(), denominator.toBI())
        }
        else -> {
            val wholeString = currentState.substringTo(decimalIndex)
            val decimalString = currentState.substring(decimalIndex + 1)
            val whole = Integer.parseInt(wholeString)
            val decimal = Integer.parseInt(decimalString)

            if (decimal == 0) {
                return BigFraction(whole * timesNeg) // also covers the case where number is 0
            }

            val denominator = (10).pow(decimalString.length)
            val numerator = whole * denominator + decimal

            BigFraction(numerator * timesNeg, denominator)
        }
    }
}

fun parseBFString(unparsed: String): BigFraction {
    val numbers = unparsed.substring(3, unparsed.lastIndex)
    val split = numbers.split(' ')
    val numString = split[0].trim()
    val denomString = split[1].trim()
    val numerator = BigInteger(numString)
    val denominator = BigInteger(denomString)
    return BigFraction(numerator, denominator)
}

fun checkIsBFString(s: String): Boolean {
    return try {
        val startEnd = s.trim().startsWith("BF[") && s.trim().endsWith("]")
        val split = s.substring(3, s.lastIndex).split(" ")
        BigInteger(split[0])
        BigInteger(split[1])
        startEnd
    } catch (e: Exception) {
        false
    }
}
