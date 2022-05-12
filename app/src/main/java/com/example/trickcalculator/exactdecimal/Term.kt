package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.toEF
import com.example.trickcalculator.utils.TermList
import java.math.BigInteger

// representation of a single term
// easily extensible for future additions (i.e. coefficient, piExp, eExp)
class Term : Comparable<Term> {
    val coefficient: ExactFraction
    val exp: Int
    val shortString: String // abbreviated as tss (NOT ss)
    private val pi = ExactConstants.PI
    private val shortPi = "p"

    constructor(coefficient: ExactFraction, exp: Int) {
        this.coefficient = coefficient
        this.exp = if (coefficient.isZero()) 0 else exp
        this.shortString = "$coefficient$shortPi${this.exp}"
    }

    constructor(coefficient: Int, exp: Int) : this(coefficient.toEF(), exp)
    constructor(coefficient: ExactFraction) : this(coefficient, 0)
    constructor(coefficient: BigInteger) : this(coefficient.toEF(), 0)
    constructor(coefficient: Int) : this(coefficient.toEF(), 0)
    constructor(coefficient: Long) : this(coefficient.toEF(), 0)

    // parses short string
    constructor(s: String) {
        val isEF = try {
            ExactFraction(s)
            true
        } catch (e: Exception) {
            false
        }

        if (isEF) {
            coefficient = ExactFraction(s)
            exp = 0
            shortString = "${coefficient}p0"
        } else {
            val numbers = s.split(shortPi)
            coefficient = ExactFraction(numbers[0])
            exp = if (coefficient.isZero()) 0 else Integer.parseInt(numbers[1])
            shortString = s
        }
    }

    operator fun times(other: Term): Term {
        val newCoefficient = coefficient * other.coefficient
        val newExp = exp + other.exp
        return Term(newCoefficient, newExp)
    }

    operator fun times(other: TermList): TermList = other.map { times(it) }
    operator fun times(other: Expression): Expression = times(other.terms).asExpression()

    operator fun plus(other: Term): Term {
        val result = when {
            coefficient.isZero() -> other
            other.coefficient.isZero() -> this
            exp == other.exp -> Term(coefficient + other.coefficient, exp)
            else -> null
        }

        if (result == null) {
            throw ArithmeticException("Cannot add Terms with different exp values")
        }

        return result
    }

    override fun equals(other: Any?): Boolean {
        if (other == null && other !is Term) {
            return false
        }

        other as Term

        return (coefficient.isZero() && other.coefficient.isZero()) || (
                coefficient == other.coefficient && exp == other.exp)
    }

    operator fun unaryMinus(): Term = Term(-coefficient, exp)

    // order by exponent, or by coefficient if exp is the same
    override operator fun compareTo(other: Term): Int {
        if (exp != other.exp) {
            return exp.compareTo(other.exp)
        }

        return coefficient.compareTo(other.coefficient)
    }

    // Untested operator!!!
    // operator fun div(other: Term): Term {
    //     val newCoeff = coefficient / other.coefficient
    //     val newExp = exp - other.exp
    //     return Term(newCoeff, newExp)
    // }

    fun isZero(): Boolean = coefficient.isZero()
    fun isNotZero(): Boolean = !coefficient.isZero()
    fun isNegative(): Boolean = coefficient < 0

    override fun toString(): String = "$coefficient$pi^$exp"
    override fun hashCode(): Int = Pair(coefficient, exp).hashCode()

    companion object {
        val ZERO = Term(0)
        val ONE = Term(1)
    }
}
