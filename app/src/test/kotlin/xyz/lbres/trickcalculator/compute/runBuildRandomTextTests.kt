package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.exactfraction.checkIsEFString
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.runRandomTest
import xyz.lbres.trickcalculator.splitString
import xyz.lbres.trickcalculator.utils.isNumber
import kotlin.test.assertEquals

private val ops = listOf("+", "-", "x", "/")
private val opsType = "operator"
private val numType = "number"

fun testBuildTextWithShuffle() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), shuffleComputation = true))

    // no modifications
    var text = splitString("1+3(4-7.5)")
    var builtText = "1 + 3 x ( 4 - 7.5 )".split(' ')
    runSingleShuffledTest(text, builtText)

    // modifications
    text = splitString("5x(.723-(16+2)/4)")
    val order = (9 downTo 0).toList()

    builtText = "4 x ( .276 - ( 83 + 7 ) / 5 )".split(" ")
    runSingleShuffledTest(text, builtText, numbersOrder = order)

    builtText = "4 x .276 - 83 + 7 / 5".split(" ")
    runSingleShuffledTest(text, builtText, numbersOrder = order, applyParens = false)

    builtText = "4 x ( 276 - ( 83 + 7 ) / 5 )".split(" ")
    runSingleShuffledTest(text, builtText, numbersOrder = order, applyDecimals = false)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("33(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x 33 x ( 2 + 8.5 ) / .73".split(" ")
    runSingleShuffledTest(text, builtText, initialValue = ef)
}

fun testBuildTextWithRandomSigns() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), randomizeSigns = true))

    // no modifications
    var text = splitString("1+3.0004-4+6(-4+.32)")
    var builtText = "1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleRandomSignTest(text, builtText)

    text = splitString("-1+3.0004-4+6(-4+.32)")
    builtText = "-1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleRandomSignTest(text, builtText)

    // modifications
    text = splitString("5x(.723-(-16+2)/(-4))")
    val order = (9 downTo 0).toList()

    builtText = "4 x ( .276 - ( -83 + 7 ) / ( -5 ) )".split(" ")
    runSingleRandomSignTest(text, builtText, numbersOrder = order)

    builtText = "4 x .276 - -83 + 7 / -5".split(" ")
    runSingleRandomSignTest(text, builtText, numbersOrder = order, applyParens = false)

    builtText = "4 x ( 276 - ( -83 + 7 ) / ( -5 ) )".split(" ")
    runSingleRandomSignTest(text, builtText, numbersOrder = order, applyDecimals = false)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("x(-33)(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x ( -33 ) x ( 2 + 8.5 ) / .73".split(" ")
    runSingleRandomSignTest(text, builtText, initialValue = ef)
}

// shuffle computation + randomize signs
fun testAllShuffling() {
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), shuffleComputation = true, randomizeSigns = true))

    // no initial value
    var text = splitString("1+3.0004-4+6(-4+.32)")
    var builtText = "1 + 3.0004 - 4 + 6 x ( -4 + .32 )".split(' ')
    runSingleAllShufflingTest(text, builtText)

    // initial value
    val ef = -ExactFraction.THREE
    text = splitString("x(-33)(2+8.5)/.73")
    builtText = listOf(ef.toEFString()) + "x ( -33 ) x ( 2 + 8.5 ) / .73".split(" ")
    runSingleRandomSignTest(text, builtText, initialValue = ef)
}

/**
 * Run a single test where shuffleComputation setting is `true`.
 * [text] and [builtText] are required, but all other settings values are optional and have defaults.
 */
private fun runSingleShuffledTest(
    text: StringList,
    builtText: StringList,
    initialValue: ExactFraction? = null,
    numbersOrder: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true
) {
    val typeMapping = mapToTypes(builtText)
    val expectedSorted = builtText.sorted()

    val buildText = {
        val result = generateAndValidateComputeText(
            initialValue,
            text,
            ops,
            numbersOrder,
            applyParens,
            applyDecimals,
            shuffleComputation = true,
            randomizeSigns = false
        )

        assertEquals(expectedSorted, result.sorted()) // contains same values
        assertEquals(typeMapping, mapToTypes(result))

        result
    }

    runRandomTest(buildText) { it != builtText }
}

private fun runSingleRandomSignTest(
    text: StringList,
    builtText: StringList,
    initialValue: ExactFraction? = null,
    numbersOrder: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true
) {
    val absValues = mapToAbsoluteValues(builtText)

    val buildText = {
        val result = generateAndValidateComputeText(
            initialValue,
            text,
            ops,
            numbersOrder,
            applyParens,
            applyDecimals,
            false,
            randomizeSigns = true
        )

        assertEquals(absValues, mapToAbsoluteValues(result)) // contains same values

        result
    }

    runRandomTest(buildText) {
        val hasNegative = it.any { isNumber(it) && it.startsWith('-') }
        val hasPositive = it.any { isNumber(it) && !it.startsWith('-') }
        it != builtText && hasPositive && hasNegative
    }
}

private fun runSingleAllShufflingTest(
    text: StringList,
    builtText: StringList,
    initialValue: ExactFraction? = null,
    numbersOrder: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true
) {
    val typeMapping = mapToTypes(builtText)
    val absSorted = mapToAbsoluteValues(builtText).sorted()

    val buildText = {
        val result = generateAndValidateComputeText(
            initialValue,
            text,
            ops,
            numbersOrder,
            applyParens,
            applyDecimals,
            shuffleComputation = true,
            randomizeSigns = true
        )

        val resultAbsValues = mapToAbsoluteValues(result)
        assertEquals(absSorted, resultAbsValues.sorted()) // contains same values
        assertEquals(typeMapping, mapToTypes(result))

        result
    }

    runRandomTest(buildText) {
        val hasNegative = it.any { isNumber(it) && it.startsWith('-') }
        val hasPositive = it.any { isNumber(it) && !it.startsWith('-') }
        it != builtText && hasPositive && hasNegative
    }
}

private fun mapToTypes(text: StringList): StringList {
    return text.map {
        when {
            isOperator(it, ops) -> opsType
            isNumber(it) -> numType
            else -> it
        }
    }
}

private fun mapToAbsoluteValues(text: StringList): StringList {
    return text.map {
        when {
            checkIsEFString(it) && ExactFraction(it).isNegative() -> {
                val ef = ExactFraction(it)
                (-ef).toEFString()
            }
            isNumber(it) && it.startsWith('-') -> it.substring(1)
            else -> it
        }
    }
}
