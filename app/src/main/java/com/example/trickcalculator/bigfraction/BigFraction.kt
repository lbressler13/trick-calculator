package com.example.trickcalculator.bigfraction

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

// A custom number class inspired by BigDecimal
// More precise decimal tracking, and ability to handle irrational numbers without specifying precision


fun abs(bf: BigFraction): BigFraction = bf.absoluteValue()

class BigFraction private constructor() : Number() {
    var numerator: BigInteger = BigInteger.ZERO
    var denominator: BigInteger = BigInteger.ONE

    constructor (numerator: BigInteger) : this() {
        this.numerator = numerator
        this.denominator = BigInteger.ONE
    }

    constructor (numerator: BigInteger, denominator: BigInteger) : this() {
        if (denominator.isZero()) {
            throw ArithmeticException("divide by zero")
        }

        this.numerator = numerator
        this.denominator = denominator
        simplify()
    }

    constructor (s: String) : this() {
        val result = parse(s)
        numerator = result.numerator
        denominator = result.denominator
    }

    constructor (numerator: Int) : this(numerator.toBI())
    constructor (numerator: Int, denominator: Int) : this(numerator.toBI(), denominator.toBI())
    constructor (numerator: Long) : this(numerator.toBI())
    constructor (numerator: Long, denominator: Long) : this(numerator.toBI(), denominator.toBI())
    constructor (numerator: Int, denominator: Long) : this(numerator.toBI(), denominator.toBI())
    constructor (numerator: Long, denominator: Int) : this(numerator.toBI(), denominator.toBI())
    constructor (numerator: BigInteger, denominator: Int) : this(numerator, denominator.toBI())
    constructor (numerator: Int, denominator: BigInteger) : this(numerator.toBI(), denominator)
    constructor (numerator: BigInteger, denominator: Long) : this(numerator, denominator.toBI())
    constructor (numerator: Long, denominator: BigInteger) : this(numerator.toBI(), denominator)

    // UNARY OPERATORS
    operator fun unaryMinus(): BigFraction = BigFraction(-numerator, denominator)
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

    operator fun plus(other: BigInteger): BigFraction = plus(other.toBF())
    operator fun plus(other: Long): BigFraction = plus(other.toBF())
    operator fun plus(other: Int): BigFraction = plus(other.toBF())

    operator fun minus(other: BigFraction): BigFraction = plus(-other)
    operator fun minus(other: BigInteger): BigFraction = plus(-other)
    operator fun minus(other: Long): BigFraction = plus(-other)
    operator fun minus(other: Int): BigFraction = plus(-other)

    operator fun times(other: BigFraction): BigFraction {
        val newNumerator = numerator * other.numerator
        val newDenominator = denominator * other.denominator
        return BigFraction(newNumerator, newDenominator)
    }

    operator fun times(other: BigInteger): BigFraction = times(other.toBF())
    operator fun times(other: Long): BigFraction = times(other.toBF())
    operator fun times(other: Int): BigFraction = times(other.toBF())

    operator fun div(other: BigFraction): BigFraction = times(other.inverse())

    operator fun div(other: BigInteger): BigFraction = div(other.toBF())
    operator fun div(other: Long): BigFraction = div(other.toBF())
    operator fun div(other: Int): BigFraction = div(other.toBF())

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is BigFraction) {
            return false
        }

        val scaled1 = numerator * other.denominator
        val scaled2 = other.numerator * denominator
        return scaled1 == scaled2
    }

    fun eq(other: Int): Boolean = numerator.eq(other) && denominator.eq(1)
    fun eq(other: Long): Boolean = numerator.eq(other) && denominator.eq(1)
    fun eq(other: BigInteger): Boolean = numerator == other && denominator.eq(1)

    operator fun compareTo(other: BigFraction): Int {
        val difference = minus(other)
        return when {
            difference.isNegative() -> -1
            difference.isZero() -> 0
            else -> 1
        }
    }

    operator fun compareTo(other: Int): Int = compareTo(other.toBF())
    operator fun compareTo(other: Long): Int = compareTo(other.toBF())
    operator fun compareTo(other: BigInteger): Int = compareTo(other.toBF())

    fun inverse(): BigFraction {
        if (numerator.eq(0)) {
            throw ArithmeticException("divide by zero")
        }

        return BigFraction(denominator, numerator)
    }

    fun absoluteValue(): BigFraction = BigFraction(numerator.abs(), denominator)

    fun isNegative(): Boolean = numerator < BigInteger.ZERO
    fun isZero(): Boolean = numerator.eq(0)

    fun simplify() {
        if (numerator.eq(0)) {
            denominator = BigInteger.ONE
        }

        if (!denominator.eq(1) && (numerator % denominator).eq(0)) {
            numerator /= denominator
            denominator = BigInteger.ONE
        } else if (!denominator.eq(1) && (denominator % numerator).eq(0)) {
            denominator /= numerator
            numerator = BigInteger.ONE
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
                numerator = numerator.abs()
                denominator = denominator.abs()
            }
            !numNegative && denomNegative -> {
                numerator *= -1
                denominator = denominator.abs()
            }
        }
    }

    // STRING METHODS

    fun toDecimalString(digits: Int = 8): String {
        if (denominator.eq(1)) {
            return numerator.toString()
        }

        val decimal = numerator.toDouble() / denominator.toDouble()

        val text = "%.${digits}f".format(decimal)
        return text.trimEnd('0')
    }

    fun toFractionString(): String = if (denominator.eq(1)) {
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

    fun toBigInteger(): BigInteger = numerator / denominator
    fun toBI(): BigInteger = toBigInteger()
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
