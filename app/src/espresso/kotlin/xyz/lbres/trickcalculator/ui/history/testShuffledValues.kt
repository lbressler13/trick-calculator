package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.testutils.closeFragment
import xyz.lbres.trickcalculator.testutils.openSettingsFragment

private const val longText = "(123456789/12.898989898989+(98765x432100)-555555555x13131313131313)x3"

fun testShuffleOperators() {
    val history = TestHistory()

    history.add(generateHI("3") { getMainTextResult() })
    history.add(generateHI("-1/2", previousResult = "3") { getMainTextResult() })

    // long decimal
    history.add(generateHI("0.123456") { getMainTextResult() })

    checkRandomness(history, 0)

    history.add(generateHI("1+2-2/3x1") { getMainTextResult() })
    history.add(generateHI("(1+2)(4-2)") { getMainTextResult() })
    history.add(generateHI("2x(1-9)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI(longText) { getMainTextResult() })

    // with error
    history.add(generateHI("4+5()-44") { "Syntax error" })

    history.add(generateHI("4+(5)-44") { getMainTextResult() })
    history.add(generateHI(".00000003") { getMainTextResult() })
    history.add(generateHI("55^6") { getMainTextResult() })

    // multiple errors
    history.add(generateHI("400/3..3") { "Syntax error" })
    history.add(generateHI("(500-5))-6") { "Syntax error" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

fun testShuffleNumbers() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.shuffleNumbersSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    // one digit
    history.add(generateHI("1") { getMainTextResult() })
    history.add(generateHI("22.22222") { getMainTextResult() })
    history.add(generateHI("00000") { getMainTextResult() })

    checkRandomness(history, 0)

    // multiple digits
    history.add(generateHI("123.456") { getMainTextResult() })
    history.add(generateHI("8900000") { getMainTextResult() })

    checkRandomness(history, 0)
    checkRandomness(history, 1)

    // operators
    history.add(generateHI("1+2-2/3x1") { getMainTextResult() })
    history.add(generateHI("(1+2)(4-2)") { getMainTextResult() })
    history.add(generateHI("2x(1-9)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI("55+16/3-4-3+23/66x44(20+30)") { getMainTextResult() })

    // with error
    history.add(generateHI("4+5()-44") { "Syntax error" })

    history.add(generateHI("4+(5)-44") { getMainTextResult() })
    history.add(generateHI(".00000003") { getMainTextResult() })
    history.add(generateHI("55^6") { getMainTextResult() })
    history.add(generateHI(longText) { getMainTextResult() })

    // multiple errors
    history.add(generateHI("400/3..3") { "Syntax error" })
    history.add(generateHI("(500-5))-6") { "Syntax error" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

fun testShuffleComputation() {
    openSettingsFragment()
    onView(withId(R.id.shuffleOperatorsSwitch)).perform(click())
    onView(withId(R.id.shuffleComputationSwitch)).perform(click())
    closeFragment()

    val history = TestHistory()

    history.add(generateHI("3") { getMainTextResult() })
    history.add(generateHI("-1/2", previousResult = "3") { getMainTextResult() })

    // long decimal
    history.add(generateHI("0.123456") { getMainTextResult() })

    checkRandomness(history, 0)

    history.add(generateHI("1+2-2/3x1") { getMainTextResult() })
    history.add(generateHI("(1+2)(4-2)") { getMainTextResult() })
    history.add(generateHI("2x(1-9)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI("1.2x5(4)") { getMainTextResult() })
    history.add(generateHI(longText) { getMainTextResult() })

    // with error
    history.add(generateHI("4+5()-44") { "Syntax error" })

    history.add(generateHI("4+(5)-44") { getMainTextResult() })
    history.add(generateHI(".00000003") { getMainTextResult() })
    history.add(generateHI("55^6") { getMainTextResult() })

    // multiple errors
    history.add(generateHI("400/3..3") { "Syntax error" })
    history.add(generateHI("(500-5))-6") { "Syntax error" })

    checkRandomness(history, 0)
    checkRandomness(history, 1)
    checkRandomness(history, 2)
}

/**
 * Get the text from the main TextView.
 * Explicitly for using randomized results to generate history items.
 * For other uses, use a [Matcher] or TextSaver
 *
 * @return [String]
 */
private fun getMainTextResult(): String {
    var text = ""

    val viewAction = object : ViewAction {
        override fun getConstraints(): Matcher<View> = withId(R.id.mainText)
        override fun getDescription(): String = "retrieving main text"

        override fun perform(uiController: UiController?, view: View?) {
            text = (view as TextView).text?.toString() ?: ""
        }
    }

    onView(withId(R.id.mainText)).perform(viewAction)

    if (text.startsWith('[')) {
        return text.substring(1, text.lastIndex) // strip [] around result
    }
    return text
}
