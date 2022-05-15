package com.example.trickcalculator.exactdecimal

import org.junit.Assert.*
import exactfraction.ExactFraction
import com.example.trickcalculator.utils.ExprLPair
import com.example.trickcalculator.utils.ExprList

fun runRemoveCommonTests() {
    testEmpty()
    testNoCommon()
    testSomeCommon()
}

private fun testEmpty() {
    var l1: ExprList = listOf()
    var l2: ExprList = listOf()
    var expected: ExprLPair = Pair(listOf(), listOf())
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf()
    l2 = listOf(Expression.ONE)
    expected = Pair(listOf(), listOf(Expression.ONE))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(Expression.ONE)
    l2 = listOf()
    expected = Pair(listOf(Expression.ONE), listOf())
    assertEquals(expected, removeCommon(l1, l2))
}

private fun testNoCommon() {
    var l1 = listOf(Expression.ZERO)
    var l2 = listOf(Expression.ONE)
    var expected = Pair(listOf(Expression.ZERO), listOf(Expression.ONE))
    assertEquals(expected, removeCommon(l1, l2))

    val t = Term(3)
    l1 = listOf(Expression(t))
    l2 = listOf(Expression(-t))
    expected = Pair(listOf(Expression(t)), listOf(Expression(-t)))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(Expression(Term(2, 2)))
    l2 = listOf(Expression(Term(2, -2)))
    expected = Pair(
        listOf(Expression(Term(2, 2))),
        listOf(Expression(Term(2, -2)))
    )
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        listOf(
            Term(2, 1),
            Term(3, 2)
        ).asExpression()
    )
    l2 = listOf(
        listOf(
            Term(1, 2),
            Term(2, 3)
        ).asExpression()
    )
    expected = Pair(
        listOf(
            listOf(
                Term(2, 1),
                Term(3, 2)
            ).asExpression()
        ),
        listOf(
            listOf(
                Term(1, 2),
                Term(2, 3)
            ).asExpression()
        )
    )
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        Expression.ONE,
        Expression(Term.ZERO),
        listOf(
            Term(ExactFraction(9, 2), -2),
            Term(-3, -1)
        ).asExpression()
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, 2)),
        listOf(
            Term(3, 6),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF, 2)
        ).asExpression(),
    )
    expected = Pair(
        listOf(
            listOf(
                Term(ExactFraction(9, 2), -2),
                Term(-3, -1)
            ).asExpression(),
            Expression.ONE,
            Expression(Term.ZERO),
            listOf(
                Term(3, 7),
                Term(ExactFraction(-13, 12)),
                Term(ExactFraction.HALF)
            ).asExpression(),
        ),
        listOf(
            Expression(Term(ExactFraction.HALF, 2)),
            listOf(
                Term(3, 6),
                Term(ExactFraction(-13, 12)),
                Term(ExactFraction.HALF)
            ).asExpression(),
            listOf(
                Term(3, 7),
                Term(ExactFraction(-13, 12)),
                Term(ExactFraction.HALF, 2)
            ).asExpression(),
        )
    )
    assertEquals(expected, removeCommon(l1, l2))
}

private fun testSomeCommon() {
    // exact equal
    var expected: ExprLPair = Pair(listOf(), listOf())

    var l1 = listOf(Expression.ONE)
    var l2 = listOf(Expression.ONE)
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(Expression(Term(2)))
    l2 = listOf(Expression(Term(2)))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(Expression(Term(2, 1)))
    l2 = listOf(Expression(Term(2, 1)))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        Expression(Term(9)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    l2 = listOf(
        Expression(Term(9)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        Expression(Term(9)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, -2)),
        Expression(Term(9))
    )
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        Expression(Term(ExactFraction.HALF, -2)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, -2)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    assertEquals(expected, removeCommon(l1, l2))

    // one is subset
    l1 = listOf(
        Expression(Term(ExactFraction.HALF, -2))
    )
    l2 = listOf(
        Expression(Term(9)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    expected = Pair(listOf(), listOf(Expression(Term(9))))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        Expression(Term(9)),
        Expression(Term(ExactFraction.HALF, -2))
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, -2))
    )
    expected = Pair(listOf(Expression(Term(9))), listOf())
    assertEquals(expected, removeCommon(l1, l2))

    var e = Expression(Term(ExactFraction(-7, 11), -2))
    var e2 = Expression(Term(ExactFraction(11, -7), 2))

    l1 = listOf(e, e)
    l2 = listOf(e, e, e, e)
    expected = Pair(listOf(), listOf(e, e))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(e, e2)
    l2 = listOf(e, e2, e, e)
    expected = Pair(listOf(), listOf(e, e))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(e2, e2, e, e)
    l2 = listOf(e, e2, e)
    expected = Pair(listOf(e2), listOf())
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(e2, e2, e, e)
    l2 = listOf(e2, e)
    expected = Pair(listOf(e, e2), listOf()) // sorts automatically
    assertEquals(expected, removeCommon(l1, l2))

    // overlapping
    e = Expression(Term(ExactFraction(-7, 11), -2))
    e2 = Expression(Term(ExactFraction(11, -7), 2))

    l1 = listOf(e, e2, e2)
    l2 = listOf(e, e, e2)
    expected = Pair(listOf(e2), listOf(e))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(e2, e, e2)
    l2 = listOf(e, e)
    expected = Pair(listOf(e2, e2), listOf(e))
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        Expression.ONE,
        Expression(Term.ZERO),
        listOf(
            Term(ExactFraction(9, 2), -2),
            Term(-3, -1)
        ).asExpression()
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, 2)),
        listOf(
            Term(3, 6),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
    )
    expected = Pair(
        listOf(
            listOf(
                Term(ExactFraction(9, 2), -2),
                Term(-3, -1)
            ).asExpression(),
            Expression.ONE,
            Expression(Term.ZERO)
        ),
        listOf(
            Expression(Term(ExactFraction.HALF, 2)),
            listOf(
                Term(3, 6),
                Term(ExactFraction(-13, 12)),
                Term(ExactFraction.HALF)
            ).asExpression()
        )
    )
    assertEquals(expected, removeCommon(l1, l2))

    l1 = listOf(
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        Expression.ONE,
        Expression.ONE,
        Expression(Term.ZERO),
        listOf(
            Term(ExactFraction(9, 2), -2),
            Term(-3, -1)
        ).asExpression()
    )
    l2 = listOf(
        Expression(Term(ExactFraction.HALF, 2)),
        listOf(
            Term(3, 6),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
        Expression.ONE,
        Expression(Term.ZERO),
        Expression.ZERO,
        listOf(
            Term(3, 7),
            Term(ExactFraction(-13, 12)),
            Term(ExactFraction.HALF)
        ).asExpression(),
    )
    expected = Pair(
        listOf(
            listOf(
                Term(ExactFraction(9, 2), -2),
                Term(-3, -1)
            ).asExpression(),
            Expression.ONE
        ),
        listOf(
            Expression.ZERO,
            Expression(Term(ExactFraction.HALF, 2)),
            listOf(
                Term(3, 6),
                Term(ExactFraction(-13, 12)),
                Term(ExactFraction.HALF)
            ).asExpression()
        )
    )
    assertEquals(expected, removeCommon(l1, l2))
}
