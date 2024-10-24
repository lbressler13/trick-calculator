package xyz.lbres.trickcalculator.compute

import io.mockk.every
import io.mockk.mockkStatic
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.assertDivByZero
import xyz.lbres.trickcalculator.assertFailsWithMessage
import xyz.lbres.trickcalculator.splitString
import xyz.lbres.trickcalculator.utils.AppLogger
import xyz.lbres.trickcalculator.utils.OperatorFunction
import kotlin.test.assertEquals

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
    assertFailsWithMessage("Syntax error") { callRunComputation(null, splitString("1+3-")) }

    assertDivByZero {
        callRunComputation(null, splitString("1/0"))
    }

    assertDivByZero {
        val order = listOf(1, 2, 0, 3, 4, 5, 6, 7, 8, 9)
        callRunComputation(ExactFraction.HALF, emptyList(), order = order)
    }

    assertDivByZero {
        callRunComputation(ExactFraction.EIGHT, splitString("/0"))
    }

    assertFailsWithMessage("Invalid operator +") {
        callRunComputation(null, splitString("1+0"), executeOp = performReduced)
    }

    // cannot test parsing and overflow errors b/c they should never happen

    // numbers order
    var order = listOf(3, 8, 2, 6, 0, 1, 9, 7, 5, 4)
    var expected = ExactFraction(8, 3)
    assertEquals(expected, callRunComputation(null, splitString("1/0"), order = order))

    var text = splitString("2x(3+4)/6.9")
    order = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = ExactFraction(33, 9)
    assertEquals(expected, callRunComputation(null, text, order = order))

    // initial value is impacted by num order
    text = splitString("x2-31/0")
    order = listOf(5, 3, 8, 1, 7, 0, 6, 2, 4, 9)
    expected = ExactFraction(1, 15)
    assertEquals(expected, callRunComputation(ExactFraction.THREE, text, order = order))

    // skip parens
    text = splitString("2(3+4)-6/(7-5)")
    expected = ExactFraction(29, 7)
    assertEquals(expected, callRunComputation(null, text, applyParens = false))

    text = splitString("2x(3+4)/(6.4-.4)")
    order = listOf(1, 2, 3, 6, 5, 7, 9, 4, 8, 0)
    expected = ExactFraction(685, 38)
    assertEquals(expected, callRunComputation(null, text, order = order, applyParens = false))

    // skip decimals
    text = splitString("0.5x2+7")
    expected = ExactFraction(17)
    assertEquals(expected, callRunComputation(null, text, applyDecimals = false))

    text = splitString("8.7/(16-2)")
    order = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = ExactFraction(80, 51)
    assertEquals(expected, callRunComputation(null, text, order = order, applyDecimals = false))

    text = splitString("1.1+2.2+3.3")
    expected = ExactFraction(66)
    assertEquals(expected, callRunComputation(null, text, applyDecimals = false))

    // skip parens + decimals
    text = splitString("8.7/(16-2)")
    order = listOf(3, 5, 1, 4, 9, 7, 2, 0, 8, 6)
    expected = ExactFraction(28, 52)
    assertEquals(expected, callRunComputation(null, text, order = order, applyParens = false, applyDecimals = false))

    // alternate ops
    text = splitString("2(3/4)-6+(7x5)")
    expected = ExactFraction.ZERO
    assertEquals(expected, callRunComputation(null, text, ops = listOf(plusMinus, timesDiv), executeOp = performSwapped))

    // normal
    text = splitString("(17+(12-90))/3")
    expected = ExactFraction(-61, 3)
    assertEquals(expected, callRunComputation(null, text))

    text = splitString("2(7-4)^(0-12/6)")
    expected = ExactFraction(2, 9)
    assertEquals(expected, callRunComputation(null, text))

    text = splitString("+(17+(12-90))/3")
    expected = ExactFraction(-61, 3)
    assertEquals(expected, callRunComputation(ExactFraction.ZERO, text))

    text = splitString("x3-6")
    expected = ExactFraction(-15, 2)
    assertEquals(expected, callRunComputation(-ExactFraction.HALF, text))
}

fun runParseTextTests() {
    mockkStatic(AppLogger::class)
    every { AppLogger.e(any(), any()) } returns 0

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
    expected = ExactFraction(11)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 - 7".split(' ')
    expected = -ExactFraction.THREE
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "4 x 7".split(' ')
    expected = ExactFraction(28)
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
    expected = ExactFraction(11)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "0402 / 2 + 3".split(' ')
    expected = ExactFraction(204)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "5 / ( 0.5 + 2 ) / ( 9 x ( 4 - 2 ) )".split(' ')
    expected = ExactFraction(1, 9)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "5 + ( 4 / 11 ) ^ ( 3 - 1 )".split(' ')
    expected = ExactFraction(621, 121)
    assertEquals(expected, parseText(text, allOps, performOp))

    text = "( 2 x ( 3 ) x 4 )".split(' ')
    expected = ExactFraction(24)
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
    assertEquals(expected, parseText(text, listOf(emptyList(), plusMinus), performOp))

    text = "5 x 3 / 6".split(' ')
    expected = ExactFraction(15, 6)
    assertEquals(expected, parseText(text, listOf(timesDiv, emptyList()), performOp))

    text = "5 x 3 / 6 + 2".split(' ')
    assertFailsWithMessage("Parse error") { parseText(text, listOf(timesDiv, emptyList()), performOp) }

    text = "5 x 3 / 6 + 2".split(' ')
    assertFailsWithMessage("Invalid operator +") { parseText(text, allOps, performReduced) }
}

// should not include parens
fun runParseOperatorRoundTests() {
    // unchanged
    var text = listOf("-1.00")
    assertEquals(listOf("-1.00"), parseOperatorRound(text, plusMinus, performOp))

    text = "1 x 3 / 17".split(' ')
    var expected = "1 x 3 / 17".split(' ')
    assertEquals(expected, parseOperatorRound(text, plusMinus, performOp))

    text = "1 - 3 + 17".split(' ')
    expected = "1 - 3 + 17".split(' ')
    assertEquals(expected, parseOperatorRound(text, timesDiv, performOp))

    // changed
    text = "1 - 3 + 17".split(' ')
    expected = listOf(ExactFraction(15).toEFString())
    assertEquals(expected, parseOperatorRound(text, plusMinus, performOp))

    text = "1.3 x 2 / 10 x 20".split(' ')
    var ef = ExactFraction("5.2").toEFString()
    assertEquals(listOf(ef), parseOperatorRound(text, timesDiv, performOp))

    text = "1.3 x 2 + 10 x 20".split(' ')
    ef = ExactFraction("2.6").toEFString()
    var ef2 = ExactFraction(200).toEFString()
    assertEquals(listOf(ef, "+", ef2), parseOperatorRound(text, timesDiv, performOp))

    text = "1000000000000 - 87 x 10000000000000000 + 33 / 11 + 6 x 40".split(' ')
    ef = ExactFraction("870000000000000000").toEFString()
    ef2 = ExactFraction.THREE.toEFString()
    val ef3 = ExactFraction(240).toEFString()
    expected = listOf("1000000000000", "-", ef, "+", ef2, "+", ef3)
    assertEquals(expected, parseOperatorRound(text, timesDiv, performOp))

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
    assertEquals(listOf(ef, "+", ef2), parseOperatorRound(text, fakeOps, performFake))
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

/**
 * Call [runComputation] with default setting values.
 * [initialValue] and [text] are required, but all settings values are optional and have defaults.
 */
private fun callRunComputation(
    initialValue: ExactFraction?,
    text: StringList,
    ops: List<StringList> = allOps,
    executeOp: OperatorFunction = performOp,
    order: IntList = List(10) { it },
    applyParens: Boolean = true,
    applyDecimals: Boolean = true,
    shuffleComputation: Boolean = false,
    randomizeSigns: Boolean = false,
): ExactFraction {
    return runComputation(initialValue, text, ops, executeOp, order, applyParens, applyDecimals, shuffleComputation, randomizeSigns)
}
