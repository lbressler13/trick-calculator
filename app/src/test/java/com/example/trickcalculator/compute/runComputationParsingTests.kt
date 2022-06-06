package com.example.trickcalculator.compute

import com.example.trickcalculator.assertDivByZero
import com.example.trickcalculator.splitString
import exactfraction.ExactFraction
import com.example.trickcalculator.utils.OperatorFunction
import org.junit.Assert.*
import exactfraction.toExactFraction

private val exp = listOf("^")
private val plusMinus = listOf("+", "-")
private val timesDiv = listOf("x", "/")
private val allOps = listOf(exp, timesDiv, plusMinus)
private val performOp: OperatorFunction = { lval, rval, op ->
    when (op) {
        "+" -> lval + rval
        "-" -> lval - rval
        "x" -> lval * rval
        "/" -> lval / rval
        "^" -> lval.pow(rval)
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
    var text = splitString("1+3-")
    var error = assertThrows(Exception::class.java) {
        runComputation(null, text, allOps, performOp, (0..9).toList(), true, true)
    }
    assertEquals("Syntax error", error.message)

    text = splitString("1/0")
    assertDivByZero {
        runComputation(null, text, allOps, performOp, (0..9).toList(), true, true)
    }

    var initialValue = ExactFraction.HALF
    text = listOf()
    val divZeroOrder = listOf(1, 2, 0, 3, 4, 5, 6, 7, 8, 9)
    assertDivByZero {
        runComputation(initialValue, text, allOps, performOp, divZeroOrder, true, true)
    }

    initialValue = ExactFraction.EIGHT
    text = splitString("/0")
    assertDivByZero {
        runComputation(initialValue, text, allOps, performOp, (0..9).toList(), true, true)
    }

    text = splitString("1+0")
    error = assertThrows(Exception::class.java) {
        runComputation(null, text, allOps, performReduced, (0..9).toList(), true, true)
    }
    assertEquals("Invalid operator +", error.message)

    // cannot test parsing and overflow errors b/c they should never happen

    // numbers order
    text = splitString("1/0")
    var nums = listOf(3, 8, 2, 6, 0, 1, 9, 7, 5, 4)
    var expected = ExactFraction(8, 3)
    var result = runComputation(null, text, allOps, performOp, nums, true, true)
    assertEquals(expected, result)

    text = splitString("2x(3+4)/6.9")
    nums = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = ExactFraction(33, 9)
    result = runComputation(null, text, allOps, performOp, nums, true, true)
    assertEquals(expected, result)

    // initial value is impacted by num order
    initialValue = ExactFraction.THREE
    text = splitString("x2-31/0")
    nums = listOf(5, 3, 8, 1, 7, 0, 6, 2, 4, 9)
    expected = ExactFraction(1, 15)
    result = runComputation(initialValue, text, allOps, performOp, nums, true, true)
    assertEquals(expected, result)

    // skip parens
    text = splitString("2(3+4)-6/(7-5)")
    expected = ExactFraction(29, 7)
    result = runComputation(null, text, allOps, performOp, (0..9).toList(), false, true)
    assertEquals(expected, result)

    text = splitString("2x(3+4)/(6.4-.4)")
    nums = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = ExactFraction(685, 38)
    result = runComputation(null, text, allOps, performOp, nums, false, true)
    assertEquals(expected, result)

    // skip decimals
    text = splitString("0.5x2+7")
    expected = 17.toExactFraction()
    result = runComputation(null, text, allOps, performOp, (0..9).toList(), true, false)
    assertEquals(expected, result)

    text =  splitString("8.7/(16-2)")
    nums = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = ExactFraction(80, 51)
    result = runComputation(null, text, allOps, performOp, nums, true, false)
    assertEquals(expected, result)

    text =  splitString("1.1+2.2+3.3")
    expected = 66.toExactFraction()
    result = runComputation(null, text, allOps, performOp, (0..9).toList(), true, false)
    assertEquals(expected, result)

    // skip parens + decimals
    text =  splitString("8.7/(16-2)")
    nums = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = ExactFraction(28, 52)
    result = runComputation(null, text, allOps, performOp, nums, false, false)
    assertEquals(expected, result)

    // alternate ops
    text = splitString("2(3/4)-6+(7x5)")
    expected = ExactFraction.ZERO
    result = runComputation(null, text, listOf(plusMinus, timesDiv), performSwapped, (0..9).toList(), true, true)
    assertEquals(expected, result)

    // normal
    text = splitString("(17+(12-90))/3")
    expected = ExactFraction(-61, 3)
    result = runComputation(null, text, allOps, performOp, (0..9).toList(), true, true)
    assertEquals(expected, result)

    text = splitString("2(7-4)^(0-12/6)")
    expected = ExactFraction(2, 9)
    result = runComputation(null, text, allOps, performOp, (0..9).toList(), true, true)
    assertEquals(expected, result)

    initialValue = ExactFraction.ZERO
    text = splitString("+(17+(12-90))/3")
    expected = ExactFraction(-61, 3)
    result = runComputation(initialValue, text, allOps, performOp, (0..9).toList(), true, true)
    assertEquals(expected, result)

    initialValue = -ExactFraction.HALF
    text = splitString("x3-6")
    expected = ExactFraction(-15, 2)
    result = runComputation(initialValue, text, allOps, performOp, (0..9).toList(), true, true)
    assertEquals(expected, result)
}

fun runParseTextTests() {
    // single value
    var text = listOf("3")
    var expected = ExactFraction.THREE
    assertEquals(expected, parseText(text, allOps, performOp))

    text = listOf("-3.5")
    expected = ExactFraction(-7, 2)
    assertEquals(expected, parseText(text, allOps, performOp))

    val long = "11111111111111111111111111144444444444444444444"
    text = listOf(long)
    expected = ExactFraction(long)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = listOf(ExactFraction(17, 9).toEFString())
    expected = ExactFraction(17, 9)
    assertEquals(expected, parseText(text, allOps, performOp))

    // individual ops
    text = "4 + 7".split(' ')
    expected = 11.toExactFraction()
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 - 7".split(' ')
    expected = -ExactFraction.THREE
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 x 7".split(' ')
    expected = 28.toExactFraction()
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 / 7".split(' ')
    expected = ExactFraction(4, 7)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 ^ 7".split(' ')
    expected = ExactFraction(16384)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "0 / 3".split(' ')
    expected = ExactFraction.ZERO
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "0 x 3".split(' ')
    expected = ExactFraction.ZERO
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "1 x 3".split(' ')
    expected = ExactFraction.THREE
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "8888888888888888888888888888888888888888888 / 2".split(' ')
    expected = ExactFraction("4444444444444444444444444444444444444444444")
    assertEquals(expected, parseText(text, allOps, performOp))

    // multiple ops + parens
    text = "5 + 3 x 2".split(' ')
    expected = 11.toExactFraction()
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "0402 / 2 + 3".split(' ')
    expected = 204.toExactFraction()
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "5 / ( 0.5 + 2 ) / ( 9 x ( 4 - 2 ) )".split(' ')
    expected = ExactFraction(1, 9)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "5 + ( 4 / 11 ) ^ ( 3 - 1 )".split(' ')
    expected = ExactFraction(621, 121)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "( 2 + 7 ) / ( 0.5 - 1 / 2 )".split(' ')
    assertDivByZero {
        parseText(text, allOps, performOp)
    }

    // alternate ops
    text = "5 - ( 0.5 x 2 ) - ( 9 + ( 4 / 2 ) )".split(' ')
    expected = ExactFraction(1, 9)
    assertEquals(expected, parseText(text, listOf(plusMinus, timesDiv), performSwapped))

    text = "5 + 3 - 6".split(' ')
    expected = ExactFraction.TWO
    assertEquals(expected, parseText(text, listOf(listOf(), plusMinus), performOp))

    text = "5 x 3 / 6".split(' ')
    expected = ExactFraction(15, 6)
    assertEquals(expected, parseText(text, listOf(timesDiv, listOf()), performOp))

    text = "5 x 3 / 6 + 2".split(' ')
    var error = assertThrows(Exception::class.java) {
        parseText(text, listOf(timesDiv, listOf()), performOp)
    }
    assertEquals(error.message, "Parse error")

    text = "5 x 3 / 6 + 2".split(' ')
    error = assertThrows(Exception::class.java) {
        parseText(text, allOps, performReduced)
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
    expected = listOf(15.toExactFraction().toEFString())
    assertEquals(expected, parseSetOfOps(text, plusMinus, performOp))

    text = "1.3 x 2 / 10 x 20".split(' ')
    var ef = ExactFraction("5.2").toEFString()
    expected = listOf(ef)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    text = "1.3 x 2 + 10 x 20".split(' ')
    ef = ExactFraction("2.6").toEFString()
    var ef2 = ExactFraction(200).toEFString()
    expected = listOf(ef, "+", ef2)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    text = "1000000000000 - 87 x 10000000000000000 + 33 / 11 + 6 x 40".split(' ')
    ef = ExactFraction("870000000000000000").toEFString()
    ef2 = ExactFraction.THREE.toEFString()
    val ef3 = ExactFraction(240).toEFString()
    expected = listOf("1000000000000", "-", ef, "+", ef2, "+", ef3)
    assertEquals(expected, parseSetOfOps(text, timesDiv, performOp))

    val fakeOps = listOf("a", "b", "c")
    val performFake: OperatorFunction = { lval, rval, op ->
        when (op) {
            "a" -> lval + rval
            "b" -> rval - lval
            "c" -> rval / lval
            else -> ExactFraction.ZERO
        }
    }

    text = "100 a 12 + 1.2 c 2.4 b 3".split(' ')
    ef = ExactFraction(112).toEFString()
    ef2 = ExactFraction.ONE.toEFString()
    expected = listOf(ef, "+", ef2)
    assertEquals(expected, parseSetOfOps(text, fakeOps, performFake))
}

fun runParseParensTests() {
    // no parens
    var text = listOf("3")
    var expected = listOf("3")
    assertEquals(expected, parseParens(text, allOps, performOp))

    text = "3 + 2.0".split(' ')
    expected = "3 + 2.0".split(' ')
    assertEquals(expected, parseParens(text, allOps, performOp))

    // parens
    text = "( 3.0 )".split(' ')
    var ef = ExactFraction(3).toEFString()
    expected = listOf(ef)
    assertEquals(expected, parseParens(text, allOps, performOp))

    text = "( 2 + 3 )".split(' ')
    ef = ExactFraction(5).toEFString()
    expected = listOf(ef)
    assertEquals(expected, parseParens(text, allOps, performOp))

    text = "5 x ( 3 - 1 )".split(' ')
    ef = ExactFraction(2).toEFString()
    expected = listOf("5", "x", ef)
    assertEquals(expected, parseParens(text, allOps, performOp))

    text = "( 9 / ( 6 / 2 ) x 3 x ( 2 + 5 ) ) - ( 7 / 8 )".split(' ')
    ef = ExactFraction(63).toEFString()
    val ef2 = ExactFraction(7, 8).toEFString()
    expected = listOf(ef, "-", ef2)
    assertEquals(expected, parseParens(text, allOps, performOp))

    text = "2 + ( 5 / 3 )".split(' ')
    ef = ExactFraction(5, 3).toEFString()
    expected = listOf("2", "+", ef)
    assertEquals(expected, parseParens(text, listOf(plusMinus, timesDiv), performOp))
}
