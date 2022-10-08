package xyz.lbres.trickcalculator.ui.main

import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.not
import xyz.lbres.trickcalculator.R

internal fun testLastHistoryItem(mainText: ViewInteraction) {
    val useLastButton = Espresso.onView(withId(R.id.useLastHistoryButton))

    // none
    useLastButton.check(matches(not(isDisplayed())))

    // one value
    clearText()
    typeText("123")
    equals()
    mainText.check(matches(withText("[123]")))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("123")))

    clearText()
    typeText("(1234)")
    equals()
    mainText.check(matches(withText("[1234]")))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("(1234)")))
    // doesn't pull previous item
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("(1234)")))

    clearText()
    typeText("00001.50000")
    equals()
    mainText.check(matches(withText("[1.5]")))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("00001.50000")))

    // multiple values
    clearText()
    var computation = "123+456"
    typeText(computation)
    equals()
    mainText.check(matches(not(withText(computation))))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText(computation)))

    clearText()
    computation = ".5(66+99/33)x22x2+0.001"
    typeText(computation)
    equals()
    mainText.check(matches(not(withText(computation))))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText(computation)))

    // with computed value
    clearText()
    typeText("1234")
    equals()
    typeText("+2")
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("1234")))

    clearText()
    typeText("1234")
    equals()
    typeText("+2")
    equals()
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("[1234]+2")))

    clearText()
    typeText("3+2")
    equals()
    computation = "(5.4+2)-3"
    typeText(computation)
    equals()
    useLastButton.perform(ViewActions.click())
    checkMainTextOptions(
        setOf(
            "[5]$computation", "[1]$computation", "[6]$computation", "[1.5]$computation"
        )
    )

    // after error
    clearText()
    typeText("(1")
    equals() // set error
    mainText.check(matches(withText("(1")))
    Espresso.onView(withId(R.id.errorText))
        .check(matches(isDisplayed()))
    useLastButton.perform(ViewActions.click())
    mainText.check(matches(withText("(1"))) // pulls computation without error
    Espresso.onView(withId(R.id.errorText))
        .check(matches(not(isDisplayed())))
}
