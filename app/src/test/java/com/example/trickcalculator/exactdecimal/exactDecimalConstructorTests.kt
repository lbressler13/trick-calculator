package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprList
import org.junit.Assert.*

// nothing that requires simplifying

private val exprList1 = listOf(
    Expression(Term(ExactFraction(3, 7), 18)),
    listOf(Term(5), Term(-100, 1)).asExpression(),
    Expression.ONE
)

private val exprList2 = listOf(
    listOf(
        Term(ExactFraction(-30, 7), 23),
        Term(ExactFraction(2, 100001), 10001),
        Term(-4),
        Term(501, 31998)
    ).asExpression(),
    listOf(
        Term(14),
        Term(ExactFraction(13, 3), 14)
    ).asExpression()
)

// nothing that requires simplification
fun runConstructorTests() {
    // List, List
    testListPair(listOf(Expression.ONE), listOf(Expression.ONE))
    testListPair(exprList1, exprList2)
    testListPair(exprList1, listOf(Expression(Term(3, -3))))
    testListPair(listOf(Expression(Term(3, -3))), exprList1)

    // empty lists
    var formatError = assertThrows(NumberFormatException::class.java) { ExactDecimal(listOf(), listOf()) }
    assertEquals("Numerator cannot be empty", formatError.message)

    formatError = assertThrows(NumberFormatException::class.java) {
        ExactDecimal(listOf(), listOf(Expression.ZERO))
    }
    assertEquals("Numerator cannot be empty", formatError.message)

    formatError = assertThrows(NumberFormatException::class.java) {
        ExactDecimal(listOf(Expression.ZERO), listOf())
    }
    assertEquals("Denominator cannot be empty", formatError.message)

    // divide by zero
    var divideError = assertThrows(ArithmeticException::class.java) {
        ExactDecimal(listOf(Expression.ONE), listOf(Expression.ZERO))
    }
    assertEquals("Divide by zero", divideError.message)

    divideError = assertThrows(ArithmeticException::class.java) {
        ExactDecimal(
            listOf(Expression.ONE),
            listOf(Expression(Term(1)), Expression.ZERO)
        )
    }
    assertEquals("Divide by zero", divideError.message)

    divideError = assertThrows(ArithmeticException::class.java) {
        ExactDecimal(
            listOf(Expression.ONE),
            listOf(listOf(Term(1), Term(-1)).asExpression())
        )
    }
    assertEquals("Divide by zero", divideError.message)


    // List
    testList(listOf(Expression.ZERO))
    testList(listOf(Expression.ONE))
    testList(listOf(Expression.ONE, Expression(Term(3))))
    testList(listOf(
        Expression(Term(1, -8)),
        Expression(Term(3)))
    )
    testList(listOf(
        Expression(Term(ExactFraction.HALF * 7, 3333333)),
        Expression(Term(10000000000000)),
        Expression.ONE,
        Expression(listOf(Term(8, 6), Term(3, 4), Term.ZERO))
    ))

    formatError = assertThrows(NumberFormatException::class.java) { ExactDecimal(listOf()) }
    assertEquals("Numerator cannot be empty", formatError.message)

    // ExactFraction
    testEF(ExactFraction.ZERO)
    testEF(ExactFraction.NINE)
    testEF(-ExactFraction.NINE)
    testEF(ExactFraction(-1, 4))
    testEF(ExactFraction(17, 9))
    testEF(ExactFraction(4, 4))
    testEF(ExactFraction(102, 3))

    // Int
    testInt(0)
    testInt(1)
    testInt(-1)
    testInt(2)
    testInt(Int.MIN_VALUE)
    testInt(Int.MAX_VALUE)

    // Long
    testLong(0)
    testLong(1)
    testLong(-1)
    testLong(2)
    testLong(Long.MIN_VALUE)
    testLong(Long.MAX_VALUE)
}

private fun testListPair(l1: ExprList, l2: ExprList) {
    val ed = ExactDecimal(l1, l2)
    assertEquals(l1, ed.numerator)
    assertEquals(l2, ed.denominator)
}

private fun testList(l: ExprList) {
    val ed = ExactDecimal(l)
    assertEquals(l, ed.numerator)
    assertEquals(listOf(Expression.ONE), ed.denominator)
}

private fun testEF(ef: ExactFraction) {
    val ed = ExactDecimal(ef)
    val numTerm = Term(ef.numerator)
    val denomTerm = Term(ef.denominator)
    val expectedN = listOf(Expression(numTerm))
    val expectedD = listOf(Expression(denomTerm))
    assertEquals(expectedN, ed.numerator)
    assertEquals(expectedD, ed.denominator)
}

private fun testInt(i: Int) {
    val ed = ExactDecimal(i)
    val expectedN = listOf(Expression(Term(i)))
    val expectedD = listOf(Expression.ONE)
    assertEquals(expectedN, ed.numerator)
    assertEquals(expectedD, ed.denominator)
}

private fun testLong(l: Long) {
    val ed = ExactDecimal(l)
    val expectedN = listOf(Expression(Term(l)))
    val expectedD = listOf(Expression.ONE)
    assertEquals(expectedN, ed.numerator)
    assertEquals(expectedD, ed.denominator)
}
