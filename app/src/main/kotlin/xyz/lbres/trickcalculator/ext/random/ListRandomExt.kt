package xyz.lbres.trickcalculator.ext.random

import xyz.lbres.trickcalculator.SharedValues

/**
 * List methods using shared random as a seed
 */
fun <E> List<E>.seededRandom(): E = random(SharedValues.random)
fun <E> List<E>.seededShuffled(): List<E> = shuffled(SharedValues.random)
