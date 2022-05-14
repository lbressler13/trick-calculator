package com.example.trickcalculator.utils

import com.example.trickcalculator.exactdecimal.Expression
import com.example.trickcalculator.exactdecimal.Term
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ui.history.HistoryItem

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

typealias IntList = List<Int>
typealias StringList = List<String>

typealias History = List<HistoryItem>

typealias TermList = List<Term>
typealias ExprList = List<Expression>
typealias ExprLPair = Pair<ExprList, ExprList>
