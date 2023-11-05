package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.runRandomTest
import xyz.lbres.trickcalculator.splitString
import xyz.lbres.trickcalculator.utils.isNumber
import kotlin.test.assertEquals
import kotlin.test.assertFails

private val ops = listOf("+", "-", "x", "/")

fun runGenerateAndValidateComputeTextTests() {
    testValidateErrors()
    testBuildText()
    testBuildTextWithMods()
    testBuildTextWithShuffle()
}

private fun testBuildText() {
    // initial text is empty and initial value is not set
    assertEquals(emptyList(), buildTextWithDefaults(null, emptyList()))

    // initial text is empty and initial value is set
    assertEquals(listOf("EF[8 1]"), buildTextWithDefaults(ExactFraction.EIGHT, emptyList()))

    var initialValue = ExactFraction(-13, 3)
    assertEquals(listOf("EF[-13 3]"), buildTextWithDefaults(initialValue, emptyList()))

    // initial text is not empty and initial value is not set

    // all single digit
    assertEquals(listOf("1"), buildTextWithDefaults(null, listOf("1")))
    assertEquals(listOf("-1"), buildTextWithDefaults(null, listOf("-", "1")))

    var text = splitString("1+2-3x7")
    var expected = "1 + 2 - 3 x 7".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("1/(2x(3-1))")
    expected = "1 / ( 2 x ( 3 - 1 ) )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    // multiple digits
    assertEquals(listOf("123"), buildTextWithDefaults(null, splitString("123")))

    text = splitString("12+106/23")
    expected = "12 + 106 / 23".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("(10056+23)-14+(13-(2))")
    expected = "( 10056 + 23 ) - 14 + ( 13 - ( 2 ) )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("4+(-31-4)/5")
    expected = "4 + ( -31 - 4 ) / 5".split(' ')
    assertEquals(expected, buildTextWithDefaults(null, text))

    // decimals
    assertEquals(listOf("1.01"), buildTextWithDefaults(null, splitString("1.01")))
    assertEquals(listOf(".5"), buildTextWithDefaults(null, splitString(".5")))

    text = splitString("1.5/55x2.6")
    expected = "1.5 / 55 x 2.6".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("5x(.723-(16+2)/4)")
    expected = "5 x ( .723 - ( 16 + 2 ) / 4 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    // initial text is not empty and initial value is set
    expected = listOf("EF[0 1]", "+", "1")
    assertEquals(expected, buildTextWithDefaults(ExactFraction.ZERO, splitString("+1")))

    text = splitString("+1x33.2")
    expected = listOf("EF[-8 1]") + "+ 1 x 33.2".split(" ")
    assertEquals(expected, buildTextWithDefaults(-ExactFraction.EIGHT, text))

    initialValue = ExactFraction(1001, 57)
    text = splitString("/(.4-5x2)")
    expected = listOf("EF[1001 57]") + "/ ( .4 - 5 x 2 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(initialValue, text))

    expected = listOf("EF[1 1]") + "- 1 + 2".split(" ")
    assertEquals(expected, buildTextWithDefaults(ExactFraction.ONE, splitString("-1+2")))

    // add times around parens
    text = splitString("45(7-1)")
    expected = "45 x ( 7 - 1 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("(7-1)45")
    expected = "( 7 - 1 ) x 45".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("15+(12-3.3)(18.5)(2/3(1-10))")
    expected = "15 + ( 12 - 3.3 ) x ( 18.5 ) x ( 2 / 3 x ( 1 - 10 ) )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text))

    text = splitString("5+2(6-3.3)")
    expected = listOf("EF[1 2]") + "x 5 + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(ExactFraction.HALF, text))

    text = splitString("(5/10)+2(6-3.3)")
    expected = listOf("EF[1 2]") + "x ( 5 / 10 ) + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(ExactFraction.HALF, text))

    text = splitString("(2(3)4)")
    expected = "( 2 x ( 3 ) x 4 )".split(' ')
    assertEquals(expected, buildTextWithDefaults(null, text))
}

private fun testBuildTextWithMods() {
    // numbers order
    var text = splitString("5x(.723-(16+2)/4)")
    var order = (9 downTo 0).toList()
    var expected = "4 x ( .276 - ( 83 + 7 ) / 5 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order))

    assertEquals(emptyList(), buildTextWithDefaults(null, emptyList(), order = order))

    text = splitString("18+23/1.75")
    order = (0..9).toList()
    expected = "18 + 23 / 1.75".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order))

    text = splitString("1+2/4")
    order = listOf(4, 7, 6, 3, 9, 2, 8, 0, 1, 5)
    expected = splitString("7+6/9")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order))

    // not apply parens
    assertEquals(emptyList(), buildTextWithDefaults(null, emptyList(), applyParens = false))

    text = splitString("1+23.4/5")
    expected = "1 + 23.4 / 5".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, applyParens = false))

    assertEquals(listOf("1"), buildTextWithDefaults(null, splitString("(1)"), applyParens = false))

    text = splitString("4-(5x(7/8))")
    expected = splitString("4-5x7/8")
    assertEquals(expected, buildTextWithDefaults(null, text, applyParens = false))

    text = splitString("(4-5)(7/8)") // times is added even when parens are stripped
    expected = splitString("4-5x7/8")
    assertEquals(expected, buildTextWithDefaults(null, text, applyParens = false))

    // not apply decimals
    assertEquals(emptyList(), buildTextWithDefaults(null, emptyList(), applyDecimals = false))

    text = splitString("10000-805")
    expected = "10000 - 805".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, applyDecimals = false))

    text = splitString("1.1-2")
    expected = "11 - 2".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, applyDecimals = false))

    text = splitString("103.5x98.765-.99x3")
    expected = "1035 x 98765 - 99 x 3".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, applyDecimals = false))

    // combination
    text = splitString("10.3x(44+2.0)")
    order = listOf(4, 8, 1, 0, 5, 9, 3, 6, 2, 7)

    expected = "103 x 44 + 20".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, applyParens = false, applyDecimals = false))

    expected = "840 x ( 55 + 14 )".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order, applyParens = true, applyDecimals = false))

    expected = "84.0 x 55 + 1.4".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order, applyParens = false, applyDecimals = true))

    expected = "840 x 55 + 14".split(" ")
    assertEquals(expected, buildTextWithDefaults(null, text, order = order, applyParens = false, applyDecimals = false))

    // with initial value
    var ef = -ExactFraction.THREE
    text = splitString("33(2+8.5)")
    expected = listOf(ef.toEFString()) + "x 33 x 2 + 8.5".split(" ")
    assertEquals(expected, buildTextWithDefaults(ef, text, applyParens = false, applyDecimals = true))

    ef = ExactFraction(2, 107)
    text = splitString("(14-4)-29.0-(0.5)(2)")
    order = (9 downTo 0).toList()
    expected = listOf(ef.toEFString()) + "x 85 - 5 - 709 - 94 x 7".split(" ")
    assertEquals(expected, buildTextWithDefaults(ef, text, order = order, applyParens = false, applyDecimals = false))
}

private fun testValidateErrors() {
    // operator at start or end
    assertSyntaxError { buildTextWithDefaults(null, listOf("+")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("/3")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("--3")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("x3-4")) }

    // operator at start or end in parens
    assertSyntaxError { buildTextWithDefaults(null, splitString("(+)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(/3)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(3-)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(-3-)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1+(3-4/)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("3.0-(4+3(2+))")) }

    // empty parens
    assertSyntaxError { buildTextWithDefaults(null, splitString("()")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("3/(2-6())")) }

    // mismatched parens
    assertSyntaxError { buildTextWithDefaults(null, splitString("(")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString(")")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString(")(")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("5-(0+(3-2)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(1))+3")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(1(8/9)+3(2+3)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(1(8/9)+3(2+3)"), applyParens = false) }

    // invalid minus
    assertSyntaxError { buildTextWithDefaults(null, splitString("-")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("(-)")) }

    // repeated ops
    assertSyntaxError { buildTextWithDefaults(null, splitString("1+-2")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1+22/x3")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("5-6(0+(3--2))")) }

    // unknown char/invalid number
    assertSyntaxError { buildTextWithDefaults(null, splitString("a")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("0X3")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1(8/9)+3*(2+3)")) }

    val partialOps = listOf("+", "-")
    assertSyntaxError { buildTextWithDefaults(null, splitString("1(8/9)+3x(2+3)"), ops = partialOps) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1(8/9)+-3.0.0x(2+3)"), ops = partialOps) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("0x--2"), ops = partialOps) }

    // invalid/repeated decimals
    assertSyntaxError { buildTextWithDefaults(null, splitString("5.")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("5."), applyDecimals = false) }
    assertSyntaxError { buildTextWithDefaults(null, splitString(".")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("5.0.1+10.2")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("5.0.+10.2")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1+.45(..4)")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1+.45(..4)"), applyDecimals = false) }

    // blank spaces
    assertSyntaxError { buildTextWithDefaults(null, splitString(" ")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("1-0\n")) }
    assertSyntaxError { buildTextWithDefaults(null, splitString("123+1 ")) }

    // multi-digit
    assertSyntaxError { buildTextWithDefaults(null, listOf("12")) }
    assertSyntaxError { buildTextWithDefaults(null, listOf("01")) }
    assertSyntaxError { buildTextWithDefaults(null, "1 + 10".split(' ')) }
    assertSyntaxError { buildTextWithDefaults(null, listOf("1.2")) }
}

private fun testBuildTextWithShuffle() {
    assertEquals(emptyList(), buildTextWithDefaults(null, emptyList(), shuffleComputation = true))

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

private fun runSingleShuffledTest(
    text: StringList,
    builtText: StringList,
    initialValue: ExactFraction? = null,
    numbersOrder: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true
) {
    val opsType = "operator"
    val numType = "number"

    val mapping = builtText.map {
        when {
            isOperator(it, ops) -> opsType
            isNumber(it) -> numType
            else -> it
        }
    }

    val expectedSorted = builtText.sorted()

    val buildText = {
        val result = generateAndValidateComputeText(
            initialValue,
            text,
            ops,
            numbersOrder,
            applyParens,
            applyDecimals,
            shuffleComputation = true
        )

        assertEquals(expectedSorted, result.sorted()) // contains same values

        mapping.forEachIndexed { index, expectedType ->
            val newValue = result[index]
            when {
                isOperator(newValue, ops) -> assertEquals(expectedType, opsType)
                isNumber(newValue) -> assertEquals(expectedType, numType)
                else -> assertEquals(expectedType, newValue)
            }
        }

        result
    }

    val checkShuffled: (StringList) -> Boolean = { it != builtText }
    runRandomTest(buildText, checkShuffled)
}

private fun assertSyntaxError(function: () -> Unit) {
    val error = assertFails { function() }
    assertEquals("Syntax error", error.message)
}

fun buildTextWithDefaults(
    initialValue: ExactFraction?,
    text: StringList,
    ops: StringList = listOf("+", "-", "x", "/"),
    order: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true,
    shuffleComputation: Boolean = false
): StringList {
    return generateAndValidateComputeText(initialValue, text, ops, order, applyParens, applyDecimals, shuffleComputation)
}
