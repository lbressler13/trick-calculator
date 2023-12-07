package xyz.lbres.trickcalculator.testutils

/**
 * Alternate to standard [repeat] function, to allow breaking out of the loop.
 *
 * @param maxIterations [Int]: number of times to repeat code without breaking loop
 * @param breakCondition () -> [Boolean]: condition to break out of loop
 * @param function () -> [Unit]: code to execute repeatedly
 */
fun repeatUntil(maxIterations: Int, breakCondition: () -> Boolean, function: () -> Unit) {
    var repeats = 0
    while (repeats < maxIterations && !breakCondition()) {
        function()
        repeats++
    }
}
