package com.example.trickcalculator.compute

import com.example.trickcalculator.assertDivByZero
import com.example.trickcalculator.splitString
import com.example.trickcalculator.utils.StringList
import exactfraction.ExactFraction
import org.junit.Assert.*
//
//fun runStripParensTests() {
//    var computeText: StringList = listOf()
//    var expected: StringList = listOf()
//    assertEquals(expected, stripParens(computeText))
//
//    // only parens
//
//    // ()
//    computeText = listOf("(", ")")
//    expected = listOf()
//    assertEquals(expected, stripParens(computeText))
//
//    // ()()
//    computeText = listOf("(", ")", "(", ")")
//    expected = listOf()
//    assertEquals(expected, stripParens(computeText))
//
//    // (())
//    computeText = listOf("(", "(", ")", ")")
//    expected = listOf()
//    assertEquals(expected, stripParens(computeText))
//
//    // no parens
//
//    // 5
//    computeText = listOf("5")
//    expected = listOf("5")
//    assertEquals(expected, stripParens(computeText))
//
//    // 5.135
//    computeText = listOf("5.135")
//    expected = listOf("5.135")
//    assertEquals(expected, stripParens(computeText))
//
//    // 6 x 12345 + .5678
//    computeText = listOf("6", "x", "12345", "+", ".5678")
//    expected = listOf("6", "x", "12345", "+", ".5678")
//    assertEquals(expected, stripParens(computeText))
//
//    // both
//
//    // (5.333)
//    computeText = listOf("(", "5.333", ")")
//    expected = listOf("5.333")
//    assertEquals(expected, stripParens(computeText))
//
//    // (5.333 x 2)
//    computeText = listOf("(", "5.333", "x", "2", ")")
//    expected = listOf("5.333", "x", "2")
//    assertEquals(expected, stripParens(computeText))
//
//    // 4 - (5 x (7 / 8))
//    computeText = listOf("4", "-", "(", "5", "x", "(", "7", "/", "8", ")", ")")
//    expected = listOf("4", "-", "5", "x", "7", "/", "8")
//    assertEquals(expected, stripParens(computeText))
//}
//
//fun runStripDecimalsTests() {
//    var computeText: StringList = listOf()
//    var expected: StringList = listOf()
//    assertEquals(expected, stripDecimals(computeText))
//
//    // no decimals
//
//    computeText = listOf("5")
//    expected = listOf("5")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString("(2-3)")
//    expected = splitString("(2-3)")
//    assertEquals(expected, stripDecimals(computeText))
//
//    // decimals
//    computeText = listOf("5", ".", "0")
//    expected = listOf("5", "0")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString("5.123")
//    expected = splitString("5123")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString(".123")
//    expected = splitString("123")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString("(0.123)")
//    expected = splitString("(0123)")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString("5+1.234")
//    expected = splitString("5+1234")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString(".345+6.78")
//    expected = splitString("345+678")
//    assertEquals(expected, stripDecimals(computeText))
//
//    computeText = splitString("0.5+(1.3/90.12)")
//    expected = splitString("05+(13/9012)")
//    assertEquals(expected, stripDecimals(computeText))
//}
//
//fun runReplaceNumbersTests() {
//    var text: StringList = listOf()
//    var order = (9 downTo 0).toList()
//    var expected: Pair<StringList, ExactFraction?> = Pair(listOf(), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//
//    text = splitString("()")
//    order = (9 downTo 0).toList()
//    expected = Pair(splitString("()"), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//
//    var initialValue: ExactFraction = ExactFraction.THREE
//    text = listOf()
//    order = (9 downTo 0).toList()
//    expected = Pair(listOf(), ExactFraction(6, 8))
//    assertEquals(expected, replaceNumbers(initialValue, text, order))
//
//    initialValue = ExactFraction(-103, 27)
//    text = listOf()
//    order = (9 downTo 0).toList()
//    expected = Pair(listOf(), ExactFraction(-896, 72))
//    assertEquals(expected, replaceNumbers(initialValue, text, order))
//
//    initialValue = ExactFraction.HALF
//    text = listOf()
//    order = listOf(1, 2, 0, 3, 4, 5, 6, 7, 8, 9)
//    assertDivByZero { replaceNumbers(initialValue, text, order) }
//
//    initialValue = ExactFraction.FIVE
//    text = splitString("1+2+4")
//    order = (0..9).toList()
//    expected = Pair(splitString("1+2+4"), ExactFraction.FIVE)
//    assertEquals(expected, replaceNumbers(initialValue, text, order))
//
//    text = splitString("1357902468")
//    order = listOf(4, 7, 6, 3, 9, 2, 8, 0, 1, 5)
//    expected = Pair(splitString("7320546981"), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//
//    text = splitString("37+(45-74)x83")
//    order = (9 downTo 0).toList()
//    expected = Pair(splitString("62+(54-25)x16"), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//
//    text = splitString(".489")
//    order = listOf(0, 1, 2, 3, 4, 5, 6, 7, 9, 8)
//    expected = Pair(splitString(".498"), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//
//    initialValue = ExactFraction(-289, 361)
//    text = splitString("0.5x13-8.69")
//    order = listOf(8, 3, 0, 6, 2, 9, 5, 4, 1, 7)
//    expected = Pair(splitString("8.9x36-1.57"), ExactFraction(-17, 653))
//    assertEquals(expected, replaceNumbers(initialValue, text, order))
//
//    text = "22 / ( 1 - 2 )".split(' ')
//    order = (9 downTo 0).toList()
//    expected = Pair("22 / ( 8 - 7 )".split(' '), null)
//    assertEquals(expected, replaceNumbers(null, text, order))
//}
//
//fun runAddMultToParensTests() {
//    // empty string
//    var text: StringList = listOf()
//    var expected: StringList = listOf()
//    assertEquals(expected, addMultToParens(null, text))
//
//    // without exact fraction
//    text = splitString("5+3")
//    expected = splitString("5+3")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("2x(5+3)")
//    expected = splitString("2x(5+3)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("2(5)")
//    expected = splitString("2x(5)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("(5)2")
//    expected = splitString("(5)x2")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("(5)(2)")
//    expected = splitString("(5)x(2)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("(5)-(2)")
//    expected = splitString("(5)-(2)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("(5-3)(2)")
//    expected = splitString("(5-3)x(2)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("3.10(1+2)5")
//    expected = splitString("3.10x(1+2)x5")
//    assertEquals(expected, addMultToParens(null, text))
//
//    text = splitString("(4(5+2)+(7)(5(.1))3)")
//    expected = splitString("(4x(5+2)+(7)x(5x(.1))x3)")
//    assertEquals(expected, addMultToParens(null, text))
//
//    // with exact fraction
//    var ef = ExactFraction(-14, 33)
//
//    text = listOf()
//    expected = listOf()
//    assertEquals(expected, addMultToParens(ef, text))
//
//    text = splitString("5(1-3)/2")
//    expected = splitString("x5x(1-3)/2")
//    assertEquals(expected, addMultToParens(ef, text))
//
//    text = splitString("+5(1-3)/2")
//    expected = splitString("+5x(1-3)/2")
//    assertEquals(expected, addMultToParens(ef, text))
//
//    text = splitString("^5(1-3)/2")
//    expected = splitString("^5x(1-3)/2")
//    assertEquals(expected, addMultToParens(ef, text))
//
//    text = splitString("(5-2)(2.5/3)")
//    expected = splitString("x(5-2)x(2.5/3)")
//    assertEquals(expected, addMultToParens(ef, text))
//
//    ef = ExactFraction.ZERO
//    text = splitString("(5-2)(2.5/3)")
//    expected = splitString("x(5-2)x(2.5/3)")
//    assertEquals(expected, addMultToParens(ef, text))
//}
