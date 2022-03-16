package com.example.trickcalculator.utils

import com.example.trickcalculator.bigfraction.BigFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as BigFractions
 */
typealias OperatorFunction = (BigFraction, BigFraction, String) -> BigFraction

typealias IntList = List<Int>
typealias StringList = List<String>
typealias MStringList = MutableList<String>
