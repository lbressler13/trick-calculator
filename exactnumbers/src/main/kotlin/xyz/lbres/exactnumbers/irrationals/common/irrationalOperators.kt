package xyz.lbres.exactnumbers.irrationals.common

import xyz.lbres.exactnumbers.common.divideByZero
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.exactnumbers.expressions.term.Term

internal operator fun Irrational.times(other: Irrational): Term = Term(ExactFraction.ONE, listOf(this, other))
internal operator fun Irrational.div(other: Irrational): Term {
    if (other.isZero()) {
        throw divideByZero
    }

    return Term(ExactFraction.ONE, listOf(this, other.swapDivided()))
}
