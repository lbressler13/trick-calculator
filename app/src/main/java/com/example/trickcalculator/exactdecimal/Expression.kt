package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.asExpression
import com.example.trickcalculator.ext.dropFirst
import com.example.trickcalculator.ext.sorted
import com.example.trickcalculator.ext.subListFrom
import com.example.trickcalculator.utils.TermList

class Expression(terms: TermList) {
    val terms = simplifyExpression(terms)
    var rationalTerm = ExactFraction.ZERO

    constructor() : this(listOf())

    init {
        val index = this.terms.indexOfFirst { it.exp == 0 }
        if (index >= 0) {
            rationalTerm = this.terms[index].coefficient
        }
    }

    operator fun times(other: Term): Expression = terms.map { it * other }.asExpression()
    operator fun times(other: Expression): Expression =
        terms.flatMap { it * other.terms }.asExpression()

    operator fun plus(other: Expression): Expression {
        var index1 = 0
        var index2 = 0

        val allTerms: MutableList<Term> = mutableListOf()

        // O(n) implementation
        // Sorting would be O(nlog(n)) to sort + O(n) to compare
        while (index1 < terms.size && index2 < other.terms.size) {
            val term1 = terms[index1]
            val term2 = other.terms[index2]
            val exp1 = term1.exp
            val exp2 = term2.exp

            when {
                exp1 < exp2 -> {
                    allTerms.add(term1)
                    index1++
                }
                exp1 > exp2 -> {
                    allTerms.add(term2)
                    index2++
                }
                exp1 == exp2 -> {
                    val sum = term1 + term2
                    if (sum.isNotZero()) {
                        allTerms.add(term1 + term2)
                    }
                    index1++
                    index2++
                }
            }
        }

        if (index1 < terms.size) {
            allTerms.addAll(terms.subListFrom(index1))
        } else if (index2 < other.terms.size) {
            allTerms.addAll(other.terms.subListFrom(index2))
        }

        return allTerms.asExpression()
    }

    override operator fun equals(other: Any?): Boolean {
        return other != null
                && other is Expression
                && terms.sorted() == other.terms.sorted()
    }

    fun dropConstant(): Expression = terms.dropFirst { it.exp == 0 }.asExpression()

    fun simplifyExpression(terms: TermList): TermList {
        if (terms.size < 2) {
            return terms
        }

        val groups = terms.groupBy { it.exp }
        val simpleTerms = groups.map {
            val newCoefficient = it.value.fold(ExactFraction.ONE) { acc, term ->
                acc * term.coefficient
            }

            val exp = it.key
            Term(newCoefficient, exp)
        }.filter { it.isNotZero() }

        return simpleTerms
    }

    override fun toString(): String {
        return "Expr($terms)"
    }
}
