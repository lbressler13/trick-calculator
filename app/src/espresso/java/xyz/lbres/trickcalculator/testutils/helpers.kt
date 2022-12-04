package xyz.lbres.trickcalculator.testutils

/**
 * Alternate to standard [repeat] function, to allow breaking out of the loop.
 *
 * @param maxRepeats [Int]: number of times to repeat code without breaking
 * @param breakCondition () -> [Boolean]: condition to break out of loop
 * @param function () -> [Unit]: code to execute repeatedly
 */
fun repeatUntil(maxRepeats: Int, breakCondition: () -> Boolean, function: () -> Unit) {
    var repeats = 0
    while (repeats < maxRepeats && !breakCondition()) {
        function()
        repeats++
    }
}
