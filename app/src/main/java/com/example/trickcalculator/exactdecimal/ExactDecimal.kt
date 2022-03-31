package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.toExactFraction
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.getGCD
import java.math.BigInteger

class ExactDecimal private constructor() : Number() {
    var numerator: ExprList = mutableListOf(Expression.ONE)
    var denominator: ExprList = mutableListOf(Expression.ONE)

    // CONSTRUCTORS

    constructor(coefficient: ExactFraction) : this() {
        numerator = listOf(Expression(coefficient.numerator))
        denominator = listOf(Expression(coefficient.denominator))
    }

    constructor(coefficient: Int) : this(coefficient.toExactFraction())
    constructor(coefficient: Long) : this(coefficient.toExactFraction())

    // do I accept expressions as standard strings? Or just TSS?
    // TSS is easier here, but more difficult in runComputation
    // Not difficult to parse one expr, but it gets complicated if there are multiple expressions
    // Realistically, runComputation initializes an ED with 1 term. Then it expands to multiple terms via ops
    // This makes sense

    constructor(numerator: ExprList, denominator: ExprList) : this() {
        this.numerator = numerator
        this.denominator = denominator

        if (numerator.isEmpty()) {
            throw NumberFormatException("Numerator cannot be empty")
        }

        if (denominator.isEmpty()) {
            throw NumberFormatException("Denominator cannot be empty")
        }

        if (denominator.isZero()) {
            throw ArithmeticException("Divide by zero")
        }

        val simplified = simplify(this)
        this.numerator = simplified.first
        this.denominator = simplified.second
    }

    constructor(numerator: ExprList) : this(numerator, listOf(Expression.ONE))

    // UNARY OPERATORS

    operator fun unaryMinus(): ExactDecimal {
        val newNumerator = numerator.map { -it }
        return ExactDecimal(newNumerator, denominator)
    }

    operator fun unaryPlus(): ExactDecimal = ExactDecimal(numerator, denominator)
    operator fun not(): Boolean = isZero()

    // BINARY OPERATORS

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is ExactDecimal) {
            return false
        }

        return numerator == other.numerator && denominator == other.denominator
    }

    /**
     * Computation:
     * x/y + a/b = (xb + ay)/(yb)
     * x/y + a/y = (x + a)/y
     */
    operator fun plus(other: ExactDecimal): ExactDecimal {
        if (denominator == other.denominator) {
            val newDenominator = denominator // y
            val newNumerator = addExpressionLists(numerator, other.numerator) // x + a
            return ExactDecimal(listOf(newNumerator), newDenominator)
        }

        val newDenominator = denominator + other.denominator // yb
        val exprList1 = numerator + other.denominator // xb
        val exprList2 = other.numerator + denominator // ay
        val newNumerator = addExpressionLists(exprList1, exprList2) // xb + ay

        return ExactDecimal(listOf(newNumerator), newDenominator)
    }

    operator fun minus(other: ExactDecimal): ExactDecimal = plus(-other)

    operator fun times(other: ExactDecimal): ExactDecimal {
        val newNumerator = numerator + other.numerator
        val newDenominator = denominator + other.denominator
        return ExactDecimal(newNumerator, newDenominator)
    }

    operator fun div(other: ExactDecimal): ExactDecimal = times(other.inverse())

    // UNARY NON-OPERATORS

    fun isZero(): Boolean = numerator.isZero()
    fun inverse(): ExactDecimal {
        if (numerator.isZero()) {
            throw ArithmeticException("divide by zero")
        }

        val simpleNum = numerator.fold(Expression.ONE) { acc, expression -> acc * expression }
            .simplifyExpression()
        val simpleDenom = denominator.fold(Expression.ONE) { acc, expression -> acc * expression }
            .simplifyExpression()

        // check if numerator/denominator consist of single integer value
        val numIsInt = simpleNum.terms.size == 1 && simpleNum.terms[0].exp == 0
        val denomIsInt = simpleDenom.terms.size == 1 && simpleDenom.terms[0].exp == 0

        if (numIsInt && simpleNum.terms[0].coefficient.isZero()) {
            throw ArithmeticException("divide by zero")
        }

        if (numIsInt && denomIsInt) {
            val product = simpleNum.terms[0].coefficient * simpleDenom.terms[0].coefficient
            return ExactDecimal(product.inverse())
        }

        return ExactDecimal(denominator, numerator)
    }

    /**
     * Add two lists of expressions
     *
     * @param exprList1 [ExprList]: first list to add
     * @param exprList2 [ExprList]: second list to add
     * @return single simplified expression, which is the sum of both lists
     */
    fun addExpressionLists(exprList1: ExprList, exprList2: ExprList): Expression {
        val partialTotal = exprList1.fold(Expression()) { acc, current -> acc * current }
        val total = exprList2.fold(partialTotal) { acc, current -> acc + current }
        return total
    }

    override fun toString(): String {
        return "ED[$numerator $denominator]"
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
        return result
    }
}
