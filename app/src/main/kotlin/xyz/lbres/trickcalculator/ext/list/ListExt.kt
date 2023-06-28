package xyz.lbres.trickcalculator.ext.list

import xyz.lbres.trickcalculator.SharedValues

fun <E> List<E>.appRandom(): E = random(SharedValues.random)

fun <E> List<E>.appShuffled(): List<E> = shuffled(SharedValues.random)
