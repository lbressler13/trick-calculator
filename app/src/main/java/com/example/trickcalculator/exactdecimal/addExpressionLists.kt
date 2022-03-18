package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.isSingleItem
import com.example.trickcalculator.ext.subListFrom
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.TermList


/**
 * Important definitions
 * Term: c * pi^exp
 * Expression: sum of over k of c_k * pi^exp_k
 * Simplified expression: expression where each k is distinct
 * Term list: representation of expression where each c_k * pi^exp_k is stored as an individual term
 * Expression list: list of expressions, which represents a multiplication over given expressions
 *
 *
 * Term short string: cpe (i.e. 2p3 = 2 * pi^3)
 * Expression encoding: short strings, separated by spaces
 */

// the sublists get multiplied
// each list is an expression list
fun addExpressionLists(exprList1: StringList, exprList2: StringList): String {
    var index1 = 0
    var index2 = 0

    val terms1 = simplifyExpressionList(getTermLists(exprList1))
    val terms2 = simplifyExpressionList(getTermLists(exprList2))

    val allTerms: MutableList<Term> = mutableListOf()

    while (index1 < terms1.size && index2 < terms2.size) {
        val term1 = terms1[index1]
        val term2 = terms2[index2]
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
                allTerms.add(term1 + term2)
                index1++
                index2++
            }
        }
    }

    if (index1 < terms1.size) {
        allTerms.addAll(terms1.subListFrom(index1))
    } else if (index2 < terms2.size) {
        allTerms.addAll(terms2.subListFrom(index2))
    }

    return allTerms.joinToString(" ") { it.shortString }
}

// expands list into 1 expression by performing ops
// guarantees that each expression is just +/-
// no addition/division except between numbers/pi
// in form sum_k (a_k * pi^k), for constants a_k for each given power of pi
fun simplifyExpressionList(exprList: List<TermList>): TermList {
    if (exprList.isEmpty()) {
        return listOf()
    }

    var expr: TermList = exprList[0]

    if (exprList.isSingleItem()) {
        return expr
    }

    exprList.subListFrom(1).forEach {
        expr = it.flatMap { term -> term * expr }
    }

    return simplifyExpression(expr)
}

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
    }

    return simpleTerms
}

fun getTermLists(exprList: StringList): List<TermList> = exprList.map { getTermList(it) }
fun getTermList(expr: String): TermList = expr.split(' ').map { Term(it) }
