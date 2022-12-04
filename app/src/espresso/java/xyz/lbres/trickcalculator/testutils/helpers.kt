package xyz.lbres.trickcalculator.testutils

fun repeatUntil(maxRepeats: Int, breakCondition: () -> Boolean, code: () -> Unit) {
    var repeats = 0
    while (repeats < maxRepeats && !breakCondition()) {
        code()
        repeats++
    }
}
