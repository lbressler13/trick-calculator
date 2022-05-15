package com.example.trickcalculator.ui.history

import exactfraction.ExactFraction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.ext.nextBoolean
import com.example.trickcalculator.ext.nextFromWeightedList
import kotlin.random.Random

private const val maxCompLength = 8
private const val probabilityError = 0.1f

private val operators = listOf("+", "-", "x", "/", "^")
private val illegalOperators = listOf("!", "&", "|", "#", "$", "%")

private val random = Random.Default

fun generateRandomHistoryItem(): HistoryItem {
    val showError = random.nextBoolean(probabilityError)

    val length = (1..maxCompLength).random()
    val computation = generateComputation(length)

    if (showError) {
        return HistoryItem(computation, generateError())
    }
    return HistoryItem(computation, generateResult())
}

// TODO: decimals, parens, syntax issues
private fun generateComputation(length: Int): StringList {
    val probabilityWholeNumber = 0.85f
    val weightedRanges = listOf(
        Pair((0 until 1000), 0.5f),
        Pair((1000 until 100000), 0.2f),
        Pair((100000 until Int.MAX_VALUE / 2), 0.2f),
        Pair((Int.MAX_VALUE / 2..Int.MAX_VALUE), 0.1f)
    )

    val totalLength = length * 2 - 1
    val computation = mutableListOf<String>()

    for (i in (0 until totalLength)) {
        if (i % 2 == 0) {
            val ef = generateExactFraction(
                weightedRanges,
                probabilityWholeNumber,
                allowNegative = false
            )
            computation.add(ef.toDecimalString(5))
        } else {
            computation.add(operators.random())
        }
    }

    return computation
}

private fun generateResult(): ExactFraction {
    val probabilityWholeNumber = 0.6f
    val weightedRanges = listOf(
        Pair((0 until 500), 0.2f),
        Pair((500 until 10000), 0.3f),
        Pair((10000 until Int.MAX_VALUE / 2), 0.25f),
        Pair((Int.MAX_VALUE / 2..Int.MAX_VALUE), 0.2f)
    )

    return generateExactFraction(weightedRanges, probabilityWholeNumber)
}

private fun generateExactFraction(
    weightedRanges: List<Pair<IntRange, Float>>,
    probabilityWholeNumber: Float,
    allowNegative: Boolean = true
): ExactFraction {
    val wholeNumber = random.nextBoolean(probabilityWholeNumber)
    val negative = allowNegative && random.nextBoolean()

    val numeratorRange = random.nextFromWeightedList(weightedRanges)
    var numerator = numeratorRange.random()
    if (negative) {
        numerator *= -1
    }
    if (wholeNumber) {
        return ExactFraction(numerator)
    }

    val denominatorRange = random.nextFromWeightedList(weightedRanges)
    val denominator = denominatorRange.random()
    return ExactFraction(numerator, denominator)
}

private fun generateError(): String {
    val messages = listOf(
        "Syntax error",
        "Divide by zero",
        "Number overflow exception",
        "Illegal operator",
        "Incorrect math"
    )

    val message = messages.random()

    return when (message) {
        "Number overflow exception" -> {
            val overflow = (Long.MIN_VALUE..Long.MAX_VALUE).random()
            "Number overflow exception on $overflow"
        }
        "Illegal operator" -> {
            val allOperators = operators + illegalOperators
            "Illegal operator: ${allOperators.random()}"
        }
        "Incorrect math" -> {
            val length = (1..12).random()
            val computation = generateComputation(length).joinToString("")
            "Invalid math: $computation"
        }
        else -> message
    }
}

