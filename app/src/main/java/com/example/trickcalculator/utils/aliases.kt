package com.example.trickcalculator.utils

import exactfraction.ExactFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

typealias IntList = List<Int>
typealias StringList = List<String>
