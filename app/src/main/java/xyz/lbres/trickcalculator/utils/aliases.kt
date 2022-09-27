package xyz.lbres.trickcalculator.utils

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.trickcalculator.ui.history.HistoryItem

/**
 * History composed of HistoryItems
 */
typealias History = List<HistoryItem>

/**
 * Function to apply an operator to two numbers.
 * Operator is represented as a string and numbers are represented as ExactFractions.
 */
typealias OperatorFunction = (ExactFraction, ExactFraction, String) -> ExactFraction
