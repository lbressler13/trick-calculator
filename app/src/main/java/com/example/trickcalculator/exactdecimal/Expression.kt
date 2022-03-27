package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.TermList
import java.math.BigInteger

class Expression {
    val terms: TermList

    constructor() {
        terms = listOf()
    }

    constructor(terms: TermList) {
        this.terms = terms
    }

    constructor(constant: BigInteger) {
        val term = Term(constant)
        terms = listOf(term)
    }

    operator fun times(other: Term): Expression = terms.map { it * other }.asExpression()
    operator fun times(other: Expression): Expression =
        terms.flatMap { it * other.terms }.asExpression()

    operator fun plus(other: Expression): Expression = Expression(terms + other.terms)

//    operator fun plus(other: Expression): Expression {
//        val terms1 = simplifyExpression().terms
//        val terms2 = other.simplifyExpression().terms
//        var index1 = 0
//        var index2 = 0
//
//        val allTerms: MutableList<Term> = mutableListOf()
//
//        // O(n) implementation
//        // Sorting would be O(nlog(n)) to sort + O(n) to compare
//        while (index1 < terms1.size && index2 < terms2.size) {
//            val term1 = terms1[index1]
//            val term2 = terms2[index2]
//            val exp1 = term1.exp
//            val exp2 = term2.exp
//
//            when {
//                exp1 < exp2 -> {
//                    allTerms.add(term1)
//                    index1++
//                }
//                exp1 > exp2 -> {
//                    allTerms.add(term2)
//                    index2++
//                }
//                exp1 == exp2 -> {
//                    val sum = term1 + term2
//                    if (sum.isNotZero()) {
//                        allTerms.add(term1 + term2)
//                    }
//                    index1++
//                    index2++
//                }
//            }
//        }
//
//        if (index1 < terms1.size) {
//            allTerms.addAll(terms1.subListFrom(index1))
//        } else if (index2 < terms2.size) {
//            allTerms.addAll(terms2.subListFrom(index2))
//        }
//
//        return allTerms.asExpression()
//    }

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

    override fun toString(): String {
        return "Expr($terms)"
    }

    companion object {
        val ZERO = Expression(listOf(Term(0)))
        val ONE = Expression(listOf(Term(1)))
    }
}
