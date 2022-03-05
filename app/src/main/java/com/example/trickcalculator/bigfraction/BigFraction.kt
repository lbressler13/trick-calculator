package com.example.trickcalculator.bigfraction

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs

// A custom number class inspired by BigDecimal, with better handling of irrational fractions

fun abs(bf: BigFraction): BigFraction = bf.absoluteValue()

class BigFraction private constructor() : Number() {
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

    operator fun plus(other: Int): BigFraction = plus(BigFraction(other))

    operator fun minus(other: BigFraction): BigFraction = plus(-other)

    operator fun minus(other: Int): BigFraction = plus(-other)

    operator fun times(other: BigFraction): BigFraction {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun times(other: Int): BigFraction = times(BigFraction(other))

    operator fun div(other: BigFraction): BigFraction = times(other.inverse())

    operator fun div(other: Int): BigFraction = div(BigFraction(other))

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null -> return false
            is BigFraction -> {
                val scaled1 = numerator * other.denominator
                val scaled2 = other.numerator * denominator
                scaled1 == scaled2
            }
            is Long -> {
                toLong() == other
            }
            is Int -> {
                toInt() == other
            }
            is Short -> {
                toShort() == other
            }
            is Char -> {
                toChar() == other
            }
            is Byte -> {
                toByte() == other
            }
            is BigDecimal -> {
                val text = other.toString()
                val bf = parseDecimal(text)
                equals(bf)
            }
            else -> false
        }
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

    fun absoluteValue(): BigFraction = BigFraction(abs(numerator), denominator)

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

    // STRING METHODS

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

    override fun hashCode(): Int = toPair().hashCode()

    // CASTING
    fun toPair(): Pair<Int, Int> = Pair(numerator, denominator)

    override fun toByte(): Byte = toInt().toByte()
    override fun toChar(): Char = toInt().toChar()
    override fun toShort(): Short = toInt().toShort()
    override fun toInt(): Int = numerator / denominator
    override fun toLong(): Long = numerator.toLong() / denominator

    override fun toDouble(): Double = numerator.toDouble() / denominator
    override fun toFloat(): Float = numerator.toFloat() / denominator

    fun toBigDecimal(): BigDecimal {
        val mc = MathContext(20, RoundingMode.HALF_UP)
        return BigDecimal(numerator.toDouble() / denominator, mc)
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

        fun parse(s: String): BigFraction {
            if (isBFString(s)) {
                return parseBFString(s)
            }

            return parseDecimal(s)
        }

        fun isBFString(s: String): Boolean = checkIsBFString(s)
    }
}