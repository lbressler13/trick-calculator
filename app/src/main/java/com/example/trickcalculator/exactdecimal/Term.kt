package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.toExactFraction
import com.example.trickcalculator.utils.TermList

// c * pi^exp
class Term : Comparable<Term> {
    val coefficient: ExactFraction
    val exp: Int
    val shortString: String

    constructor(coefficient: ExactFraction, exp: Int) {
        this.coefficient = coefficient
        this.exp = exp
        this.shortString = "${coefficient}p$exp"
    }

    // parses short string
    constructor(s: String) {
        val numbers = s.split('p')
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

    operator fun plus(other: Term): Term {
        if (exp != other.exp) {
            throw ArithmeticException("Cannot add Terms with different exp values")
        }

        return Term(coefficient + other.coefficient, exp)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Term) {
            return false
        }

        return coefficient == other.coefficient && exp == other.exp
    }

    override fun toString(): String {
        return "${coefficient}pi^$exp"
    }

    override operator fun compareTo(other: Term): Int {
        if (exp == other.exp) {
            return coefficient.compareTo(other.coefficient)
        }

        return exp.compareTo(other.exp)
    }

    override fun hashCode(): Int = Pair(coefficient, exp).hashCode()
}
