package com.example.trickcalculator.bigfraction

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs

// A custom number class inspired by BigDecimal, with better handling of irrational fractions

fun abs(bf: BigFraction): BigFraction = bf.absoluteValue()

class BigFraction private constructor() : Number() {
    var numerator: Long = 0
    var denominator: Long = 1

    constructor (numerator: Long) : this() {
        this.numerator = numerator
        this.denominator = 1
    }

    constructor (numerator: Long, denominator: Long) : this() {
        if (denominator == 0L) {
            throw ArithmeticException("divide by zero")
        }

        this.numerator = numerator
        this.denominator = denominator
        simplify()
    }

    constructor (numerator: Int) : this(numerator.toLong())
    constructor (numerator: Int, denominator: Int) : this(numerator.toLong(), denominator.toLong())

    // UNARY OPERATORS
    operator fun unaryMinus(): BigFraction = BigFraction(numerator * -1, denominator)
    operator fun unaryPlus(): BigFraction = BigFraction(numerator, denominator)
    operator fun not(): Boolean = isZero()

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

    operator fun plus(other: Long): BigFraction = plus(BigFraction(other))
    operator fun plus(other: Int): BigFraction = plus(BigFraction(other))

    operator fun minus(other: BigFraction): BigFraction = plus(-other)
    operator fun minus(other: Long): BigFraction = plus(-other)
    operator fun minus(other: Int): BigFraction = plus(-other)

    operator fun times(other: BigFraction): BigFraction {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun times(other: Long): BigFraction = times(BigFraction(other))
    operator fun times(other: Int): BigFraction = times(BigFraction(other))

    operator fun div(other: BigFraction): BigFraction = times(other.inverse())

    operator fun div(other: Long): BigFraction = div(BigFraction(other))
    operator fun div(other: Int): BigFraction = div(BigFraction(other))

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is BigFraction) {
            return false
        }

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
        if (numerator == 0L) {
            throw ArithmeticException("divide by zero")
        }

        return BigFraction(denominator, numerator)
    }

    fun absoluteValue(): BigFraction = BigFraction(abs(numerator), denominator)

    fun isNegative(): Boolean = numerator < 0

    fun isZero(): Boolean = numerator == 0L

    fun simplify() {
        if (numerator == 0L) {
            denominator = 1
        }

        if (denominator != 1L && numerator % denominator == 0L) {
            numerator /= denominator
            denominator = 1
        } else if (denominator != 1L && denominator % numerator == 0L) {
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
        if (denominator == 1L) {
            return numerator.toString()
        }

        val decimal = numerator.toDouble() / denominator.toDouble()

        val text = "%.${digits}f".format(decimal)
        return text.trimEnd('0')
    }

    fun toFractionString(): String = if (denominator == 1L) {
        numerator.toString()
    } else {
        "$numerator/$denominator"
    }

    fun toPairString(): String = "($numerator, $denominator)"
    fun toBFString(): String = "BF[$numerator $denominator]"

    override fun toString(): String = toDecimalString()

    override fun hashCode(): Int = toPair().hashCode()

    // CASTING
    fun toPair(): Pair<Long, Long> = Pair(numerator, denominator)

    override fun toByte(): Byte = toLong().toByte()
    override fun toChar(): Char = toInt().toChar()
    override fun toShort(): Short = toLong().toShort()
    override fun toInt(): Int = toLong().toInt()
    override fun toLong(): Long = numerator / denominator

    override fun toDouble(): Double = numerator.toDouble() / denominator
    override fun toFloat(): Float = numerator.toFloat() / denominator

    fun toBigDecimal(precision: Int = 20): BigDecimal {
        val mc = MathContext(precision, RoundingMode.HALF_UP)
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