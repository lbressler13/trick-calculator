package com.example.trickcalculator

import android.util.Log
import com.example.trickcalculator.ext.length
import com.example.trickcalculator.ext.pow
import com.example.trickcalculator.ext.substringTo
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

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

        fun toDecimalString(): String {
            val numDecimal = BigDecimal(numerator)
            val denomDecimal = BigDecimal(denominator)

            val decimal = numDecimal.divide(denomDecimal, 5, RoundingMode.HALF_UP)
            var text = decimal.toString()
            // Log.e("dec string", text)

            if (text.indexOf('.') != -1) {
                val stripped = text.trimEnd('0')

                text = when {
                    stripped == "." -> "0"
                    stripped.last() == '.' -> stripped.substringTo(stripped.lastIndex)
                    else -> stripped
                }
            }

            return text
        }

        fun toFractionString(): String {
            if (denominator == 1) {
                return numerator.toString()
            }

            return "$numerator/$denominator"
        }

        fun toPairString(): String {
            return "($numerator, $denominator)"
        }

        override fun toString(): String {
            return toDecimalString()
        }

        override fun hashCode(): Int {
            return Pair(numerator, denominator).hashCode()
        }

        // CASTING
        fun toPair(): Pair<Int, Int> {
            return Pair(numerator, denominator)
        }


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

        fun parse(unparsed: String): BigFraction {
            var currentState: String = unparsed.trim()

            val isNegative = unparsed.startsWith("-")
            if (isNegative) {
                currentState = currentState.substring(1)
            }

            val decimalIndex: Int = currentState.indexOf('.')

            return when (decimalIndex) {
                -1 -> {
                    val numerator = Integer.parseInt(currentState)
                    BigFraction(numerator)
                }
                0 -> {
                    currentState = currentState.substring(1)
                    val numerator = Integer.parseInt(currentState)

                    if (numerator == 0) {
                        return BigFraction(0)
                    }
                    val denominator = (10).pow(numerator.length())

                    BigFraction(numerator, denominator)
                }
                else -> {
                    // Log.e("full string", unparsed)
                    val wholeString = currentState.substringTo(decimalIndex)
                    val decimalString = currentState.substring(decimalIndex + 1)
                    val whole = Integer.parseInt(wholeString)
                    val decimal = Integer.parseInt(decimalString)
                    // Log.e("pieces", Pair(whole, decimal).toString())

                    if (decimal == 0) {
                        return BigFraction(whole) // also covers the case where number is 0
                    }

                    val denominator = (10).pow(decimal.length())
                    val numerator = whole * denominator + decimal

                    BigFraction(numerator, denominator)
                }
            }
        }
    }
}