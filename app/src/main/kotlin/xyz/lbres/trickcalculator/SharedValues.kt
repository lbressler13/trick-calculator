package xyz.lbres.trickcalculator

import java.util.Date
import kotlin.random.Random

/**
 * Values that are shared throughout the app
 */
object SharedValues {
    val random = Random(Date().time)
}
