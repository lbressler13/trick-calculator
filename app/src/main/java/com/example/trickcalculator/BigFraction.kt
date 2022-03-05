package com.example.trickcalculator

import com.example.trickcalculator.ext.length
import com.example.trickcalculator.ext.pow
import com.example.trickcalculator.ext.substringTo
import kotlin.math.abs

// A custom number class inspired by BigDecimal, with better handling of irrational fractions

// TODO implement Number
class BigFraction private constructor() {
    var numerator: Int = 0
    var denominator: Int = 1

    constructor (numerator: Int) : this() {
        this.numerator = numerator
        this.denominator = 1
    }

    constructor (numerator: Int, denominator: Int) : this() {
        if (denominator == 0) {
            throw ArithmeticException("divide by zero")
        }

        this.numerator = numerator
        this.denominator = denominator
        simplify()
    }

    // UNARY OPERATORS
    operator fun unaryMinus(): BigFraction = BigFraction(numerator * -1, denominator)
    operator fun unaryPlus(): BigFraction = BigFraction(numerator, denominator)
    operator fun not(): Boolean = numerator == 0

    operator fun inc(): BigFraction {
        val newNumerator = numerator + denominator
        return BigFraction(newNumerator, denominator)
    }

    operator fun dec(): BigFraction {
        val newNumerator = numerator - denominator
        return BigFraction(newNumerator, denominator)
    }

    // BINARY OPERATORS
    operator fun plus(other: BigFraction): BigFraction {
        if (denominator == other.denominator) {
            val newNumerator = numerator + other.numerator
            return BigFraction(newNumerator, denominator)
        }

        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator

        val newNumerator = scaled1 + scaled2
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun minus(other: BigFraction): BigFraction = plus(-other)

    operator fun times(other: BigFraction): BigFraction {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun div(other: BigFraction): BigFraction = times(other.inverse())

    override operator fun equals(other: Any?): Boolean {
        if (other == null || other !is BigFraction) {
            return false
        }

        // TODO handling for number types

        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator
        return scaled1 == scaled2
    }

    operator fun compareTo(other: BigFraction): Int {
        val difference = minus(other)
        return when {
            difference.isNegative() -> -1
            difference.isZero() -> 0
            else -> 1
        }
    }

    fun inverse(): BigFraction {
        if (numerator == 0) {
            throw ArithmeticException("divide by zero")
        }

        return BigFraction(denominator, numerator)
    }

    fun isNegative(): Boolean = numerator < 0

    fun isZero(): Boolean = numerator == 0

    fun simplify() {
        if (numerator == 0) {
            denominator = 1
        }

        if (denominator != 1 && numerator % denominator == 0) {
            numerator /= denominator
            denominator = 1
        } else if (denominator != 1 && denominator % numerator == 0) {
            denominator /= numerator
            numerator = 1
        }
        // TODO use GCF

        setSign()
    }

    // move negatives to numerator
    private fun setSign() {
        val numNegative = numerator < 0
        val denomNegative = denominator < 0

        when {
            numNegative && denomNegative -> {
                numerator = abs(numerator)
                denominator = abs(denominator)
            }
            !numNegative && denomNegative -> {
                numerator *= -1
                denominator = abs(denominator)
            }
        }
    }

    fun toDecimalString(digits: Int = 8): String {
        if (denominator == 1) {
            return numerator.toString()
        }

        val decimal = numerator.toDouble() / denominator.toDouble()

        val text = "%.${digits}f".format(decimal)
        return text.trimEnd('0')
    }

    fun toFractionString(): String = if (denominator == 1) {
        numerator.toString()
    } else {
        "$numerator/$denominator"
    }

    fun toPairString(): String = "($numerator, $denominator)"

    fun toBFString(): String = "BF[$numerator $denominator]"

    override fun toString(): String = toDecimalString()

    override fun hashCode(): Int = Pair(numerator, denominator).hashCode()

    // CASTING
    fun toPair(): Pair<Int, Int> = Pair(numerator, denominator)

    companion object {
        val ZERO = BigFraction(0)
        val ONE = BigFraction(1)
        val TWO = BigFraction(2)
        val THREE = BigFraction(3)
        val FOUR = BigFraction(4)
        val FIVE = BigFraction(5)
        val SIX = BigFraction(6)
        val SEVEN = BigFraction(7)
        val EIGHT = BigFraction(8)
        val NINE = BigFraction(9)

        private fun parseDecimal(unparsed: String): BigFraction {
            var currentState: String = unparsed.trim()

            val isNegative = unparsed.startsWith("-")
            val timesNeg = if (isNegative) -1 else 1
            if (isNegative) {
                currentState = currentState.substring(1)
            }

            val decimalIndex: Int = currentState.indexOf('.')

            return when (decimalIndex) {
                -1 -> {
                    val numerator = Integer.parseInt(currentState)
                    BigFraction(numerator * timesNeg)
                }
                0 -> {
                    currentState = currentState.substring(1)
                    val numerator = Integer.parseInt(currentState)

                    if (numerator == 0) {
                        return BigFraction(0)
                    }
                    val denominator = (10).pow(numerator.length())

                    BigFraction(numerator * timesNeg, denominator)
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

        private fun parseBFString(unparsed: String): BigFraction {
            val numbers = unparsed.substring(3, unparsed.lastIndex)
            val split = numbers.split(' ')
            val numString = split[0].trim()
            val denomString = split[1].trim()
            val numerator = Integer.parseInt(numString)
            val denominator = Integer.parseInt(denomString)
            return BigFraction(numerator, denominator)
        }

        fun parse(s: String): BigFraction {
            if (s.startsWith("BF")) {
                return parseBFString(s)
            }

            return parseDecimal(s)
        }

        fun isBFString(s: String): Boolean {
            return try {
                val startEnd = s.trim().startsWith("BF[") && s.trim().endsWith("]")
                val split = s.substring(3, s.lastIndex).split(" ")
                Integer.parseInt(split[0])
                Integer.parseInt(split[1])
                startEnd
            } catch (e: Exception) {
                false
            }
        }
    }
}