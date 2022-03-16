package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.bigfraction.BigFraction
import com.example.trickcalculator.bigfraction.eq
import com.example.trickcalculator.bigfraction.toBigFraction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.listsEqual
import java.lang.Integer.min
import kotlin.math.max

class ExactDecimal private constructor() : Number() {
    var numerator: StringList = mutableListOf()
    var denominator: StringList = mutableListOf()
    var coefficient: BigFraction = BigFraction.ONE

    private val pi: String = "p"
    private val ops: StringList = listOf("+", "-", "x", "/")

    // CONSTRUCTORS

    constructor(coefficient: BigFraction) : this() {
        this.coefficient = coefficient
    }

    constructor(coefficient: Int) : this(coefficient.toBigFraction())
    constructor(coefficient: Long) : this(coefficient.toBigFraction())

    constructor(numerator: StringList, denominator: StringList, coefficient: BigFraction) : this() {
        this.numerator = numerator
        this.denominator = denominator
        this.coefficient = coefficient

        simplify()
    }

    constructor(numerator: StringList, denominator: StringList, coefficient: Int) :
            this(numerator, denominator, coefficient.toBigFraction())

    constructor(numerator: StringList, denominator: StringList, coefficient: Long) :
            this(numerator, denominator, coefficient.toBigFraction())

    // UNARY OPERATORS

    operator fun unaryMinus(): ExactDecimal = ExactDecimal(numerator, denominator, -coefficient)
    operator fun unaryPlus(): ExactDecimal = ExactDecimal(numerator, denominator, coefficient)
    operator fun not(): Boolean = isZero()
    // TODO inc + dec

    // BINARY OPERATORS

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ExactDecimal) {
            return false
        }

        return coefficient == other.coefficient
                && listsEqual(numerator, other.numerator)
                && listsEqual(denominator, other.denominator)
        // TODO fix this
    }

    operator fun plus(other: ExactDecimal): ExactDecimal {
        val newNumerator = numerator + other.denominator
        val newDenominator = denominator + other.numerator
        val newCoefficient = BigFraction(coefficient, other.coefficient)
        return ExactDecimal(newNumerator, newDenominator, newCoefficient)
    }

    operator fun minus(other: ExactDecimal): ExactDecimal = plus(-other)

    operator fun times(other: ExactDecimal): ExactDecimal {
        val newNumerator = numerator + other.numerator
        val newDenominator = denominator + other.denominator
        val newCoefficient = coefficient * other.coefficient
        return ExactDecimal(newNumerator, newDenominator, newCoefficient)
    }

    operator fun div(other: ExactDecimal): ExactDecimal = times(other.inverse())

    // UNARY NON-OPERATORS

    fun isZero(): Boolean = coefficient.isZero()
    fun inverse(): ExactDecimal {
        if (coefficient.eq(0)) {
            throw ArithmeticException("divide by zero")
        }

        return ExactDecimal(denominator, numerator, coefficient.inverse())
    }

    private fun simplify() {
        simplifyZero()
//        simplifyCommon()
//
//        simplifyAllStrings()
//        simplifyCommon()
//        simplifyZero()
    }

    private fun simplifyZero() {
        if (coefficient.isZero()) {
            numerator = listOf()
            denominator = listOf()
        }
    }

    private fun simplifyCommon() {
        // TODO does this work with repeats and stuff?
        // NO it does not
        val newNumerator: StringList = (numerator - denominator)
        val newDenominator: StringList = (denominator - numerator)

        numerator = newNumerator
        denominator = newDenominator
    }

    private fun simplifyAllStrings() {}

    /**
     * Get greatest common divisor of 2 numbers using Euclidean algorithm
     */
    // TODO gcd of more than 2
    private fun getGCD(val1: Int, val2: Int): Int {
        if (val1 == 0 || val2 == 0 || val1 == val2) {
            return 1
        }

        var sum = max(val1, val2)
        var value = min(val1, val2)
        var finished = false


        while (!finished) {
            val remainder = sum % value

            if (remainder == 0) {
                finished = true
            } else {
                sum = value
                value = remainder
            }
        }

        return value
    }

    override fun toString(): String {
        return "ED[$coefficient $numerator $denominator]"
    }

    // TODO actual casting
    override fun toByte(): Byte = 0
    override fun toChar(): Char = Char(0)
    override fun toDouble(): Double = 0.0
    override fun toFloat(): Float = 0f
    override fun toInt(): Int = 0
    override fun toLong(): Long = 0L
    override fun toShort(): Short = 0
}