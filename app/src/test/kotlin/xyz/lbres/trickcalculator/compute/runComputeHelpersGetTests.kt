package xyz.lbres.trickcalculator.compute

import kotlin.test.assertEquals

fun runGetMatchingParenIndexTests() {
    var text = "( )".split(' ')
    var index = 0
    var expected = 1
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "( ( ) )".split(' ')
    index = 0
    expected = 3
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 1
    expected = 2
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "( ( ) ( ( ) ( ) ) )".split(' ')
    index = 0
    expected = 9
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 1
    expected = 2
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 3
    expected = 8
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 4
    expected = 5
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 6
    expected = 7
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "( 5 )".split(' ')
    index = 0
    expected = 2
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "4 ( 3 + 2 ) ( 5 )".split(' ')
    index = 1
    expected = 5
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 6
    expected = 8
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "( 1.0 ) + 5 + ( ( 5 - 6 ) + ( 1 ) )".split(' ')
    index = 0
    expected = 2
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 6
    expected = 16
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 7
    expected = 11
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 12
    expected = 15
    assertEquals(expected, getMatchingParenIndex(index, text))

    text = "( 5 ( 2 ) 3 )".split(' ')
    index = 0
    expected = 6
    assertEquals(expected, getMatchingParenIndex(index, text))

    index = 2
    expected = 4
    assertEquals(expected, getMatchingParenIndex(index, text))
}

fun runGetParseErrorMessageTests() {
    // no value to parse
    var expected = "Parse error"

    assertEquals(expected, getParseErrorMessage(null))
    assertEquals(expected, getParseErrorMessage(""))
    assertEquals(expected, getParseErrorMessage("\""))
    assertEquals(expected, getParseErrorMessage("hello"))
    assertEquals(expected, getParseErrorMessage("hello world\""))
    assertEquals(expected, getParseErrorMessage("\"'hello world'"))
    assertEquals(expected, getParseErrorMessage("'hello \" world'"))
    assertEquals(expected, getParseErrorMessage("For input string \"abc"))
    assertEquals(expected, getParseErrorMessage("For input string 123456\""))
    assertEquals(expected, getParseErrorMessage("For input string \"\""))

    // number
    val testNumberOverflowMessage: (String) -> Unit = { number ->
        val message = "For input string \"$number\""
        expected = "Number overflow on value $number"
        assertEquals(expected, getParseErrorMessage(message))
    }

    testNumberOverflowMessage("0")
    testNumberOverflowMessage("23")
    testNumberOverflowMessage("1000")
    testNumberOverflowMessage("-1000")
    testNumberOverflowMessage("100000000000000000000000")
    testNumberOverflowMessage("0.1")
    testNumberOverflowMessage("4.11111")
    testNumberOverflowMessage(".456")
    testNumberOverflowMessage("-.456")

    // non-number
    val testNonNumberSymbolMessage: (String) -> Unit = { symbol ->
        val message = "For input string \"$symbol\""
        expected = "Cannot parse symbol $symbol"
        assertEquals(expected, getParseErrorMessage(message))
    }

    testNonNumberSymbolMessage("(")
    testNonNumberSymbolMessage("hello world")
    testNonNumberSymbolMessage(".")
    testNonNumberSymbolMessage("-")
    testNonNumberSymbolMessage("1.1.")
    testNonNumberSymbolMessage("123.456.789")
}
