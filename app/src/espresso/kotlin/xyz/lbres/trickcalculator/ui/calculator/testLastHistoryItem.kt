package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.clearText
import xyz.lbres.trickcalculator.testutils.equals
import xyz.lbres.trickcalculator.testutils.typeText
import xyz.lbres.trickcalculator.testutils.withAnyText

private val mainText = onView(withId(R.id.mainText))
private val useLastButton = onView(withId(R.id.useLastHistoryButton))

fun testLastHistoryItem() {
    val errorText = onView(withId(R.id.errorText))

    // none
    useLastButton.check(matches(not(isDisplayed())))

    // one number
    runSingleTest("123", "[123]")
    runSingleTest("(-1234)", "[-1234]")
    runSingleTest("00001.50000", "[1.5]")

    // multiple numbers
    runSingleTest("13+46")
    runSingleTest(".5(66+99/3)x0.001")

    // with computed value
    clearText()
    typeText("1234")
    equals()
    typeText("+2")
    useLastButton.perform(click())
    mainText.check(matches(withText("1234")))

    runSingleTestWithComputedValue("1234", "+2", setOf("[1234]+2"))

    val longComputation = "(5.4+2)-3"
    var options =
        setOf(
            "[5]$longComputation",
            "[1]$longComputation",
            "[6]$longComputation",
            "[1.5]$longComputation",
        )
    runSingleTestWithComputedValue("3+2", longComputation, options)

    // no times added for multiplication
    clearText()
    typeText("1234")
    equals()
    typeText("2")
    useLastButton.perform(click())
    mainText.check(matches(withText("1234")))

    runSingleTestWithComputedValue("1234", "2", setOf("[1234]2"))

    // after error
    clearText()
    typeText("(1")
    equals() // set error
    mainText.check(matches(withText("(1")))
    errorText.check(matches(isDisplayed()))
    useLastButton.perform(click())
    mainText.check(matches(withText("(1"))) // pulls computation without error
    errorText.check(matches(not(isDisplayed())))

    clearText()
    typeText("6x3")
    equals() // set result
    options = setOf("[9]", "[3]", "[18]", "[2]")
    mainText.check(matches(withAnyText(options)))
    typeText("+")
    equals() // set error
    errorText.check(matches(isDisplayed()))
    clearText()
    errorText.check(matches(not(isDisplayed())))
    useLastButton.perform(click()) // pull in previous result
    errorText.check(matches(not(isDisplayed())))
    options = setOf("[9]+", "[3]+", "[18]+", "[2]+")
    mainText.check(matches(withAnyText(options)))

    // repeat button clicks
    clearText()
    typeText("00")
    equals()
    mainText.check(matches(withText("[0]")))
    useLastButton.perform(click())
    mainText.check(matches(withText("00")))
    useLastButton.perform(click())
    mainText.check(matches(withText("00")))

    clearText()
    useLastButton.perform(click())
    mainText.check(matches(withText("00")))

    clearText()
    typeText("1")
    useLastButton.perform(click())
    mainText.check(matches(withText("00")))
}

/**
 * Type a computation and validate that it is retrieved using last item button.
 * Also validates result of the computation, if [expectedText] is not `null`
 *
 * @param computation [String]: computation to type
 * @param expectedText [String]?: expected result of computation, defaults to `null`
 */
private fun runSingleTest(
    computation: String,
    expectedText: String? = null,
) {
    clearText()
    typeText(computation)
    equals()

    if (expectedText != null) {
        mainText.check(matches(withText(expectedText)))
    } else {
        mainText.check(matches(not(withText(computation))))
    }

    useLastButton.perform(click())
    mainText.check(matches(withText(computation)))
}

/**
 * Type two computations and check that second computation is retrieved using last item button
 *
 * @param comp1 [String]: first computation to type
 * @param comp2 [String]: second computation to type
 * @param previousOptions [Set]<String>: possible results of [comp1]
 */
private fun runSingleTestWithComputedValue(
    comp1: String,
    comp2: String,
    previousOptions: Set<String>,
) {
    clearText()
    typeText(comp1)
    equals()
    typeText(comp2)
    equals()
    useLastButton.perform(click())
    mainText.check(matches(withAnyText(previousOptions)))
}
