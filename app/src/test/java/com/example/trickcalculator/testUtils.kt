package com.example.trickcalculator

import com.example.trickcalculator.exactdecimal.Term
import com.example.trickcalculator.ext.asExpression
import com.example.trickcalculator.utils.ExprList
import com.example.trickcalculator.utils.StringList

fun createExprList(strings: StringList): ExprList {
    return strings.map {
        it.split(' ').map { t -> Term(t) }.asExpression()
    }
}
