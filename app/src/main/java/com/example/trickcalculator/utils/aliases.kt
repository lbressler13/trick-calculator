package com.example.trickcalculator.utils

import com.example.trickcalculator.ui.history.HistoryItem
import exactnumbers.exactfraction.ExactFraction

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions.
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction

/**
 * History composed of HistoryItems
 */
typealias History = List<HistoryItem>
