package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.toExactFraction
import com.example.trickcalculator.utils.TermList

// representation of a single term
// easily extensible for future additions (i.e. coefficient, piExp, eExp)
class Term {
    val coefficient: ExactFraction
    val exp: Int
    val shortString: String
    private val pi = ExactConstants.PI
    private val shortPi = "p"

    constructor(coefficient: ExactFraction, exp: Int) {
        this.coefficient = coefficient
        this.exp = exp
        this.shortString = "$coefficient$shortPi$exp"
    }

    // parses short string
    constructor(s: String) {
        val numbers = s.split(shortPi)
        this.coefficient = Integer.parseInt(numbers[0]).toExactFraction()
        this.exp = Integer.parseInt(numbers[1])
        this.shortString = s
    }

    operator fun times(other: Term): Term {
        val newCoefficient = coefficient * other.coefficient
        val newExp = exp + other.exp
        return Term(newCoefficient, newExp)
    }

    operator fun times(other: TermList): TermList = other.map { times(it) }
    operator fun times(other: Expression): Expression = other * this

    operator fun plus(other: Term): Term {
        if (exp != other.exp) {
            throw ArithmeticException("Cannot add Terms with different exp values")
        }

        return Term(coefficient + other.coefficient, exp)
    }

    override fun equals(other: Any?): Boolean = other != null && other is Term
            && coefficient == other.coefficient && exp == other.exp


    fun isZero(): Boolean = coefficient.isZero()
    fun isNotZero(): Boolean = !coefficient.isZero()
    fun isNegative(): Boolean = coefficient < 0

    override fun toString(): String = "$coefficient$pi^$exp"
    override fun hashCode(): Int = Pair(coefficient, exp).hashCode()
}
