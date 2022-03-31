package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.isNegative
import com.example.trickcalculator.utils.ExprLPair
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.getGCD
import java.math.BigInteger

// this needs to return a pair instead of an ED in order to avoid getting stuck in an infinite loop of constructor/simplify
fun simplify(ed: ExactDecimal): ExprLPair {
    var currentNum = ed.numerator
    var currentDenom = ed.denominator

    if (currentNum.isZero()) {
        return Pair(
            listOf(Expression.ZERO), listOf(Expression.ONE)
        )
    }

    val numGroups = currentNum.groupBy { it.isAllConstants() }
    val denomGroups = currentDenom.groupBy { it.isAllConstants() }
    val numConstant: Expression =
        numGroups[true]?.fold(Expression.ONE) { acc, expr -> acc * expr } ?: Expression.ONE
    val denomConstant: Expression =
        denomGroups[true]?.fold(Expression.ONE) { acc, expr -> acc * expr } ?: Expression.ONE

    val numResult = simplifyAllStrings(numGroups[false] ?: listOf())
    val denomResult = simplifyAllStrings(denomGroups[false] ?: listOf())

    val result = removeCommon(numResult.first, denomResult.first)
    currentNum = result.first + numConstant * Term(numResult.second)
    currentDenom = result.second + denomConstant * Term(denomResult.second)

    return Pair(currentNum, currentDenom)
}

fun removeCommon(num: ExprList, denom: ExprList): ExprLPair {
    if (num.isEmpty() || denom.isEmpty()) {
        return Pair(num, denom)
    }

    // map: O(n)
    // groupBy: O(n)
    // count: O(n)
    // adding to num/denom: O(n)
    val newNumerator: MutableList<Expression> = mutableListOf()
    val newDenominator: MutableList<Expression> = mutableListOf()
    (num.map { Pair(it, "num") } + denom.map { Pair(it, "denom") })
        .groupBy { it.first }
        .forEach { (expr, group) ->
            val countNum = group.count { it.second == "num" }
            val countDenom = group.size - countNum

            if (countNum > countDenom) {
                for (i in (0 until countNum - countDenom)) {
                    newNumerator.add(expr)
                }
            } else if (countDenom > countNum) {
                for (i in (0 until countDenom - countNum)) {
                    newDenominator.add(expr)
                }
            }
        }

    newNumerator.sortBy { it.maxExponent }
    newDenominator.sortBy { it.maxExponent }

    return Pair(newNumerator, newDenominator)
}

fun simplifyAllStrings(exprs: ExprList): Pair<ExprList, ExactFraction> {
    var c = ExactFraction.ONE

    // maybe gcd of nums and denoms? that's probably right lol
    val newExprs = exprs.map {
        var numCoeffs = it.terms.map { t -> t.coefficient.numerator }
        var denomCoeffs = it.terms.map { t -> t.coefficient.denominator }

        if (numCoeffs.all { co -> co.isNegative() }) {
            c = -ExactFraction.ONE
            numCoeffs = numCoeffs.map { co -> -co }
        }
        if (denomCoeffs.all { co -> co.isNegative() }) {
            c = -ExactFraction.ONE
            denomCoeffs = denomCoeffs.map { co -> -co }
        }

        val numGcd = getListGCD(numCoeffs)
        val denomGcd = getListGCD(denomCoeffs)
        val gcd = ExactFraction(numGcd, denomGcd)

        c *= gcd
        val newTerms = it.terms.map { t ->
            val newCoeff = t.coefficient / gcd
            Term(newCoeff, t.exp)
        }
        Expression(newTerms)
    }

    return Pair(newExprs, c)
}

fun getListGCD(values: List<BigInteger>): BigInteger {
    if (values.isEmpty()) {
        return BigInteger.ONE
    }

    if (values.size == 1) {
        return values[0]
    }

    if (values.size == 2) {
        return getGCD(values[0], values[1])
    }

    var current: BigInteger = values[0]
    for (value in values) {
        current = getGCD(value, current)
        if (current == BigInteger.ONE) {
            return BigInteger.ONE
        }
    }

    return current
}
