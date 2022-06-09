package com.example.trickcalculator.ext

import com.example.trickcalculator.runRandomTest
import exactfraction.ExactFraction
import org.junit.Test
import org.junit.Assert.*

class MutableListExtTest {
    @Test
    fun testPopRandom() {
        var ml = mutableListOf<String>()
        assertNull(ml.popRandom())

        ml = mutableListOf("123")
        assertEquals("123", ml.popRandom())
        assertEquals(mutableListOf<String>(), ml)

        ml = mutableListOf("123", "456")
        runSinglePopRandomTest(ml)

        ml = mutableListOf("1", "1", "1", "2")
        runSinglePopRandomTest(ml)

        val mlI = mutableListOf(23, 89, 0, -104, 44, 2)
        runSinglePopRandomTest(mlI)

        val mlEF = mutableListOf(
            ExactFraction.THREE,
            ExactFraction.HALF,
            ExactFraction(-19, 2),
            ExactFraction(7, 12),
            ExactFraction(18, 103)
        )
        runSinglePopRandomTest(mlEF)
    }

    // assumes ml has size >= 2 and does not have all elements of same value
    private fun <T> runSinglePopRandomTest(ml: MutableList<T>) {
        val createResultList = {
            val list = mutableListOf<T>()
            list.addAll(ml)
            val resultList = mutableListOf<T>()
            for (i in ml.indices) {
                val result = list.popRandom()
                assertNotNull(result)
                resultList.add(result!!)
            }

            resultList
        }
        val checkResult = { it: MutableList<T> -> it != ml }

        runRandomTest(createResultList, checkResult)
    }
}
