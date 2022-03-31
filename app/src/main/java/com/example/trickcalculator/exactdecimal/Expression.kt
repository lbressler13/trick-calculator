package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.TermList
import java.math.BigInteger

class Expression {
    val terms: TermList
    val maxExponent: Int

    constructor() {
        terms = listOf()
        maxExponent = 0
    }

    constructor(terms: TermList) {
        this.terms = terms

        maxExponent = if (terms.isEmpty()) {
            0
        } else {
            terms.maxOrNull()!!.exp
        }
    }

    constructor(constant: BigInteger) {
        val term = Term(constant)
        terms = listOf(term)
        maxExponent = 0
    }

    constructor(term: Term) {
        terms = listOf(term)
        maxExponent = term.exp
    }

    operator fun times(other: Term): Expression = terms.map { it * other }.asExpression()
    operator fun times(other: Expression): Expression =
        terms.flatMap { it * other.terms }.asExpression()

    operator fun plus(other: Expression): Expression = Expression(terms + other.terms)

    override operator fun equals(other: Any?): Boolean {
        return other != null
                && other is Expression
                && simplifyExpression().terms == other.simplifyExpression().terms
    }

    operator fun unaryMinus(): Expression = Expression(terms.map { -it })

    // guaranteed to have terms sorted by exponent
    fun simplifyExpression(): Expression {
        if (terms.size < 2) {
            return Expression(terms)
        }

        val groups = terms.groupBy { it.exp }
        val simpleTerms = groups.map {
            val newCoefficient = it.value.fold(ExactFraction.ZERO) { acc, term ->
                acc + term.coefficient
            }

            val exp = it.key
            Term(newCoefficient, exp)
        }
            .filter { it.isNotZero() }
            .sorted()

        return Expression(simpleTerms)
    }

    // because terms are a summation, it's only 0 if ALL terms are zero
    fun isZero(): Boolean {
        if (terms.isEmpty()) {
            return true
        }

        val simplified = simplifyExpression()
        return simplified.terms.isEmpty() || simplified.terms.all { it.isZero() }
    }

    fun isAllConstants(): Boolean = terms.all { it.exp == 0 }

    override fun toString(): String {
        return "Expr($terms)"
    }

    override fun hashCode(): Int {
        return terms.sorted().hashCode()
    }

    companion object {
        val ZERO = Expression(listOf(Term(0)))
        val ONE = Expression(listOf(Term(1)))
    }
}
