package xyz.lbres.trickcalculator.compute

import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.assertFailsWithMessage
import xyz.lbres.trickcalculator.splitString
import kotlin.test.assertEquals

fun runGenerateAndValidateComputeTextTests() {
    testValidateErrors()
    testBuildText()
    testBuildTextWithMods()
    testBuildTextWithShuffle()
    testBuildTextWithRandomSigns()
    testAllShuffling()
}

private fun testValidateErrors() {
    // operator at start or end
    assertSyntaxError { callGenerateAndValidate(null, listOf("+")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("/3")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("--3")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("x3-4")) }

    // operator at start or end in parens
    assertSyntaxError { callGenerateAndValidate(null, splitString("(+)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(/3)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(3-)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(-3-)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1+(3-4/)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("3.0-(4+3(2+))")) }

    // empty parens
    assertSyntaxError { callGenerateAndValidate(null, splitString("()")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("3/(2-6())")) }

    // mismatched parens
    assertSyntaxError { callGenerateAndValidate(null, splitString("(")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString(")")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString(")(")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5-(0+(3-2)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(1))+3")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(1(8/9)+3(2+3)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(1(8/9)+3(2+3)"), applyParens = false) }

    // invalid minus
    assertSyntaxError { callGenerateAndValidate(null, splitString("-")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("(-)")) }

    // repeated ops
    assertSyntaxError { callGenerateAndValidate(null, splitString("1+-2")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1+22/x3")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5-6(0+(3--2))")) }

    // unknown char/invalid number
    assertSyntaxError { callGenerateAndValidate(null, splitString("a")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("0X3")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1(8/9)+3*(2+3)")) }

    val partialOps = listOf("+", "-")
    assertSyntaxError { callGenerateAndValidate(null, splitString("1(8/9)+3x(2+3)"), ops = partialOps) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1(8/9)+-3.0.0x(2+3)"), ops = partialOps) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("0x--2"), ops = partialOps) }

    // invalid/repeated decimals
    assertSyntaxError { callGenerateAndValidate(null, splitString("5.")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5."), applyDecimals = false) }
    assertSyntaxError { callGenerateAndValidate(null, splitString(".")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("."), applyDecimals = false) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5.0.1+10.2")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5.0.1+10.2"), applyDecimals = false) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5.0.+10.2")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("5.0.+10.2"), applyDecimals = false) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1+.45(..4)")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1+.45(..4)"), applyDecimals = false) }

    // blank spaces
    assertSyntaxError { callGenerateAndValidate(null, splitString(" ")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("1-0\n")) }
    assertSyntaxError { callGenerateAndValidate(null, splitString("123+1 ")) }

    // multi-digit
    assertSyntaxError { callGenerateAndValidate(null, listOf("12")) }
    assertSyntaxError { callGenerateAndValidate(null, listOf("01")) }
    assertSyntaxError { callGenerateAndValidate(null, "1 + 10".split(' ')) }
    assertSyntaxError { callGenerateAndValidate(null, listOf("1.2")) }
    assertSyntaxError { callGenerateAndValidate(null, listOf("EF[1 1]")) }
}

private fun testBuildText() {
    // initial text is empty and initial value is not set
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList()))

    // initial text is empty and initial value is set
    assertEquals(listOf("EF[8 1]"), callGenerateAndValidate(ExactFraction.EIGHT, emptyList()))

    var initialValue = ExactFraction(-13, 3)
    assertEquals(listOf("EF[-13 3]"), callGenerateAndValidate(initialValue, emptyList()))

    // initial text is not empty and initial value is not set

    // all single digit
    assertEquals(listOf("1"), callGenerateAndValidate(null, listOf("1")))
    assertEquals(listOf("-1"), callGenerateAndValidate(null, listOf("-", "1")))

    var text = splitString("1+2-3x7")
    var expected = "1 + 2 - 3 x 7".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("1/(2x(3-1))")
    expected = "1 / ( 2 x ( 3 - 1 ) )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    // multiple digits
    assertEquals(listOf("123"), callGenerateAndValidate(null, splitString("123")))

    text = splitString("12+106/23")
    expected = "12 + 106 / 23".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("(10056+23)-14+(13-(2))")
    expected = "( 10056 + 23 ) - 14 + ( 13 - ( 2 ) )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("4+(-31-4)/5")
    expected = "4 + ( -31 - 4 ) / 5".split(' ')
    assertEquals(expected, callGenerateAndValidate(null, text))

    // decimals
    assertEquals(listOf("1.01"), callGenerateAndValidate(null, splitString("1.01")))
    assertEquals(listOf(".5"), callGenerateAndValidate(null, splitString(".5")))

    text = splitString("1.5/55x2.6")
    expected = "1.5 / 55 x 2.6".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("5x(.723-(16+2)/4)")
    expected = "5 x ( .723 - ( 16 + 2 ) / 4 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    // initial text is not empty and initial value is set
    expected = listOf("EF[0 1]", "+", "1")
    assertEquals(expected, callGenerateAndValidate(ExactFraction.ZERO, splitString("+1")))

    text = splitString("+1x33.2")
    expected = listOf("EF[-8 1]") + "+ 1 x 33.2".split(" ")
    assertEquals(expected, callGenerateAndValidate(-ExactFraction.EIGHT, text))

    initialValue = ExactFraction(1001, 57)
    text = splitString("/(.4-5x2)")
    expected = listOf("EF[1001 57]") + "/ ( .4 - 5 x 2 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(initialValue, text))

    expected = listOf("EF[1 1]") + "- 1 + 2".split(" ")
    assertEquals(expected, callGenerateAndValidate(ExactFraction.ONE, splitString("-1+2")))

    // add times around parens
    text = splitString("45(7-1)")
    expected = "45 x ( 7 - 1 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("(7-1)45")
    expected = "( 7 - 1 ) x 45".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("15+(12-3.3)(18.5)(2/3(1-10))")
    expected = "15 + ( 12 - 3.3 ) x ( 18.5 ) x ( 2 / 3 x ( 1 - 10 ) )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text))

    text = splitString("5+2(6-3.3)")
    expected = listOf("EF[1 2]") + "x 5 + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(ExactFraction.HALF, text))

    text = splitString("(5/10)+2(6-3.3)")
    expected = listOf("EF[1 2]") + "x ( 5 / 10 ) + 2 x ( 6 - 3.3 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(ExactFraction.HALF, text))

    text = splitString("(2(3)4)")
    expected = "( 2 x ( 3 ) x 4 )".split(' ')
    assertEquals(expected, callGenerateAndValidate(null, text))
}

private fun testBuildTextWithMods() {
    // numbers order
    var text = splitString("5x(.723-(16+2)/4)")
    var order = (9 downTo 0).toList()
    var expected = "4 x ( .276 - ( 83 + 7 ) / 5 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order))

    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), order = order))

    text = splitString("18+23/1.75")
    order = (0..9).toList()
    expected = "18 + 23 / 1.75".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order))

    text = splitString("1+2/4")
    order = listOf(4, 7, 6, 3, 9, 2, 8, 0, 1, 5)
    expected = splitString("7+6/9")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order))

    // not apply parens
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), applyParens = false))

    text = splitString("1+23.4/5")
    expected = "1 + 23.4 / 5".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, applyParens = false))

    assertEquals(listOf("1"), callGenerateAndValidate(null, splitString("(1)"), applyParens = false))

    text = splitString("4-(5x(7/8))")
    expected = splitString("4-5x7/8")
    assertEquals(expected, callGenerateAndValidate(null, text, applyParens = false))

    text = splitString("(4-5)(7/8)") // times is added even when parens are stripped
    expected = splitString("4-5x7/8")
    assertEquals(expected, callGenerateAndValidate(null, text, applyParens = false))

    // not apply decimals
    assertEquals(emptyList(), callGenerateAndValidate(null, emptyList(), applyDecimals = false))

    text = splitString("10000-805")
    expected = "10000 - 805".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, applyDecimals = false))

    text = splitString("1.1-2")
    expected = "11 - 2".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, applyDecimals = false))

    text = splitString("103.5x98.765-.99x3")
    expected = "1035 x 98765 - 99 x 3".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, applyDecimals = false))

    // combination
    text = splitString("10.3x(44+2.0)")
    order = listOf(4, 8, 1, 0, 5, 9, 3, 6, 2, 7)

    expected = "103 x 44 + 20".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = null, applyParens = false, applyDecimals = false))

    expected = "840 x ( 55 + 14 )".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order, applyParens = true, applyDecimals = false))

    expected = "84.0 x 55 + 1.4".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order, applyParens = false, applyDecimals = true))

    expected = "840 x 55 + 14".split(" ")
    assertEquals(expected, callGenerateAndValidate(null, text, order = order, applyParens = false, applyDecimals = false))

    // with initial value
    var ef = -ExactFraction.THREE
    text = splitString("33(2+8.5)")
    expected = listOf(ef.toEFString()) + "x 33 x 2 + 8.5".split(" ")
    assertEquals(expected, callGenerateAndValidate(ef, text, applyParens = false, applyDecimals = true))

    ef = ExactFraction(2, 107)
    text = splitString("(14-4)-29.0-(0.5)(2)")
    order = (9 downTo 0).toList()
    expected = listOf(ef.toEFString()) + "x 85 - 5 - 709 - 94 x 7".split(" ")
    assertEquals(expected, callGenerateAndValidate(ef, text, order = order, applyParens = false, applyDecimals = false))
}

/**
 * Assert that a syntax error is thrown
 */
private fun assertSyntaxError(function: () -> Unit) {
    assertFailsWithMessage("Syntax error", function)
}

/**
 * Call [generateAndValidateComputeText] with default setting values.
 * [initialValue] and [text] are required, but all settings values are optional and have defaults.
 */
fun callGenerateAndValidate(
    initialValue: ExactFraction?,
    text: StringList,
    ops: StringList = listOf("+", "-", "x", "/"),
    order: IntList? = null,
    applyParens: Boolean = true,
    applyDecimals: Boolean = true,
    randomizeSigns: Boolean = false,
    shuffleComputation: Boolean = false
): StringList {
    return generateAndValidateComputeText(initialValue, text, ops, order, applyParens, applyDecimals, randomizeSigns, shuffleComputation)
}
