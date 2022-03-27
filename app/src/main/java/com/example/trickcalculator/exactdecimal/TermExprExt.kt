package com.example.trickcalculator.exactdecimal

/**
 * Create Expression from list of Terms
 *
 * @return expression using this list of terms
 */
fun List<Term>.asExpression(): Expression = Expression(this)

/**
 * Determine if the product of a list of Expressions is equal to zero
 *
 * @return true if the product is equal to zero, false otherwise
 */
fun List<Expression>.isZero(): Boolean = isEmpty() || any { it.isZero() }
