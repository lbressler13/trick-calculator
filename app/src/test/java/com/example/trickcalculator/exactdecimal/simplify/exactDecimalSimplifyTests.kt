package com.example.trickcalculator.exactdecimal

import com.example.trickcalculator.exactfraction.ExactFraction
import org.junit.Assert.*

fun runSimplifyTests() {
    // single single-term expression
    var ed = ExactDecimal(ExactFraction.ZERO)
    var expected = ExactDecimal(listOf(Expression.ZERO), listOf(Expression.ONE))
    assertEquals(expected, simplify(ed))

    ed = ExactDecimal(ExactFraction.SIX)
    expected = ExactDecimal(
        listOf(Expression(Term(ExactFraction.SIX))),
        listOf(Expression.ONE)
    )
    assertEquals(expected, simplify(ed))

    ed = ExactDecimal(ExactFraction(14, -23))
    expected = ExactDecimal(
        listOf(Expression(Term(ExactFraction(-14)))),
        listOf(Expression(Term(23)))
    )
    assertEquals(expected, simplify(ed))

    var num = listOf(Expression(Term(3, 1)))
    var denom = listOf(Expression(Term(ExactFraction.HALF)))
    expected = ExactDecimal(
        listOf(Expression(Term(1, 1)), Expression(Term(3))),
        listOf(Expression(Term(ExactFraction.HALF)))
    )
    assertEquals(expected, simplify(ExactDecimal(num, denom)))

    // single multi-term expression
    num = listOf(listOf(Term(3), Term(-9)).asExpression())
    denom = listOf(Expression(Term(27, 2)))
    expected = ExactDecimal(
        listOf(Expression(Term(-1))),
        listOf(Expression(Term(1, 2)))
    )
    assertEquals(expected, simplify(ExactDecimal(num, denom)))

    num = listOf(listOf(Term(3, -1), Term(-9)).asExpression())
    denom = listOf(Expression(Term(27, 2)))
    expected = ExactDecimal(
        listOf(
            listOf(Term(1, -1), Term(-3)).asExpression()
        ),
        listOf(listOf(Term(9), Term(1, 2)).asExpression())
    )
    assertEquals(expected, simplify(ExactDecimal(num, denom)))

    num = listOf(listOf(Term(3, -1), Term(-9)).asExpression())
    denom = listOf(listOf(Term(27, 2), Term(4, 3)).asExpression())
    expected = ExactDecimal(
        listOf(
            listOf(Term(1, -1), Term(-3)).asExpression()
        ),
        listOf(listOf(Term(27, 2), Term(4, 3)).asExpression())
    )
    assertEquals(expected, simplify(ExactDecimal(num, denom)))

    // num = listOf(listOf(Term(3, -1), Term(-9)).asExpression())
    // denom = listOf(listOf(Term(27, 2), Term(4, 3)).asExpression())
    // expected = ExactDecimal(
    //     listOf(
    //         listOf(Term(1, -1), Term(-3)).asExpression()
    //     ),
    //     listOf(listOf(Term(36), Term(1, 2)).asExpression())
    // )
    // assertEquals(expected, simplify(ExactDecimal(num, denom)))

    // TODO

    // multiple expressions

    // can't be simplified

    // multiple w/ same exp
}
