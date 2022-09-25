package xyz.lbres.trickcalculator.compute

import xyz.lbres.trickcalculator.runRandomTest
import xyz.lbres.trickcalculator.splitString
import xyz.lbres.trickcalculator.utils.isNumber
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

private val ops = listOf("+", "-", "x", "/")

fun runGenerateAndValidateComputeTextTests() {
    testValidateErrors()
    testBuildText()
    testBuildTextWithMods()
    testBuildTextWithShuffle()
}

private fun testValidateErrors() {
    // operator at start or end
    var text = listOf("+")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("-3")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("x3-4")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("3.0-(4+2)/")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    // operator at start or end in parens
    text = splitString("(+)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("(-3)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("1+(3-4/)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("3.0-(4+3(2+))")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    // empty parens
    text = splitString("()")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("3/(2-6())")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    // mismatched parens
    text = listOf("(")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = listOf(")")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString(")(")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("5-(0+(3-2)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("(1)+3)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("(1(8/9)+3(2+3)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("(1(8/9)+3(2+3)")
    assertSyntaxError {
        generateAndValidateComputeText(
            null,
            text,
            ops,
            null,
            applyParens = false,
            true,
            false
        )
    }

    // repeated ops
    text = splitString("1+-2")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("1+22/x3")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("5-6(0+(3--2))")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    // unknown char/invalid number
    text = listOf("a")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("0*3")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("1(8/9)+3*(2+3)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    val partialOps = listOf("+", "-")
    text = splitString("1(8/9)+3x(2+3)")
    assertSyntaxError {
        generateAndValidateComputeText(
            null,
            text,
            partialOps,
            null,
            true,
            true,
            false
        )
    }

    text = splitString("1(8/9)+-3.0.0x(2+3)")
    assertSyntaxError {
        generateAndValidateComputeText(
            null,
            text,
            partialOps,
            null,
            true,
            true,
            false
        )
    }

    text = splitString("0x--3")
    assertSyntaxError {
        generateAndValidateComputeText(
            null,
            text,
            partialOps,
            null,
            true,
            true,
            false
        )
    }

    // invalid/repeated decimals
    text = splitString("5.")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = listOf(".")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("5.0.1+10.2")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("5.0.+10.2")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("1+.45(..4)")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("1+.45(..4)")
    assertSyntaxError {
        generateAndValidateComputeText(
            null,
            text,
            ops,
            null,
            true,
            applyDecimals = false,
            false
        )
    }

    // blank spaces
    text = listOf("")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = listOf(" ")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = splitString("123+1 ")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    // multi-digit
    text = listOf("12")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = listOf("01")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = "1 + 10".split(' ')
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }

    text = listOf("1.2")
    assertSyntaxError { generateAndValidateComputeText(null, text, ops, null, true, true, false) }
}

private fun testBuildText() {
    // initial text is empty and initial value is not set
    var initialValue: ExactFraction? = null
    var text: StringList = listOf()
    var expected: StringList = listOf()
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // initial text is empty and initial value is set
    text = listOf()

    initialValue = ExactFraction.EIGHT
    expected = listOf(initialValue.toEFString())
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    initialValue = ExactFraction(-13, 3)
    expected = listOf(initialValue.toEFString())
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // initial text is not empty and initial value is not set
    initialValue = null

    // all single digit
    text = listOf("1")
    expected = listOf("1")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("1+2-3x7")
    expected = "1 + 2 - 3 x 7".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("1/(2x(3-1))")
    expected = "1 / ( 2 x ( 3 - 1 ) )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // multiple digits
    text = splitString("123")
    expected = listOf("123")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("12+106/23")
    expected = "12 + 106 / 23".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("(10056+23)-14+(13-(2))")
    expected = "( 10056 + 23 ) - 14 + ( 13 - ( 2 ) )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // decimals
    text = splitString("1.01")
    expected = listOf("1.01")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString(".5")
    expected = listOf(".5")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("1.5/55x2.6")
    expected = "1.5 / 55 x 2.6".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("5x(.723-(16+2)/4)")
    expected = "5 x ( .723 - ( 16 + 2 ) / 4 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // initial text is not empty and initial value is set
    initialValue = ExactFraction.ZERO
    text = splitString("+1")
    expected = listOf(initialValue.toEFString()) + "+ 1".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    initialValue = -ExactFraction.EIGHT
    text = splitString("+1x33.2")
    expected = listOf(initialValue.toEFString()) + "+ 1 x 33.2".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    initialValue = ExactFraction(1001, 57)
    text = splitString("/(.4-5x2)")
    expected = listOf(initialValue.toEFString()) + "/ ( .4 - 5 x 2 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    // add times around parens
    text = splitString("45(7-1)")
    expected = "45 x ( 7 - 1 )".split(" ")
    assertEquals(expected, generateAndValidateComputeText(null, text, ops, null, true, true, false))

    text = splitString("(7-1)45")
    expected = "( 7 - 1 ) x 45".split(" ")
    assertEquals(expected, generateAndValidateComputeText(null, text, ops, null, true, true, false))

    text = splitString("15+(12-3.3)(18.5)(2/3(1-10))")
    expected = "15 + ( 12 - 3.3 ) x ( 18.5 ) x ( 2 / 3 x ( 1 - 10 ) )".split(" ")
    assertEquals(expected, generateAndValidateComputeText(null, text, ops, null, true, true, false))

    initialValue = ExactFraction.HALF
    text = splitString("5+2(6-3.3)")
    expected = listOf(initialValue.toEFString()) + "x 5 + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )

    text = splitString("(5/10)+2(6-3.3)")
    expected = listOf(initialValue.toEFString()) + "x ( 5 / 10 ) + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(initialValue, text, ops, null, true, true, false)
    )
}

private fun testBuildTextWithMods() {
    // numbers order
    var text = splitString("5x(.723-(16+2)/4)")
    var order = (9 downTo 0).toList()
    var expected = "4 x ( .276 - ( 83 + 7 ) / 5 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, order, true, true, false)
    )

    text = listOf()
    expected = listOf()
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, order, true, true, false)
    )

    text = splitString("18+23/1.75")
    order = (0..9).toList()
    expected = "18 + 23 / 1.75".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, order, true, true, false)
    )

    text = splitString("1+2/4")
    order = listOf(4, 7, 6, 3, 9, 2, 8, 0, 1, 5)
    expected = splitString("7+6/9")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, order, true, true, false)
    )

    // not apply parens
    text = listOf()
    expected = listOf()
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, applyParens = false, true, false)
    )

    text = splitString("1+23.4/5")
    expected = "1 + 23.4 / 5".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, applyParens = false, true, false)
    )

    text = splitString("(1)")
    expected = listOf("1")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, applyParens = false, true, false)
    )

    text = splitString("4-(5x(7/8))")
    expected = splitString("4-5x7/8")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, applyParens = false, true, false)
    )

    text = splitString("(4-5)(7/8)") // times is added even when parens are stripped
    expected = splitString("4-5x7/8")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, applyParens = false, true, false)
    )

    // not apply decimals
    text = listOf()
    expected = listOf()
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, false, applyDecimals = false, false)
    )

    text = splitString("10000-805")
    expected = "10000 - 805".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, false, applyDecimals = false, false)
    )

    text = splitString("1.1-2")
    expected = "11 - 2".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, false, applyDecimals = false, false)
    )

    text = splitString("103.5x98.765-.99x3")
    expected = "1035 x 98765 - 99 x 3".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(null, text, ops, null, false, applyDecimals = false, false)
    )

    // combination
    text = splitString("10.3x(44+2.0)")
    order = listOf(4, 8, 1, 0, 5, 9, 3, 6, 2, 7)

    expected = "103 x 44 + 20".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(
            null,
            text,
            ops,
            null,
            applyParens = false,
            applyDecimals = false,
            false
        )
    )

    expected = "840 x ( 55 + 14 )".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(
            null,
            text,
            ops,
            order,
            applyParens = true,
            applyDecimals = false,
            false
        )
    )

    expected = "84.0 x 55 + 1.4".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(
            null,
            text,
            ops,
            order,
            applyParens = false,
            applyDecimals = true,
            false
        )
    )

    expected = "840 x 55 + 14".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(
            null,
            text,
            ops,
            order,
            applyParens = false,
            applyDecimals = false,
            false
        )
    )

    // with initial value
    var ef = -ExactFraction.THREE
    text = splitString("33(2+8.5)")
    expected = listOf(ef.toEFString()) + "x 33 x 2 + 8.5".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(ef, text, ops, null, applyParens = false, true, false)
    )

    ef = ExactFraction(2, 107)
    text = splitString("(14-4)-29.0-(0.5)(2)")
    order = (9 downTo 0).toList()
    expected = listOf(ef.toEFString()) + "x 85 - 5 - 709 - 94 x 7".split(" ")
    assertEquals(
        expected,
        generateAndValidateComputeText(
            ef,
            text,
            ops,
            order,
            applyParens = false,
            applyDecimals = false,
            false
        )
    )
}

private fun testBuildTextWithShuffle() {
    var text: StringList = listOf()
    repeat(20) {
        assertEquals(
            text,
            generateAndValidateComputeText(
                null,
                text,
                ops,
                null,
                true,
                true,
                true
            )
        )
    }

    // no modifications
    text = splitString("1+3(4-7.5)")
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
    val error = assertThrows(Exception::class.java) { function() }
    assertEquals("Syntax error", error.message)
}
