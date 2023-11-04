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

    var error = "For input string: \"abc"
    assertEquals(expected, getParseErrorMessage(error))

    error = "For input string: \"\""
    assertEquals(expected, getParseErrorMessage(error))

    // number
    val testNumberOverflow: (String) -> Unit = { number ->
        error = "For input string \"$number\""
        expected = "Number overflow on value $number"
        assertEquals(expected, getParseErrorMessage(error))
    }

    testNumberOverflow("0")
    testNumberOverflow("23")
    testNumberOverflow("1000")
    testNumberOverflow("-1000")
    testNumberOverflow("100000000000000000000000")
    testNumberOverflow("0.1")
    testNumberOverflow("4.11111")
    testNumberOverflow(".456")
    testNumberOverflow("-.456")

    // non-number
    val testNonNumberSymbol: (String) -> Unit = { symbol ->
        error = "For input string \"$symbol\""
        expected = "Cannot parse symbol $symbol"
        assertEquals(expected, getParseErrorMessage(error))
    }

    testNonNumberSymbol("(")
    testNonNumberSymbol("hello world")
    testNonNumberSymbol(".")
    testNonNumberSymbol("-")
    testNonNumberSymbol("1.1.")
    testNonNumberSymbol("123.456.789")
}
