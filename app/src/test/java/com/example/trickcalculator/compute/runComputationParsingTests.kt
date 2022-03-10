package com.example.trickcalculator.compute

import com.example.trickcalculator.bigfraction.BigFraction
import com.example.trickcalculator.bigfraction.toBF
import com.example.trickcalculator.bigfraction.toBigFraction
import com.example.trickcalculator.utils.OperatorFunction
import org.junit.Assert.*

private val plusMinus = listOf("+", "-")
private val timesDiv = listOf("x", "/")
private val performOp: OperatorFunction = { lval, rval, op ->
    when (op) {
        "+" -> lval + rval
        "-" -> lval - rval
        "x" -> lval * rval
        "/" -> lval / rval
        else -> throw Exception("Invalid operator $op")
    }
}
private val performSwapped: OperatorFunction = { lval, rval, op ->
    when (op) {
        "x" -> lval + rval
        "/" -> lval - rval
        "+" -> lval * rval
        "-" -> lval / rval
        else -> throw Exception("Invalid operator $op")
    }
}
private val performReduced: OperatorFunction = { lval, rval, op ->
    when (op) {
        "-" -> lval - rval
        "x" -> lval * rval
        "/" -> lval / rval
        else -> throw Exception("Invalid operator $op")
    }
}

// relatively light, b/c functionality should all be tested in fn-specific tests
fun runRunComputationTests() {
    // errors
    var text = "1 + 3 3".split(' ')
    var error = assertThrows(Exception::class.java) {
        runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), true, true)
    }
    assertEquals("Syntax error", error.message)

    text = "1 / 0".split(' ')
    error = assertThrows(ArithmeticException::class.java) {
        runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), true, true)
    }
    assertEquals("divide by zero", error.message)

    text = "1 + 0".split(' ')
    error = assertThrows(Exception::class.java) {
        runComputation(text, timesDiv, plusMinus, performReduced, (0..9).toList(), true, true)
    }
    assertEquals("Invalid operator +", error.message)

    // cannot test parsing and overflow errors b/c they should never happen

    // numbers order
    text = "1 / 0".split(' ')
    var nums = listOf(3, 8, 2, 6, 0, 1, 9, 7, 5, 4)
    var expected = BigFraction(8, 3)
    var result = runComputation(text, timesDiv, plusMinus, performOp, nums, true, true)
    assertEquals(expected, result)

    text = "2 x ( 3 + 4 ) / 6.9".split(' ')
    nums = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = BigFraction(33, 9)
    result = runComputation(text, timesDiv, plusMinus, performOp, nums, true, true)
    assertEquals(expected, result)

    // skip parens
    text = "2 ( 3 + 4 ) - 6 / ( 7 - 5 )".split(' ')
    expected = BigFraction(29, 7)
    result = runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), false, true)
    assertEquals(expected, result)

    text = "2 x ( 3 + 4 ) / ( 6.4 - .4 )".split(' ')
    nums = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = BigFraction(685, 38)
    result = runComputation(text, timesDiv, plusMinus, performOp, nums, false, true)
    assertEquals(expected, result)

    // skip decimals
    text = "0.5 x 2 + 7".split(' ')
    expected = 17.toBigFraction()
    result = runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), true, false)
    assertEquals(expected, result)

    text = "8.7 / ( 16 - 2 )".split(' ')
    nums = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = BigFraction(80, 51)
    result = runComputation(text, timesDiv, plusMinus, performOp, nums, true, false)
    assertEquals(expected, result)

    text = "1.1 + 2.2 + 3.3".split(' ')
    expected = 66.toBigFraction()
    result = runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), true, false)
    assertEquals(expected, result)

    // skip parens + decimals
    text = "8.7 / ( 16 - 2 )".split(' ')
    nums = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = BigFraction(28, 52)
    result = runComputation(text, timesDiv, plusMinus, performOp, nums, false, false)
    assertEquals(expected, result)

    // alternate ops
    text = "2 ( 3 / 4 ) - 6 + ( 7 x 5 )".split(' ')
    expected = BigFraction.ZERO
    result = runComputation(text, plusMinus, timesDiv, performSwapped, (0..9).toList(), true, true)
    assertEquals(expected, result)

    // normal
    text = "( 17 + ( 12 - 90 ) ) / 3".split(' ')
    expected = BigFraction(-61, 3)
    result = runComputation(text, timesDiv, plusMinus, performOp, (0..9).toList(), true, true)
    assertEquals(expected, result)
}

fun runParseTextTests() {
    // single value
    var text = listOf("3")
    var expected = BigFraction.THREE
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = listOf("-3.5")
    expected = BigFraction(-7, 2)
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    val long = "11111111111111111111111111144444444444444444444"
    text = listOf(long)
    expected = BigFraction(long)
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = listOf(BigFraction(17, 9).toBFString())
    expected = BigFraction(17, 9)
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    // individual ops
    text = "4 + 7".split(' ')
    expected = 11.toBigFraction()
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "4 - 7".split(' ')
    expected = (-3).toBigFraction()
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "4 x 7".split(' ')
    expected = 28.toBigFraction()
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "4 / 7".split(' ')
    expected = BigFraction(4, 7)
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "0 / 3".split(' ')
    expected = BigFraction.ZERO
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "0 x 3".split(' ')
    expected = BigFraction.ZERO
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "1 x 3".split(' ')
    expected = BigFraction.THREE
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "8888888888888888888888888888888888888888888 / 2".split(' ')
    expected = BigFraction("4444444444444444444444444444444444444444444")
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    // multiple ops + parens
    text = "5 + 3 x 2".split(' ')
    expected = 11.toBigFraction()
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "0402 / 2 + 3".split(' ')
    expected = 204.toBigFraction()
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "5 / ( 0.5 + 2 ) / ( 9 x ( 4 - 2 ) )".split(' ')
    expected = BigFraction(1, 9)
    assertEquals(expected, parseText(text, timesDiv, plusMinus, performOp))

    text = "( 2 + 7 ) / ( 0.5 - 1 / 2 )".split(' ')
    var error: Exception = assertThrows(ArithmeticException::class.java) {
        parseText(text, timesDiv, plusMinus, performOp)
    }
    assertEquals(error.message, "divide by zero")

    // alternate ops
    text = "5 - ( 0.5 x 2 ) - ( 9 + ( 4 / 2 ) )".split(' ')
    expected = BigFraction(1, 9)
    assertEquals(expected, parseText(text, plusMinus, timesDiv, performSwapped))

    text = "5 + 3 - 6".split(' ')
    expected = BigFraction.TWO
    assertEquals(expected, parseText(text, listOf(), plusMinus, performOp))

    text = "5 x 3 / 6".split(' ')
    expected = BigFraction(15, 6)
    assertEquals(expected, parseText(text, timesDiv, listOf(), performOp))

    text = "5 x 3 / 6 + 2".split(' ')
    error = assertThrows(Exception::class.java) {
        parseText(text, timesDiv, listOf(), performOp)
    }
    assertEquals(error.message, "Parse error")

    text = "5 x 3 / 6 + 2".split(' ')
    error = assertThrows(Exception::class.java) {
        parseText(text, timesDiv, plusMinus, performReduced)
    }
    assertEquals(error.message, "Invalid operator +")
}

// should not include parens
fun runParseSetOfOpsTests() {
    // unchanged
    var text = listOf("-1.00")
    var expected = listOf("-1.00")
    assertEquals(expected, parseSetOfOps(text, plusMinus, performOp))

    text = "1 x 3 / 17".split(' ')
    expected = "1 x 3 / 17".split(' ')
    assertEquals(expected, parseSetOfOps(text, plusMinus, performOp))

    text = "1 - 3 + 17".split(' ')
    expected = "1 - 3 + 17".split(' ')
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    // changed
    text = "1 - 3 + 17".split(' ')
    expected = listOf(15.toBF().toBFString())
    assertEquals(expected, parseSetOfOps(text, plusMinus, performOp))

    text = "1.3 x 2 / 10 x 20".split(' ')
    var bf = BigFraction("5.2").toBFString()
    expected = listOf(bf)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    text = "1.3 x 2 + 10 x 20".split(' ')
    bf = BigFraction("2.6").toBFString()
    var bf2 = BigFraction(200).toBFString()
    expected = listOf(bf, "+", bf2)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    text = "1000000000000 - 87 x 10000000000000000 + 33 / 11 + 6 x 40".split(' ')
    bf = BigFraction("870000000000000000").toBFString()
    bf2 = BigFraction.THREE.toBFString()
    val bf3 = BigFraction(240).toBFString()
    expected = listOf("1000000000000", "-", bf, "+", bf2, "+", bf3)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    val fakeOps = listOf("a", "b", "c")
    val performFake: OperatorFunction = { lval, rval, op ->
        when (op) {
            "a" -> lval + rval
            "b" -> rval - lval
            "c" -> rval / lval
            else -> BigFraction.ZERO
        }
    }

    text = "100 a 12 + 1.2 c 2.4 b 3".split(' ')
    bf = BigFraction(112).toBFString()
    bf2 = BigFraction.ONE.toBFString()
    expected = listOf(bf, "+", bf2)
    assertEquals(expected, parseSetOfOps(text, fakeOps, performFake))
}

fun runParseParensTests() {
    // no parens
    var text = listOf("3")
    var expected = listOf("3")
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    text = "3 + 2.0".split(' ')
    expected = "3 + 2.0".split(' ')
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    // parens
    text = "( 3.0 )".split(' ')
    var bf = BigFraction(3).toBFString()
    expected = listOf(bf)
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    text = "( 2 + 3 )".split(' ')
    bf = BigFraction(5).toBFString()
    expected = listOf(bf)
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    text = "5 x ( 3 - 1 )".split(' ')
    bf = BigFraction(2).toBFString()
    expected = listOf("5", "x", bf)
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    text = "( 9 / ( 6 / 2 ) x 3 x ( 2 + 5 ) ) - ( 7 / 8 )".split(' ')
    bf = BigFraction(63).toBFString()
    val bf2 = BigFraction(7, 8).toBFString()
    expected = listOf(bf, "-", bf2)
    assertEquals(expected, parseParens(text, timesDiv, plusMinus, performOp))

    text = "2 + ( 5 / 3 )".split(' ')
    bf = BigFraction(5, 3).toBFString()
    expected = listOf("2", "+", bf)
    assertEquals(expected, parseParens(text, plusMinus, timesDiv, performOp))
}
