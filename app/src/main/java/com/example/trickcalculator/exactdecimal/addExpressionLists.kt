package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.utils.ExprList

/**
 * Add two lists of expressions
 *
 * @param exprList1 [ExprList]: first list to add
 * @param exprList2 [ExprList]: second list to add
 * @return single simplified expression, which is the sum of both lists
 */
fun addExpressionLists(exprList1: ExprList, exprList2: ExprList): Expression {
    val partialTotal = exprList1.fold(Expression()) { acc, current-> acc * current }
    val total = exprList2.fold(partialTotal) { acc, current -> acc + current }
    return total
}
