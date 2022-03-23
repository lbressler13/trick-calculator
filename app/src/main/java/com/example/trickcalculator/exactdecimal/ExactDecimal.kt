package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.toExactFraction
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.IntList
import java.lang.Integer.min
import kotlin.math.absoluteValue
import kotlin.math.max

// TODO about a million tests

class ExactDecimal private constructor() : Number() {
    var numerator: ExprList = mutableListOf()
    var denominator: ExprList = mutableListOf()
    var coefficient: ExactFraction = ExactFraction.ONE

    // CONSTRUCTORS

    constructor(coefficient: ExactFraction) : this() {
        this.coefficient = coefficient
    }

    constructor(coefficient: Int) : this(coefficient.toExactFraction())
    constructor(coefficient: Long) : this(coefficient.toExactFraction())

    // TODO do I accept expressions as standard strings? Or just TSS?
    // TSS is easier here, but more difficult in runComputation
    // Not difficult to parse one expr, but it gets complicated if there are multiple expressions
    // Realistically, runComputation initializes an ED with 1 term. Then it expands to multiple terms via ops
    // This makes sense

    constructor(numerator: ExprList, denominator: ExprList, coefficient: ExactFraction) : this() {
        this.numerator = numerator
        this.denominator = denominator
        this.coefficient = coefficient

        simplify()
    }

    // runComputation will likely use one of these
//    constructor(numerator: String, denominator: String, coefficient: ExactFraction) :
//            this(listOf(numerator), listOf(denominator), coefficient)
//    constructor(numerator: String, denominator: String) :
//            this(listOf(numerator), listOf(denominator), ExactFraction.ONE)
//
//    constructor(numerator: StringList, denominator: StringList, coefficient: Int) :
//            this(numerator, denominator, coefficient.toExactFraction())
//
//    constructor(numerator: StringList, denominator: StringList, coefficient: Long) :
//            this(numerator, denominator, coefficient.toExactFraction())

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
                && numerator == other.numerator
                && denominator == other.denominator
    }

    /**
     * Computation:
     * x/y + a/b = (xb + ay)/(yb)
     * x/y + a/y = (x + a)/y
     */
    operator fun plus(other: ExactDecimal): ExactDecimal {
        var newCoefficient = ExactFraction(coefficient, other.coefficient)
        if (denominator == other.denominator) {
            val newDenominator = denominator // y
            val newNumerator = addExpressionLists(numerator, other.numerator) // x + a
            return ExactDecimal(listOf(newNumerator), newDenominator, newCoefficient)
        }

        val newDenominator = denominator + other.denominator // yb
        val exprList1 = numerator + other.denominator // xb
        val exprList2 = other.numerator + denominator // ay
        var newNumerator = addExpressionLists(exprList1, exprList2) // xb + ay

        val constant = newNumerator.rationalTerm
        if (constant != ExactFraction.ZERO) {
            newCoefficient *= constant
            newNumerator = newNumerator.dropConstant()
        }

        return ExactDecimal(listOf(newNumerator), newDenominator, newCoefficient)
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
        val newNumerator = numerator - denominator
        val newDenominator = denominator - numerator

        numerator = newNumerator
        denominator = newDenominator
    }

    private fun simplifyAllStrings() {}

    private fun getGCD(values: IntList): Int {
        if (values.size < 2) {
            return 1
        }

        if (values.size == 2) {
            return getGCDPair(values[0].absoluteValue, values[1].absoluteValue)
        }

        var current: Int = values[0].absoluteValue
        for (value in values) {
            current = getGCDPair(value.absoluteValue, current)
            if (current == 1) {
                return 1
            }
        }

        return current
    }

    /**
     * Get greatest common divisor of 2 numbers using Euclidean algorithm
     */
    private fun getGCDPair(val1: Int, val2: Int): Int {
        if (val1 == 0 || val2 == 0 || val1 == val2) {
            return 1
        }

        if (val1 == val2) {
            return val1
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

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        result = 31 * result + coefficient.hashCode()
        return result
    }
}