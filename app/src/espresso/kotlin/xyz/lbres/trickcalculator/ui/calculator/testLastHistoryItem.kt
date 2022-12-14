package xyz.lbres.trickcalculator.ui.calculator

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R

fun testLastHistoryItem(mainText: ViewInteraction) {
    val useLastButton = onView(withId(R.id.useLastHistoryButton))

    // none
    useLastButton.check(matches(not(isDisplayed())))

    // one value
    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))
    useLastButton.perform(click())
    mainText.check(matches(withText("123")))

    clearText()
    typeText("(1234)")
    equals()
    mainText.check(matches(withText("[1234]")))
    useLastButton.perform(click())
    mainText.check(matches(withText("(1234)")))
    // doesn't pull previous item
    useLastButton.perform(click())
    mainText.check(matches(withText("(1234)")))

    clearText()
    typeText("00001.50000")
    equals()
    mainText.check(matches(withText("[1.5]")))
    useLastButton.perform(click())
    mainText.check(matches(withText("00001.50000")))

    // multiple values
    clearText()
    var computation = "123+456"
    typeText(computation)
    equals()
    mainText.check(matches(not(withText(computation))))
    useLastButton.perform(click())
    mainText.check(matches(withText(computation)))

    clearText()
    computation = ".5(66+99/33)x22x2+0.001"
    typeText(computation)
    equals()
    mainText.check(matches(not(withText(computation))))
    useLastButton.perform(click())
    mainText.check(matches(withText(computation)))

    // with computed value
    clearText()
    typeText("1234")
    equals()
    typeText("+2")
    useLastButton.perform(click())
    mainText.check(matches(withText("1234")))

    clearText()
    typeText("1234")
    equals()
    typeText("+2")
    equals()
    useLastButton.perform(click())
    mainText.check(matches(withText("[1234]+2")))

    clearText()
    typeText("3+2")
    equals()
    computation = "(5.4+2)-3"
    typeText(computation)
    equals()
    useLastButton.perform(click())
    checkMainTextMatchesAny(
        setOf(
            "[5]$computation", "[1]$computation", "[6]$computation", "[1.5]$computation"
        )
    )

    // after error
    clearText()
    typeText("(1")
    equals() // set error
    mainText.check(matches(withText("(1")))
    onView(withId(R.id.errorText)).check(matches(isDisplayed()))
    useLastButton.perform(click())
    mainText.check(matches(withText("(1"))) // pulls computation without error
    onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))

    clearText()
    typeText("6x3")
    equals() // set result
    checkMainTextMatchesAny(setOf("[9]", "[3]", "[18]", "[2]"))
    typeText("+")
    equals() // set error
    onView(withId(R.id.errorText)).check(matches(isDisplayed()))
    clearText()
    onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
    useLastButton.perform(click()) // pull in previous result
    onView(withId(R.id.errorText)).check(matches(not(isDisplayed())))
    checkMainTextMatchesAny(setOf("[9]+", "[3]+", "[18]+", "[2]+"))
}