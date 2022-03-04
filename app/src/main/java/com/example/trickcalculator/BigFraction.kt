package com.example.trickcalculator

import com.example.trickcalculator.ext.substringTo
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

// TODO implement Number
class BigFraction(var numerator: Int) {
    var denominator: Int = 1

    constructor (numerator: Int, denominator: Int) : this(numerator) {
        if (denominator == 0) {
            throw ArithmeticException("divide by zero")
        }

        this.denominator = denominator
    }

    init {
        simplify()
    }

    // UNARY OPERATORS
    operator fun unaryMinus(): BigFraction {
        val newNumerator = -abs(numerator)
        val newDenominator = abs(denominator)
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun unaryPlus(): BigFraction {
        val newNumerator = -abs(numerator)
        val newDenominator = abs(denominator)
        return BigFraction(newNumerator, newDenominator)
    }

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
        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator

        val newNumerator = scaled1 + scaled2
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun minus(other: BigFraction): BigFraction {
        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator

        val newNumerator = scaled1 - scaled2
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun times(other: BigFraction): BigFraction {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun div(other: BigFraction): BigFraction {
        // reverse numerator & denominator for division
        val newNumerator = numerator * other.denominator
        val newDenominator = denominator * other.numerator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun plusAssign(other: BigFraction) {
        val result = plus(other)
        numerator = result.numerator
        denominator = result.denominator
    }

    operator fun minusAssign(other: BigFraction) {
        val result =  minus(other)
        numerator = result.numerator
        denominator = result.denominator
    }

    operator fun timesAssign(other: BigFraction) {
        val result =  times(other)
        numerator = result.numerator
        denominator = result.denominator
    }

    operator fun divAssign(other: BigFraction) {
        val result =  div(other)
        numerator = result.numerator
        denominator = result.denominator
    }

    override operator fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is BigFraction) {
            return false
        }

        // TODO handling for number types

        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator
        return scaled1 == scaled2
    }

    operator fun compareTo(other: BigFraction): Int {
        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator

        return scaled1 - scaled2
    }

    private fun simplify() {
        setSign()
        // TODO simplify fraction
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
        return "$numerator/$denominator"
    }

    override fun toString(): String {
        return toDecimalString()
    }

    override fun hashCode(): Int {
        return Pair(numerator, denominator).hashCode()
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
    }
}