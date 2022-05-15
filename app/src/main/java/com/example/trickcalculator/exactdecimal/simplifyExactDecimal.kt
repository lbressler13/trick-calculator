package com.example.trickcalculator.exactdecimal

import exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprLPair
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.getGCD
import com.example.trickcalculator.utils.getListGCD
import java.math.BigInteger

// TODO don't include term w/ value 1,0
// this needs to return a pair instead of an ED in order to avoid getting stuck in an infinite loop of constructor/simplify
fun simplify(ed: ExactDecimal): ExactDecimal {
    var currentNum = ed.numerator
    var currentDenom = ed.denominator

    if (currentNum.isZero()) {
        return ExactDecimal(
            listOf(Expression.ZERO), listOf(Expression.ONE)
        )
    }

    currentNum = currentNum.map { combineByExp(it) }
    currentDenom = currentDenom.map { combineByExp(it) }

    var numConstant = ExactFraction.ONE
    currentNum = currentNum.map {
        val constIndex = it.terms.indexOfFirst { t -> t.exp == 0 }
        if (constIndex != -1) {
            numConstant *= it.terms[constIndex].coefficient
            val termsStart = it.terms.subList(0, constIndex)
            val termsEnd = if (constIndex == it.terms.lastIndex) listOf() else it.terms.subList(constIndex + 1, it.terms.size)
            Expression(termsStart + termsEnd)
        } else {
            it
        }
    }.filterNot { it.isZero() }

    var denomConstant = ExactFraction.ONE
    currentDenom = currentDenom.map {
        val constIndex = it.terms.indexOfFirst { t -> t.exp == 0 }
        if (constIndex != -1) {
            denomConstant *= it.terms[constIndex].coefficient
            val termsStart = it.terms.subList(0, constIndex)
            val termsEnd = if (constIndex == it.terms.lastIndex) listOf() else it.terms.subList(constIndex + 1, it.terms.size)
            Expression(termsStart + termsEnd)
        } else {
            it
        }
    }.filterNot { it.isZero() }

    val constGcd = getGCD(numConstant, denomConstant)
    numConstant /= constGcd
    denomConstant /= constGcd

    val numResult = if (currentNum.isNullOrEmpty()) {
        Pair(listOf(), ExactFraction.ONE)
    } else {
        simplifyAllCoeffs(currentNum ?: listOf(Expression.ONE))
    }
    val denomResult = if (currentDenom.isNullOrEmpty()) {
        Pair(listOf(), ExactFraction.ONE)
    } else {
        simplifyAllCoeffs(currentDenom ?: listOf(Expression.ONE))
    }

    val result = removeCommon(numResult.first, denomResult.first)
    numConstant *= numResult.second
    denomConstant *= denomResult.second
    currentNum = result.first + Expression(Term(numResult.second))
    currentDenom = result.second + Expression(Term(denomResult.second))

    return ExactDecimal(currentNum, currentDenom)
}

fun combineByExp(expr: Expression): Expression {
    val groups = expr.terms.groupBy { it.exp }
    val newTerms = groups.map {
        val exp = it.key
        val terms = it.value
        val newCoeff = terms.fold(ExactFraction.ONE) { acc, t -> acc * t.coefficient }
        Term(newCoeff, exp)
    }
    return Expression(newTerms)
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

fun simplifyAllCoeffs(exprs: ExprList): Pair<ExprList, ExactFraction> {
    if (exprs.isEmpty()) {
        return Pair(listOf(), ExactFraction.ZERO)
    }

    var c = ExactFraction.ONE
    val newExprs = exprs.map {
        val result = simplifyCoeffsSingleExpr(it)
        c *= result.second
        result.first
    }

    if (c.isZero()) {
        return Pair(listOf(Expression.ZERO), ExactFraction.ZERO)
    }

    return Pair(newExprs, c)
}

// for all terms in return, denominator of coeff is 1
fun simplifyCoeffsSingleExpr(expr: Expression): Pair<Expression, ExactFraction> {
    if (expr.isZero()) {
        return Pair(expr, ExactFraction.ZERO)
    }

    val negative = expr.terms.all { it.coefficient.isNegative() }

    val totalDenom = expr.terms.fold(BigInteger.ONE) { acc, t -> acc * t.coefficient.denominator }

    val scaledNums = expr.terms.map {
        val signedNum = if (negative) it.coefficient.numerator.abs() else it.coefficient.numerator
        signedNum * totalDenom / it.coefficient.denominator
    }

    val gcd = ExactFraction(getListGCD(scaledNums), totalDenom)

    val newTerms = expr.terms.map {
        val signedNum = if (negative) it.coefficient.numerator.abs() else it.coefficient.numerator
        val oldCoeff = ExactFraction(
            signedNum,
            it.coefficient.denominator
        )
        val newCoeff = oldCoeff / gcd
        Term(newCoeff, it.exp)
    }

    return if (negative) {
        Pair(Expression(newTerms), -gcd)
    } else {
        Pair(Expression(newTerms), gcd)
    }
}
