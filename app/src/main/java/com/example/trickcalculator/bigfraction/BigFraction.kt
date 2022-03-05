package com.example.trickcalculator.bigfraction

import com.example.trickcalculator.ext.toBI
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs

// A custom number class inspired by BigDecimal
// More precise decimal tracking, and ability to handle irrational numbers without specifying precision


fun abs(bf: BigFraction): BigFraction = bf.absoluteValue()

class BigFraction private constructor() : Number() {
    var numerator: BigInteger = 0.toBI()
    var denominator: BigInteger = 1.toBI()

    constructor (numerator: BigInteger) : this() {
        this.numerator = numerator
        this.denominator = 1.toBI()
    }

    constructor (numerator: BigInteger, denominator: BigInteger) : this() {
        if (denominator == 0.toBI()) {
            throw ArithmeticException("divide by zero")
        }

        this.numerator = numerator
        this.denominator = denominator
        simplify()
    }

    constructor (numerator: Int) : this(numerator.toBI())
    constructor (numerator: Int, denominator: Int) : this(numerator.toBI(), denominator.toBI())
    constructor (numerator: Long) : this(numerator.toBI())
    constructor (numerator: Long, denominator: Int) : this(numerator.toBI(), denominator.toBI())

    // UNARY OPERATORS
    operator fun unaryMinus(): BigFraction = BigFraction(numerator * (-1).toBI(), denominator)
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
        if (numerator == 0.toBI()) {
            throw ArithmeticException("divide by zero")
        }

        return BigFraction(denominator, numerator)
    }

    fun absoluteValue(): BigFraction = BigFraction(numerator.abs(), denominator)

    fun isNegative(): Boolean = numerator < 0.toBI()

    fun isZero(): Boolean = numerator == 0.toBI()

    fun simplify() {
        if (numerator == 0.toBI()) {
            denominator = 1.toBI()
        }

        if (denominator != 1.toBI() && numerator % denominator == 0.toBI()) {
            numerator /= denominator
            denominator = 1.toBI()
        } else if (denominator != 1.toBI() && denominator % numerator == 0.toBI()) {
            denominator /= numerator
            numerator = 1.toBI()
        }
        // TODO use GCF

        setSign()
    }

    // move negatives to numerator
    private fun setSign() {
        val numNegative = numerator < 0.toBI()
        val denomNegative = denominator < 0.toBI()

        when {
            numNegative && denomNegative -> {
                numerator = numerator.abs()
                denominator = denominator.abs()
            }
            !numNegative && denomNegative -> {
                numerator *= (-1).toBI()
                denominator = denominator.abs()
            }
        }
    }

    // STRING METHODS

    fun toDecimalString(digits: Int = 8): String {
        if (denominator == 1.toBI()) {
            return numerator.toString()
        }

        val decimal = numerator.toDouble() / denominator.toDouble()

        val text = "%.${digits}f".format(decimal)
        return text.trimEnd('0')
    }

    fun toFractionString(): String = if (denominator == 1.toBI()) {
        numerator.toString()
    } else {
        "$numerator/$denominator"
    }

    fun toPairString(): String = "($numerator, $denominator)"
    fun toBFString(): String = "BF[$numerator $denominator]"

    override fun toString(): String = toDecimalString()

    override fun hashCode(): Int = toPair().hashCode()

    // CASTING
    fun toPair(): Pair<BigInteger, BigInteger> = Pair(numerator, denominator)

    override fun toByte(): Byte = (numerator / denominator).toByte()
    override fun toChar(): Char = (numerator / denominator).toChar()
    override fun toShort(): Short = (numerator / denominator).toShort()
    override fun toInt(): Int = (numerator / denominator).toInt()
    override fun toLong(): Long = (numerator / denominator).toLong()

    override fun toDouble(): Double = (numerator.toBigDecimal() / denominator.toBigDecimal()).toDouble()
    override fun toFloat(): Float = (numerator.toBigDecimal() / denominator.toBigDecimal()).toFloat()

    fun toBigDecimal(precision: Int = 20): BigDecimal {
        val mc = MathContext(precision, RoundingMode.HALF_UP)
        return numerator.toBigDecimal().divide(denominator.toBigDecimal(), mc)
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
