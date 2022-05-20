package com.example.trickcalculator.ui.history

import com.example.trickcalculator.compute.isOperator
import exactfraction.ExactFraction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.ext.nextBoolean
import com.example.trickcalculator.ext.nextFromWeightedList
import com.example.trickcalculator.utils.isNumber
import java.util.*
import kotlin.random.Random

private typealias MStringList = MutableList<String>

private const val maxCompLength = 8
private const val probabilityError = 0.1f

private val operators = listOf("+", "-", "x", "/", "^")
private val illegalOperators = listOf("!", "&", "|", "#", "$", "%")

private val random = Random(Date().time)

// generate a random history item
fun generateRandomHistoryItem(): HistoryItem {
    val showError = random.nextBoolean(probabilityError)

    val length = (1..maxCompLength).random()
    val computation = generateComputation(length)

    if (showError) {
        return HistoryItem(computation, generateError())
    }
    return HistoryItem(computation, generateResult())
}

// TODO: parens, syntax issues
// generate a random computation string, consisting of numbers and operators
// assumes length > 0
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

    val getExactFraction = {
        generateExactFraction(
            weightedRanges,
            probabilityWholeNumber,
            allowNegative = false
        )
    }

    for (i in (0 until totalLength)) {
        if (i % 2 == 0) {
            val ef = getExactFraction()
            computation.add(ef.toDecimalString(5))
        } else {
            computation.add(operators.random())
        }
    }

    val probabilityParens = 0.2f
    if (random.nextBoolean(probabilityParens)) {
        addParens(computation)
    }

    val probabilitySyntaxError = 0.1f
    if (random.nextBoolean(probabilitySyntaxError)) {
        addSyntaxError(computation)
    }

    return computation
}

// assumes input does not have any syntax errors
private fun addParens(computation: MStringList) {
    if (computation.size > 1) {
        // must start and end w/ a number, which means numbers are at even indices
        // insert before number
        var startIndex = (0 until computation.size - 1).random()
        if (isOperator(computation[startIndex], operators)) {
            startIndex--
        }
        computation.add(startIndex, "(")

        // now need an odd index, to insert after a number
        var endIndex = (startIndex + 1 until computation.size).random()
        if (isNumber(computation[endIndex])) {
            endIndex++
        }
        if (endIndex == startIndex + 2) {
            if (endIndex + 2 <= computation.size) {
                endIndex += 2
            }
        }
        computation.add(endIndex, ")")
    }
}

// if error count is 2 and 2 single parens are added, this may result in valid syntax
private fun addSyntaxError(computation: MStringList) {
    val errorType = listOf(
        "singleParen",
        "extraOperator",
        "emptyParens",
        "extraDecimal"
    ).random()

    when (errorType) {
        "singleParen" -> addSingleParen(computation)
        "extraOperator" -> addOperator(computation)
        "emptyParens" -> {
            val index = (0..computation.size).random()
            computation.add(index, "()")
        }
        "extraDecimal" -> addDecimal(computation)
    }
}

private fun addOperator(computation: MStringList) {
    val index = (0..computation.size).random()
    var operator = operators.random()
    if (index == 0 && operator == "-") {
        operator = listOf("+", "x", "/", "^").random()
    }

    if (index != 0 && index != computation.size) {
        val current = computation[index]
        val previous = computation[index - 1]
        val adjacentOperator = when {
            isOperator(current, operators) -> current
            isOperator(previous, operators) -> previous
            else -> ""
        }
        if (adjacentOperator == operator) {
            operator = operators.filter { it != operator }.random()
        }
    }
    computation.add(index, operator)
}

private fun addDecimal(computation: MStringList) {
    var index = (0..computation.size).random()
    if (index != computation.size) {
        val item = computation[index]
        if (isNumber(item) && item.indexOf(".") == -1) {
            index++
        }
    }
    computation.add(index, ".")
}

private fun addSingleParen(computation: MStringList) {
    var index = (0..computation.size).random()
    var paren = listOf("(", ")").random()
    val existingIndex = computation.indexOf(paren)
    if (existingIndex != -1) {
        val indices = listOf(
            Pair(existingIndex, 0.7f),
            Pair(index, 0.3f)
        )
        index = random.nextFromWeightedList(indices)
    }

    if (index != existingIndex && index != computation.size && isNumber(computation[index])) {
        paren = "("
    } else if (index != existingIndex) {
        paren = ")"
    }

    computation.add(index, paren)
}

// generate a result ExactFraction
private fun generateResult(): ExactFraction {
    val probabilityWholeNumber = 0.6f
    val weightedRanges = listOf(
        Pair((0 until 500), 0.2f),
        Pair((500 until 10000), 0.3f),
        Pair((10000 until Int.MAX_VALUE / 2), 0.25f),
        Pair((Int.MAX_VALUE / 2..Int.MAX_VALUE), 0.25f)
    )

    return generateExactFraction(weightedRanges, probabilityWholeNumber)
}

// generate ExactFraction given weights and probabilities
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

// generate a random error
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

