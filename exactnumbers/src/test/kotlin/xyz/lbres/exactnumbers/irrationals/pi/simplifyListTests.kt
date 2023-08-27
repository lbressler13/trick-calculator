package xyz.lbres.exactnumbers.irrationals.pi

import xyz.lbres.exactnumbers.irrationals.log.Log
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal fun runSimplifyListTests() {
    // error
    assertFailsWith<ClassCastException> { Pi.simplifyList(listOf(Pi(), Log.ONE)) }

    // equal
    var expected: List<Pi> = emptyList()

    assertEquals(expected, Pi.simplifyList(null))

    var pis: List<Pi> = emptyList()
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(), Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(true), Pi(), Pi(true), Pi(), Pi(), Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))

    // positive
    pis = listOf(Pi())
    expected = listOf(Pi())
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(), Pi(), Pi())
    expected = listOf(Pi(), Pi(), Pi())
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(), Pi(), Pi(true))
    expected = listOf(Pi())
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(), Pi(), Pi(), Pi(true), Pi(), Pi(true))
    expected = listOf(Pi(), Pi())
    assertEquals(expected, Pi.simplifyList(pis))

    // negative
    pis = listOf(Pi(true))
    expected = listOf(Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(true), Pi(true), Pi(true))
    expected = listOf(Pi(true), Pi(true), Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(true), Pi(true), Pi())
    expected = listOf(Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))

    pis = listOf(Pi(true), Pi(true), Pi(true), Pi(), Pi(true), Pi())
    expected = listOf(Pi(true), Pi(true))
    assertEquals(expected, Pi.simplifyList(pis))
}
