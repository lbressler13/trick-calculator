package com.example.trickcalculator.utils

import com.example.trickcalculator.exactdecimal.Term
import com.example.trickcalculator.exactfraction.ExactFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

typealias IntList = List<Int>
typealias StringList = List<String>
typealias MStringList = MutableList<String>

typealias TermList = List<Term>
