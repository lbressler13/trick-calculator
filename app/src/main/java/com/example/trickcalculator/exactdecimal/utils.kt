package com.example.trickcalculator.exactdecimal

/**
 * Create Expression from list of Terms
 *
 * @return expression using this list of terms
 */
fun List<Term>.asExpression(): Expression = Expression(this)

fun List<Expression>.isZero(): Boolean = any { it.terms.all { t -> t.isZero() } }

/**
 * Sort function for TermList
 *
 * @return terms sorted by exponent
 */
fun List<Term>.sorted(): List<Term> = sortedBy { it.exp }

fun exprListOfTerm(term: Term) = listOf(Expression(listOf(term)))
