package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.runTestWithRetry
import xyz.lbres.trickcalculator.splitString
import xyz.lbres.trickcalculator.utils.isNumber
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private val ops = listOf("+", "-", "x", "/")

@Suppress("BooleanLiteralArgument")
fun testBuildTextWithShuffle() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), shuffleComputation = true))

    // no modifications
    var text = splitString("1+3(4-7.5)")
    var builtText = "1 + 3 x ( 4 - 7.5 )".split(' ')
    runSingleRandomizationTest(text, builtText, true, false)

    // modifications
    text = splitString("5x(.723-(16+2)/4)")
    val order = (9 downTo 0).toList()

    builtText = "4 x ( .276 - ( 83 + 7 ) / 5 )".split(" ")
    runSingleRandomizationTest(text, builtText, true, false, numbersOrder = order)

    builtText = "4 x .276 - 83 + 7 / 5".split(" ")
    runSingleRandomizationTest(text, builtText, true, false, numbersOrder = order, applyParens = false)

    builtText = "4 x ( 276 - ( 83 + 7 ) / 5 )".split(" ")
    runSingleRandomizationTest(text, builtText, true, false, numbersOrder = order, applyDecimals = false)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("33(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x 33 x ( 2 + 8.5 ) / .73".split(" ")
    runSingleRandomizationTest(text, builtText, true, false, initialValue = ef)
}

@Suppress("BooleanLiteralArgument")
fun testBuildTextWithRandomSigns() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), randomizeSigns = true))

    // no modifications
    var text = splitString("1+3.0004-4+6(-4+.32)")
    var builtText = "1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleRandomizationTest(text, builtText, false, true)

    text = splitString("-1+3.0004-4+6(-4+.32)")
    builtText = "-1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleRandomizationTest(text, builtText, false, true)

    // modifications
    text = splitString("5x(.723-(-16+2)/(-4))")
    val order = (9 downTo 0).toList()

    builtText = "4 x ( .276 - ( -83 + 7 ) / ( -5 ) )".split(" ")
    runSingleRandomizationTest(text, builtText, false, true, numbersOrder = order)

    builtText = "4 x .276 - -83 + 7 / -5".split(" ")
    runSingleRandomizationTest(text, builtText, false, true, numbersOrder = order, applyParens = false)

    builtText = "4 x ( 276 - ( -83 + 7 ) / ( -5 ) )".split(" ")
    runSingleRandomizationTest(text, builtText, false, true, numbersOrder = order, applyDecimals = false)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("x(-33)(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x ( -33 ) x ( 2 + 8.5 ) / .73".split(" ")
    runSingleRandomizationTest(text, builtText, false, true, initialValue = ef)
}

// shuffle computation + randomize signs
@Suppress("BooleanLiteralArgument")
fun testAllShuffling() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), shuffleComputation = true, randomizeSigns = true))

    // no initial value
    var text = splitString("1+3.0004-4+6(-4+.32)")
    var builtText = "1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleRandomizationTest(text, builtText, true, true)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("x(-33)(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x ( -33 ) x ( 2 + 8.5 ) / .73".split(" ")
    runSingleRandomizationTest(text, builtText, true, true, initialValue = ef)
}

/**
 * Run a single text with shuffled/randomized values.
 * [text], [builtText], [shuffleComputation], and [randomizeSigns] are required,
 * but all other values are optional and have defaults.
 */
private fun runSingleRandomizationTest(
    text: StringList,
    builtText: StringList,
    shuffleComputation: Boolean,
    randomizeSigns: Boolean,
    initialValue: ExactFraction? = null,
    numbersOrder: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true,
) {
    val typeMapping = mapToTypes(builtText)
    val absValues = mapToAbsoluteValues(builtText)

    val buildText = {
        val result = generateAndValidateComputeText(
            initialValue,
            text,
            ops,
            numbersOrder,
            applyParens,
            applyDecimals,
            randomizeSigns,
            shuffleComputation,
        )

        assertEquals(typeMapping, mapToTypes(result))

        val resultAbsValues = mapToAbsoluteValues(result)
        when {
            shuffleComputation && randomizeSigns -> assertEquals(absValues.sorted(), resultAbsValues.sorted())
            shuffleComputation -> assertEquals(builtText.sorted(), result.sorted())
            randomizeSigns -> assertEquals(absValues, resultAbsValues)
        }

        result
    }

    val distinctElements = text.toSet()
    val distinctResults: MutableSet<StringList> = mutableSetOf()

    val minResults = when {
        distinctElements.size < 3 -> 1
        distinctElements.size < 5 -> 2
        distinctElements.size > 8 -> 4
        else -> 3
    }

    runTestWithRetry(tries = 20) {
        assertTrue {
            val result = buildText()
            distinctResults.add(result)
            val hasPositive = result.any { isNumber(it) && !it.startsWith('-') }
            val hasNegative = result.any { isNumber(it) && it.startsWith('-') }
            val validSigns = !randomizeSigns || (hasPositive && hasNegative)

            validSigns && result != builtText && distinctResults.size >= minResults
        }
    }
}

/**
 * Map a list of values to a new list where all numbers and operators are replaced with type labels
 */
private fun mapToTypes(text: StringList): StringList {
    return text.map {
        when {
            isOperator(it, ops) -> "operator"
            isNumber(it) -> "number"
            else -> it
        }
    }
}

/**
 * Map a list of values to a new list where negative values are stripped from all numbers, including EF-format strings
 */
private fun mapToAbsoluteValues(text: StringList): StringList {
    return text.map {
        when {
            ExactFraction.isEFString(it) && ExactFraction(it).isNegative() -> {
                val ef = ExactFraction(it)
                (-ef).toEFString()
            }
            isNumber(it) && it.startsWith('-') -> it.substring(1)
            else -> it
        }
    }
}
