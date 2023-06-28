package xyz.lbres.trickcalculator.ext.intrange

import xyz.lbres.kotlinutils.list.IntList
import xyz.lbres.trickcalculator.SharedValues.random

fun IntRange.appRandom(): Int = random(random)

fun IntRange.appShuffled(): IntList = shuffled(random)
