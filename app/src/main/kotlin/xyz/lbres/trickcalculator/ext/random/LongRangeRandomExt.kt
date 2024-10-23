package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.trickcalculator.SharedValues.random

/**
 * LongRange methods using shared random as a seed
 */
fun LongRange.seededRandom(): Long = random(random)
