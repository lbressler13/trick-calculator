package xyz.lbres.trickcalculator.utils

import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.SharedValues.random

/**
 * IntRange methods using shared random as a seed
 */
fun IntRange.seededRandom(): Int = random(random)
fun IntRange.seededShuffled(): IntList = shuffled(random)

/**
 * LongRange methods using shared random as a seed
 */
fun LongRange.seededRandom(): Long = random(random)

/**
 * List methods using shared random as a seed
 */
fun <E> List<E>.seededRandom(): E = random(random)
fun <E> List<E>.seededShuffled(): List<E> = shuffled(random)
