package xyz.lbres.trickcalculator.ui.history

import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.Matcher
import xyz.lbres.kotlinutils.collection.ext.toConstMultiSet
import xyz.lbres.trickcalculator.R

/**
 * Test representation of a compute history to display in the UI, including methods to run checks on the history.
 * Enables running checks for multiple levels of randomness on the same history.
 * Methods include checking for items to be displayed, checking that items are ordered, and checking that items are shuffled.
 */
class TestHistory {
    private val computeHistory: MutableList<TestHI> = mutableListOf()

    val size: Int
        get() = computeHistory.size

    /**
     * Add a new item to the history
     *
     * @param item [TestHI]: value to add
     */
    fun add(item: TestHI) {
        computeHistory.add(item)
    }

    /**
     * Check that all ViewHolders contain a valid history item.
     * Method of checking is based on level of randomness.
     *
     * @param randomness [Int]: history randomness setting
     * @param throwError [Boolean]: if the function should throw an error when the check fails.
     * If `false`, the function will return `false`. Defaults to `true`.
     * @return [Boolean]: `true` if the check passes, `false` if it fails and [throwError] is set to `false`
     */
    fun checkAllDisplayed(randomness: Int, throwError: Boolean = true): Boolean {
        checkAllowedRandomness(randomness)

        val displayedValues = computeHistory.indices.map { getViewHolderTextAtPosition(it) }
        var allDisplayed = false
        var errorMessage = ""

        when (randomness) {
            0, 1 -> {
                val displayedSet = displayedValues.toConstMultiSet()
                val computeHistorySet = computeHistory.toConstMultiSet()
                allDisplayed = displayedSet == computeHistorySet

                val diff = computeHistorySet - displayedSet
                errorMessage = "ViewHolders with text $diff not found. History: $computeHistory"
            }
            2 -> {
                val computations = computeHistory.map { it.first }.toConstMultiSet()
                val results = computeHistory.map { it.second }.toConstMultiSet()

                val displayedComputations = displayedValues.map { it.first }.toConstMultiSet()
                val displayedResults = displayedValues.map { it.second }.toConstMultiSet()
                allDisplayed = computations == displayedComputations && results == displayedResults

                val computationDiff = computations - displayedComputations
                val resultDiff = results - displayedResults
                errorMessage = "ViewHolders with compute text $computationDiff and result text $resultDiff not found. History: $computeHistory"
            }
        }

        return when {
            allDisplayed -> allDisplayed
            throwError -> throw AssertionError(errorMessage)
            else -> false
        }
    }

    /**
     * Check that all items are displayed in the order in the history.
     *
     * @return [Boolean]: `true` if items are ordered, `false` otherwise
     */
    fun checkDisplayOrdered(): Boolean {
        val displayedValues = computeHistory.indices.map { getViewHolderTextAtPosition(it) }
        return displayedValues == computeHistory
    }

    /**
     * Run a set of checks to determine if the values displayed are shuffled.
     *
     * @param randomness [Int]: history randomness setting
     * @return [Boolean] `true` if the history passes all the checks, `false` otherwise
     */
    fun checkDisplayShuffled(randomness: Int): Boolean {
        checkAllowedRandomness(randomness)

        if (computeHistory.size < 2) {
            return true
        }

        val displayedValues = computeHistory.indices.map { getViewHolderTextAtPosition(it) }
        val computeHistorySet = computeHistory.toConstMultiSet()
        val displayedSet = displayedValues.toConstMultiSet()

        return when (randomness) {
            0 -> checkDisplayOrdered()
            1 -> displayedSet == computeHistorySet && displayedValues != computeHistory
            2 -> {
                val computations = computeHistory.map { it.first }
                val results = computeHistory.map { it.second }

                val displayedComputations = displayedValues.map { it.first }
                val displayedResults = displayedValues.map { it.second }

                val computationsShuffled = computations != displayedComputations
                val resultsShuffled = results != displayedResults
                val pairsShuffled = displayedSet != computeHistorySet

                computationsShuffled && resultsShuffled && pairsShuffled
            }
            else -> false // never matches this case
        }
    }

    /**
     * Check that the randomness is in the allowed range, and throw an exception if it is not
     *
     * @param randomness [Int]: value to check
     */
    private fun checkAllowedRandomness(randomness: Int) {
        if (randomness !in 0..2) {
            throw IllegalArgumentException("Invalid randomness value: $randomness")
        }
    }

    /**
     * Get the computation and result/error text for a history item ViewHolder at a given position
     *
     * @param position [Int]: position of ViewHolder
     * @return [Pair]<String, String>: pair where first value represents the compute text and second value represents result/error
     */
    private fun getViewHolderTextAtPosition(position: Int): Pair<String, String> {
        var computation = ""
        var errorResult = ""

        val getViewHolderText = object : ViewAction {
            override fun getConstraints(): Matcher<View> = isDisplayed()
            override fun getDescription(): String = "retrieving text from viewholder at position $position"

            override fun perform(uiController: UiController?, view: View?) {
                computation = view?.findViewById<TextView>(R.id.computeText)?.text?.toString() ?: ""

                val number = view?.findViewById<TextView>(R.id.resultText)?.text?.toString()
                val error = view?.findViewById<TextView>(R.id.errorText)?.text?.toString()
                errorResult = when {
                    !number.isNullOrBlank() -> number
                    !error.isNullOrBlank() -> error
                    else -> ""
                }
            }
        }

        onView(withId(R.id.itemsRecycler)).perform(
            actionOnHistoryItemAtPosition(position, getViewHolderText),
        )

        return Pair(computation, errorResult)
    }

    override fun toString(): String = computeHistory.toString()
}
