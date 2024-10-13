package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.trickcalculator.SharedValues.random

/**
 * List methods using shared random as a seed
 */
fun <E> List<E>.seededRandom(): E = random(random)
fun <E> List<E>.seededShuffled(): List<E> = shuffled(random)
