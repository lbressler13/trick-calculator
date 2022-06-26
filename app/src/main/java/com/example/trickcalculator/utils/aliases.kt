package com.example.trickcalculator.utils

import com.example.trickcalculator.ui.history.HistoryItem
import exactfraction.ExactFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

typealias History = List<HistoryItem>
