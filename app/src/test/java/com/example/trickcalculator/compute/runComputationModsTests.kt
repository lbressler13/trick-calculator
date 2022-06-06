package com.example.trickcalculator.compute

import com.example.trickcalculator.splitString
import com.example.trickcalculator.utils.StringList
import org.junit.Assert.*

fun runStripParensTests() {
    var computeText: StringList = listOf()
    var expected: StringList = listOf()
    assertEquals(expected, stripParens(computeText))

    // only parens

    // ()
    computeText = listOf("(", ")")
    expected = listOf()
    assertEquals(expected, stripParens(computeText))

    // ()()
    computeText = listOf("(", ")", "(", ")")
    expected = listOf()
    assertEquals(expected, stripParens(computeText))

    // (())
    computeText = listOf("(", "(", ")", ")")
    expected = listOf()
    assertEquals(expected, stripParens(computeText))

    // no parens

    // 5
    computeText = listOf("5")
    expected = listOf("5")
    assertEquals(expected, stripParens(computeText))

    // 5.135
    computeText = listOf("5.135")
    expected = listOf("5.135")
    assertEquals(expected, stripParens(computeText))

    // 6 x 12345 + .5678
    computeText = listOf("6", "x", "12345", "+", ".5678")
    expected = listOf("6", "x", "12345", "+", ".5678")
    assertEquals(expected, stripParens(computeText))

    // both

    // (5.333)
    computeText = listOf("(", "5.333", ")")
    expected = listOf("5.333")
    assertEquals(expected, stripParens(computeText))

    // (5.333 x 2)
    computeText = listOf("(", "5.333", "x", "2", ")")
    expected = listOf("5.333", "x", "2")
    assertEquals(expected, stripParens(computeText))

    // 4 - (5 x (7 / 8))
    computeText = listOf("4", "-", "(", "5", "x", "(", "7", "/", "8", ")", ")")
    expected = listOf("4", "-", "5", "x", "7", "/", "8")
    assertEquals(expected, stripParens(computeText))
}

fun runStripDecimalsTests() {
    var computeText: StringList = listOf()
    var expected: StringList = listOf()
    assertEquals(expected, stripDecimals(computeText))

    // no decimals

    computeText = listOf("5")
    expected = listOf("5")
    assertEquals(expected, stripDecimals(computeText))

    computeText = "( 2 - 3 )".split(' ')
    expected = "( 2 - 3 )".split(' ')
    assertEquals(expected, stripDecimals(computeText))

    // decimals
    computeText = listOf("5", ".", "0")
    expected = listOf("5", "0")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString("5.123")
    expected = splitString("5123")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString(".123")
    expected = splitString("123")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString("(0.123)")
    expected = splitString("(0123)")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString("5+1.234")
    expected = splitString("5+1234")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString(".345+6.78")
    expected = splitString("345+678")
    assertEquals(expected, stripDecimals(computeText))

    computeText = splitString("0.5+(1.3/90.12)")
    expected = splitString("05+(13/9012)")
    assertEquals(expected, stripDecimals(computeText))
}

fun runReplaceNumbersTests() {
    var text: StringList = listOf()
    var order = (9 downTo 0).toList()
    var expected: StringList = listOf()
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString("()")
    order = (9 downTo 0).toList()
    expected = splitString("()")
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString("1+2+4")
    order = (0..9).toList()
    expected = splitString("1+2+4")
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString("1357902468")
    order = listOf(4, 7, 6, 3, 9, 2, 8, 0, 1, 5)
    expected = splitString("7320546981")
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString("37+(45-74)x83")
    order = (9 downTo 0).toList()
    expected = splitString("62+(54-25)x16")
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString(".489")
    order = listOf(0, 1, 2, 3, 4, 5, 6, 7, 9, 8)
    expected = splitString(".498")
    assertEquals(expected, replaceNumbers(text, order))

    text = splitString("0.5x13-8.69")
    order = listOf(8, 3, 0, 6, 2, 9, 5, 4, 1, 7)
    expected = splitString("8.9x36-1.57")
    assertEquals(expected, replaceNumbers(text, order))

    text = "22 / ( 1 - 2 )".split(' ')
    order = (9 downTo 0).toList()
    expected = "22 / ( 8 - 7 )".split(' ')
    assertEquals(expected, replaceNumbers(text, order))
}

fun runAddMultToParensTests() {
    var text: StringList = listOf()
    var expected: StringList = listOf()
    assertEquals(expected, addMultToParens(text))

    text = "5 + 3".split(' ')
    expected = "5 + 3".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "2 x ( 5 + 3 )".split(' ')
    expected = "2 x ( 5 + 3 )".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "2 ( 5 )".split(' ')
    expected = "2 x ( 5 )".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "( 5 ) 2".split(' ')
    expected = "( 5 ) x 2".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "( 5 ) ( 2 )".split(' ')
    expected = "( 5 ) x ( 2 )".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "( 5 ) - ( 2 )".split(' ')
    expected = "( 5 ) - ( 2 )".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "( 5 - 3 ) ( 2 )".split(' ')
    expected = "( 5 - 3 ) x ( 2 )".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "3.10 ( 1 + 2 ) 5".split(' ')
    expected = "3.10 x ( 1 + 2 ) x 5".split(' ')
    assertEquals(expected, addMultToParens(text))

    text = "( 4 ( 5 + 2 ) + ( 7 ) ( 5 ( .1 ) ) 3 )".split(' ')
    expected = "( 4 x ( 5 + 2 ) + ( 7 ) x ( 5 x ( .1 ) ) x 3 )".split(' ')
    assertEquals(expected, addMultToParens(text))
}
