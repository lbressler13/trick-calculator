package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.isNegative
import com.example.trickcalculator.utils.ExprLPair
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.getGCD
import com.example.trickcalculator.utils.getListGCD
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
    // TODO ^ test that
    // this is literally what TDD is for
    // This has explained the purpose of TDD, much better than anything at work ever could
    val newExprs = exprs.map {
        var numCoeffs = it.terms.map { t -> t.coefficient.numerator }
        var denomCoeffs = it.terms.map { t -> t.coefficient.denominator }

        if (numCoeffs.all { co -> co.isNegative() }) {
            c = -c
            numCoeffs = numCoeffs.map { co -> -co }
        }
        if (denomCoeffs.all { co -> co.isNegative() }) {
            c = -c
            denomCoeffs = denomCoeffs.map { co -> -co }
        }

        val numGcd = getListGCD(numCoeffs)
        val denomGcd = getListGCD(denomCoeffs)
        val gcd = ExactFraction(numGcd, denomGcd)

        c *= gcd
        val newTerms = it.terms.mapIndexed { index, t ->
            val oldCoeff = ExactFraction(
                numCoeffs[index],
                denomCoeffs[index]
            ) // uses coeffs that have been mapped to positive if possible
            val newCoeff = oldCoeff / gcd
            Term(newCoeff, t.exp)
        }
        Expression(newTerms)
    }
//    val newExprs = exprs.map {
//        val result = simplifyCoeffsSingleExpr(it)
//        c *= result.second
//        result.first
//    }

    return Pair(newExprs, c)
}

fun simplifyCoeffsSingleExpr(expr: Expression): Pair<Expression, BigInteger> {
    var negative = expr.terms.all { it.coefficient.isNegative() }

    val totalDenom: BigInteger =
        expr.terms.fold(BigInteger.ONE) { acc, t -> acc * t.coefficient.denominator }
    val numPairs = expr.terms.map {
        // multiply by all denoms except current
        var scaledNum = it.coefficient.numerator * totalDenom / it.coefficient.denominator
        if (negative) {
            scaledNum = -scaledNum
        }
        Pair(scaledNum, it.exp)
    }

    val gcd = getListGCD(numPairs.map { it.first })
    val simplified = numPairs.map {
        val coeff = ExactFraction(it.first, gcd) / totalDenom
        Term(coeff, it.second)
    }.asExpression()

    return if (negative) {
        Pair(simplified, -gcd)
    } else {
        Pair(simplified, gcd)
    }
}
