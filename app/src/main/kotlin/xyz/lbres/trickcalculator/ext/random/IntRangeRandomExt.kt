package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.SharedValues

/**
 * IntRange methods using shared random as a seed
 */
fun IntRange.seededRandom(): Int = random(SharedValues.random)

fun IntRange.seededShuffled(): IntList = shuffled(SharedValues.random)
