package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.SharedValues.random

/**
 * IntRange methods using shared random as a seed
 */
fun IntRange.seededRandom(): Int = random(random)
fun IntRange.seededShuffled(): IntList = shuffled(random)
