package com.example.trickcalculator.compute

import org.junit.Assert.assertEquals

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
}

fun runGetParsingErrorTests() {
    // no value to parse
    var error: String? = null
    var expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = ""
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "\""
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "hello"
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "hello world\""
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "\"'hello world'"
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "'hello \" world'"
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "'hello \" world'"
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"abc"
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"\""
    expected = "Parse error"
    assertEquals(expected, getParsingError(error))

    // number
    error = "For input string: \"0\""
    expected = "Number overflow on value 0"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"23\""
    expected = "Number overflow on value 23"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"1000\""
    expected = "Number overflow on value 1000"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"-1000\""
    expected = "Number overflow on value -1000"
    assertEquals(expected, getParsingError(error))

    val veryBig = "100000000000000000000000"
    error = "For input string: \"$veryBig\""
    expected = "Number overflow on value $veryBig"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"0.1\""
    expected = "Number overflow on value 0.1"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"4.11111\""
    expected = "Number overflow on value 4.11111"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \".456\""
    expected = "Number overflow on value .456"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"-.456\""
    expected = "Number overflow on value -.456"
    assertEquals(expected, getParsingError(error))

    // non-number

    error = "For input string: \"(\""
    expected = "Cannot parse symbol ("
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"hello world\""
    expected = "Cannot parse symbol hello world"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \".\""
    expected = "Cannot parse symbol ."
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"-\""
    expected = "Cannot parse symbol -"
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"1.1.\""
    expected = "Cannot parse symbol 1.1."
    assertEquals(expected, getParsingError(error))

    error = "For input string: \"123.456.789\""
    expected = "Cannot parse symbol 123.456.789"
    assertEquals(expected, getParsingError(error))
}
