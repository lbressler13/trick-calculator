package com.example.trickcalculator.bigfraction

import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

/**
 * Custom number implementation inspired by BigDecimal.
 * Exact representations of rational numbers, without specifying decimal precision.
 */
class BigFraction private constructor() : Number() {
    // These values are re-assigned in all public constructors
    var numerator: BigInteger = BigInteger.ZERO
    var denominator: BigInteger = BigInteger.ONE

    // CONSTRUCTORS

    /**
     * Constructor using only numerator.
     * Creates BigFraction with integer value.
     *
     * @param numerator [BigInteger]: numerator of fraction
     */
    constructor (numerator: BigInteger) : this() {
        this.numerator = numerator
        this.denominator = BigInteger.ONE
    }

    /**
     * Constructor using numerator and denominator.
     * Simplifies fraction on creation
     *
     * @param numerator [BigInteger]: numerator of fraction
     * @param denominator [BigInteger]: denominator of fraction
     * @throws ArithmeticException if denominator is 0
     */
    constructor (numerator: BigInteger, denominator: BigInteger) : this() {
        if (denominator.isZero()) {
            throw ArithmeticException("divide by zero")
        }

        this.numerator = numerator
        this.denominator = denominator
        simplify()
    }

    // TODO test this
    constructor (numerator: BigFraction, denominator: BigFraction) : this() {
        if (denominator.isZero()) {
            throw ArithmeticException("divide by zero")
        }

        val result = numerator / denominator

        this.numerator = result.numerator
        this.denominator = result.denominator
        simplify()
    }

    /**
     * Constructor which parses value from string
     *
     * @param s [String]: string to parse
     * @throws NumberFormatException if s is not in a parsable format
     */
    constructor (s: String) : this() {
        // result was simplified when initialized, no need to re-simplify here
        val result = parse(s)
        numerator = result.numerator
        denominator = result.denominator
    }

    // constructors for combinations of Int, Long, and BigInteger
    constructor (numerator: Int) : this(numerator.toBI())
    constructor (numerator: Long) : this(numerator.toBI())
    constructor (numerator: Int, denominator: Int) : this(numerator.toBI(), denominator.toBI())
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

    // UNARY NON-OPERATORS

    fun inverse(): BigFraction {
        if (numerator.eq(0)) {
            throw ArithmeticException("divide by zero")
        }

        return BigFraction(denominator, numerator)
    }

    fun absoluteValue(): BigFraction = BigFraction(numerator.abs(), denominator)
    fun isNegative(): Boolean = numerator < BigInteger.ZERO
    fun isZero(): Boolean = numerator.eq(0)

    // SIMPLIFICATION

    private fun simplify() {
        simplifyZero()
        simplifyGCD()
        simplifySign()
    }

    /**
     * Set denominator to 1 when numerator is 0
     */
    private fun simplifyZero() {
        if (numerator.eq(0)) {
            denominator = BigInteger.ONE
        }
    }

    /**
     * Move negatives to numerator
     */
    private fun simplifySign() {
        val numNegative = numerator.isNegative()
        val denomNegative = denominator.isNegative()

        when {
            numNegative && denomNegative -> {
                numerator = numerator.abs()
                denominator = denominator.abs()
            }
            !numNegative && denomNegative -> {
                numerator = -numerator
                denominator = denominator.abs()
            }
        }
    }

    /**
     * Simplify using greatest common divisor using Euclidean algorithm
     */
    private fun simplifyGCD() {
        if (!numerator.isZero()) {
            var sum = if (numerator > denominator) numerator else denominator
            var value = if (numerator > denominator) denominator else numerator
            var finished = false

            while (!finished) {
                val remainder = sum % value

                if (remainder == BigInteger.ZERO) {
                    finished = true
                } else {
                    sum = value
                    value = remainder
                }
            }

            val gcd = value
            numerator /= gcd
            denominator /= gcd
        }
    }

    // STRING METHODS

    /**
     * Create a string representation in standard decimal format
     *
     * @param digits [Int]: digits of precision in string. Defaults to 8.
     * Will be ignored if this number results in a string in exponential format
     * @return string representation in decimal format
     */
    fun toDecimalString(digits: Int = 8): String {
        if (denominator.eq(1)) {
            return numerator.toString()
        }

        val numDecimal = numerator.toBigDecimal()
        val denomDecimal = denominator.toBigDecimal()

        var mc = MathContext(digits, RoundingMode.HALF_UP)
        var decimal = numDecimal.divide(denomDecimal, mc)

        // return non-exponential string, regardless of digits
        val isExponentialString = decimal.toString().indexOf('E') != -1
        if (isExponentialString) {
            val precision = numerator.toString().length + digits
            mc = MathContext(precision, RoundingMode.HALF_UP)
            decimal = numDecimal.divide(denomDecimal, mc)
        }

        return decimal.toString().trimEnd('0')
    }

    /**
     * Create a fractional representation of the number, either as whole number or fraction
     *
     * @return string representation of number in fractional format
     */
    fun toFractionString(): String = if (denominator.eq(1)) {
        numerator.toString()
    } else {
        "$numerator/$denominator"
    }

    fun toPairString(): String = "($numerator, $denominator)"
    fun toBFString(): String = "BF[$numerator $denominator]"

    override fun toString(): String = toBFString()

    override fun hashCode(): Int = toPair().hashCode()

    // CASTING

    fun toPair(): Pair<BigInteger, BigInteger> = Pair(numerator, denominator)

    /**
     * If value is between min and max Byte values, cast to Byt using simple division, rounding down.
     *
     * @return number as Byte
     * @throws BigFractionOverflowException if value is outside range of Byte value
     */
    override fun toByte(): Byte {
        val value = numerator / denominator
        val maxByte = Byte.MAX_VALUE.toInt().toBigInteger()
        val minByte = Byte.MIN_VALUE.toInt().toBigInteger()
        if (value in minByte..maxByte) {
            return value.toByte()
        }

        throw overflowException("Byte")
    }

    /**
     * If value is between min and max Char values, cast to Char using simple division, rounding down.
     * Accounts for Chars being non-negative
     *
     * @return number as Char
     * @throws BigFractionOverflowException if value is outside range of Char value
     */
    override fun toChar(): Char {
        val value = numerator / denominator
        val maxChar = Char.MAX_VALUE.code.toBigInteger()
        val minChar = Char.MIN_VALUE.code.toBigInteger()
        if (value in minChar..maxChar) {
            return value.toInt().toChar()
        }

        throw overflowException("Char")
    }

    /**
     * If value is between min and max Short values, cast to Short using simple division, rounding down.
     *
     * @return number as Short
     * @throws BigFractionOverflowException if value is outside range of Short value
     */
    override fun toShort(): Short {
        val value = numerator / denominator
        val maxShort = Short.MAX_VALUE.toInt().toBigInteger()
        val minShort = Short.MIN_VALUE.toInt().toBigInteger()
        if (value in minShort..maxShort) {
            return value.toShort()
        }

        throw overflowException("Short")
    }

    /**
     * If value is between min and max Int values, cast to Int using simple division, rounding down.
     *
     * @return number as Int
     * @throws BigFractionOverflowException if value is outside range of Int value
     */
    override fun toInt(): Int {
        val value = numerator / denominator
        val maxInt = Int.MAX_VALUE.toBigInteger()
        val minInt = Int.MIN_VALUE.toBigInteger()
        if (value in minInt..maxInt) {
            return value.toInt()
        }

        throw overflowException("Int")
    }

    /**
     * If value is between min and max Long values, cast to Long using simple division, rounding down.
     *
     * @return number as Long
     * @throws BigFractionOverflowException if value is outside range of Long value
     */
    override fun toLong(): Long {
        val value = numerator / denominator
        val maxLong = Long.MAX_VALUE.toBigInteger()
        val minLong = Long.MIN_VALUE.toBigInteger()
        if (value in minLong..maxLong) {
            return value.toLong()
        }

        throw overflowException("Long")
    }

    /**
     * If value is between min and max Double values, cast to Double, using division with precision of 20.
     *
     * @return number as Double
     * @throws BigFractionOverflowException if value is outside range of Double value
     */
    override fun toDouble(): Double {
        val mc = MathContext(20, RoundingMode.HALF_UP)
        val numDecimal = numerator.toBigDecimal()
        val denomDecimal = denominator.toBigDecimal()
        val value = numDecimal.divide(denomDecimal, mc)

        val maxDouble = Double.MAX_VALUE.toBigDecimal()
        val minDouble = -maxDouble

        if (value in minDouble..maxDouble) {
            return value.toDouble()
        }

        throw overflowException("Double")
    }


    /**
     * If value is between min and max Float values, cast to Float, using division with precision of 20.
     *
     * @return number as Float
     * @throws BigFractionOverflowException if value is outside range of Float value
     */
    override fun toFloat(): Float {
        val mc = MathContext(20, RoundingMode.HALF_UP)
        val numDecimal = numerator.toBigDecimal()
        val denomDecimal = denominator.toBigDecimal()
        val value = numDecimal.divide(denomDecimal, mc)

        val maxFloat = Float.MAX_VALUE.toBigDecimal()
        val minFloat = -maxFloat

        if (value in minFloat..maxFloat) {
            return value.toFloat()
        }

        throw overflowException("Float")
    }

    fun toBigInteger(): BigInteger = numerator / denominator
    fun toBI(): BigInteger = toBigInteger()
    fun toBigDecimal(precision: Int = 20): BigDecimal {
        val mc = MathContext(precision, RoundingMode.HALF_UP)
        return numerator.toBigDecimal().divide(denominator.toBigDecimal(), mc)
    }

    private fun overflowException(type: String): Exception = BigFractionOverflowException("Overflow when casting to $type", toFractionString())

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
        val NEG_ONE = BigFraction(-1)

        fun parse(s: String): BigFraction {
            if (isBFString(s)) {
                return parseBFString(s)
            }

            return parseDecimal(s)
        }

        fun isBFString(s: String): Boolean = checkIsBFString(s)
    }
}
