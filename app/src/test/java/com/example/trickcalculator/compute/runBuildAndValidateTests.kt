package com.example.trickcalculator.compute

import com.example.trickcalculator.splitString
import com.example.trickcalculator.utils.StringList
import exactfraction.ExactFraction
import org.junit.Assert.*

private val ops = listOf("+", "-", "x", "/")

fun runBuildAndValidateTests() {
    testValidateErrors()
    testBuildText()
}

private fun testValidateErrors() {
    // operator at start or end
    var text = listOf("+")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("-3")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("x3-4")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("3.0-(4+2)/")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // operator at start or end in parens
    text = splitString("(+)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("(-3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("1+(3-4/)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("3.0-(4+3(2+))")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // empty parens
    text = splitString("()")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("3/(2-6())")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // mismatched parens
    text = listOf("(")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = listOf(")")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString(")(")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("5-(0+(3-2)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("(1)+3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("(1(8/9)+3(2+3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // repeated ops
    text = splitString("1+-2")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("1+22/x3")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("5-6(0+(3--2))")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // unknown char/invalid number
    text = listOf("a")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("0*3")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("1(8/9)+3*(2+3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    val partialOps = listOf("+", "-")
    text = splitString("1(8/9)+3x(2+3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, partialOps) }

    text = splitString("1(8/9)+-3.0.0x(2+3)")
    assertSyntaxError { buildAndValidateComputeText(null, text, partialOps) }

    text = splitString("0x--3")
    assertSyntaxError { buildAndValidateComputeText(null, text, partialOps) }

    // invalid/repeated decimals
    text = splitString("5.")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = listOf(".")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("5.0.1+10.2")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("5.0.+10.2")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("1+.45(..4)")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // blank spaces
    text = listOf("")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = listOf(" ")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = splitString("123+1 ")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // multi-digit
    text = listOf("12")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = listOf("01")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = "1 + 10".split(' ')
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    text = listOf("1.2")
    assertSyntaxError { buildAndValidateComputeText(null, text, ops) }

    // number at start with initial value set
    val initialsValues = listOf(ExactFraction.ZERO, ExactFraction(-1234), ExactFraction(17, 56))
    val numbersFirstText = listOf(listOf("1"), "0.56".split(""), "(2-3)x1".split(""))
    for (initialValue in initialsValues) {
        for (t in numbersFirstText) {
            assertSyntaxError { buildAndValidateComputeText(initialValue, t, ops) }
        }
    }
}

private fun testBuildText() {
    // initial text is empty and initial value is not set
    var initialValue: ExactFraction? = null
    var text: StringList = listOf()
    var expected: StringList = listOf()
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    // initial text is empty and initial value is set
    text = listOf()

    initialValue = ExactFraction.EIGHT
    expected = listOf(initialValue.toEFString())
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    initialValue = ExactFraction(-13, 3)
    expected = listOf(initialValue.toEFString())
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    // initial text is not empty and initial value is not set
    initialValue = null

    // all single digit
    text = listOf("1")
    expected = listOf("1")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("1+2-3x7")
    expected = "1 + 2 - 3 x 7".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("1/(2x(3-1))")
    expected = "1 / ( 2 x ( 3 - 1 ) )".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    // multiple digits
    text = splitString("123")
    expected = listOf("123")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("12+106/23")
    expected = "12 + 106 / 23".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("(10056+23)-14+(13-(2))")
    expected = "( 10056 + 23 ) - 14 + ( 13 - ( 2 ) )".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    // decimals
    text = splitString("1.01")
    expected = listOf("1.01")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString(".5")
    expected = listOf(".5")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("1.5/55x2.6")
    expected = "1.5 / 55 x 2.6".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    text = splitString("5x(.723-(16+2)/4)")
    expected = "5 x ( .723 - ( 16 + 2 ) / 4 )".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    // initial text is not empty and initial value is set
    initialValue = ExactFraction.ZERO
    text = splitString("+1")
    expected = listOf(initialValue.toEFString()) + "+ 1".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    initialValue = -ExactFraction.EIGHT
    text = splitString("+1x33.2")
    expected = listOf(initialValue.toEFString()) + "+ 1 x 33.2".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))

    initialValue = ExactFraction(1001, 57)
    text = splitString("/(.4-5x2)")
    expected = listOf(initialValue.toEFString()) + "/ ( .4 - 5 x 2 )".split(" ")
    assertEquals(expected, buildAndValidateComputeText(initialValue, text, ops))
}

private fun assertSyntaxError(function: () -> Unit) {
    val error = assertThrows(Exception::class.java) { function() }
    assertEquals("Syntax error", error.message)
}

