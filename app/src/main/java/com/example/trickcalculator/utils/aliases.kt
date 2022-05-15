package com.example.trickcalculator.utils

import com.example.trickcalculator.exactdecimal.Expression
import com.example.trickcalculator.exactdecimal.Term
import exactfraction.ExactFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

typealias IntList = List<Int>
typealias StringList = List<String>

typealias TermList = List<Term>
typealias ExprList = List<Expression>
typealias ExprLPair = Pair<ExprList, ExprList>
