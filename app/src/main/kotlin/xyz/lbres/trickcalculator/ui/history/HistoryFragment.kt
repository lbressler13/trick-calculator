package xyz.lbres.trickcalculator.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.kotlinutils.random.ext.nextBoolean
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentHistoryBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.History
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible
import java.util.Date
import kotlin.random.Random

/**
 * Fragment to display computation history, possibly with some level of randomness
 */
class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var sharedViewModel: SharedViewModel

    override var navigateToSettings: Int? = R.id.navigateHistoryToSettings

    // saved to prevent unnecessary changes when opening/closing settings dialog
    private var randomness: Int = 0
    private var history: History = emptyList()

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        randomness = sharedViewModel.historyRandomness
        history = sharedViewModel.history

        setUI()

        binding.closeButton.root.setOnClickListener { closeFragment() }

        return binding.root
    }

    // TODO don't reshuffle when opening/closing settings

    /**
     * Generate history, using the degree of randomness specified in the viewmodel
     */
    private fun getRandomHistory(): History? {
        return when (sharedViewModel.historyRandomness) {
            0 -> sharedViewModel.history // no randomness
            1 -> sharedViewModel.history.shuffled() // shuffled order
            2 -> shuffleValues() // shuffled values
            3 -> generateRandomHistory() // random generation
            else -> sharedViewModel.history
        }
    }

    /**
     * Shuffle history computations and results/errors.
     * Returns a list that contains all computations and results/errors, but not necessarily in their original pari.
     *
     * @return [History]: history where computations and values have been shuffled
     */
    private fun shuffleValues(): History {
        val history = sharedViewModel.history

        val computations: List<StringList> = history.map { it.computation }.shuffled()
        val values: List<Pair<ExactFraction?, String?>> =
            history.map { Pair(it.result, it.error) }.shuffled()

        val shuffledHistory = computations.mapIndexed { index, comp ->
            val valuePair = values[index]
            if (valuePair.first != null) {
                HistoryItem(comp, valuePair.first!!)
            } else {
                HistoryItem(comp, valuePair.second!!)
            }
        }

        return shuffledHistory
    }

    /**
     * Generate randomized history items based on the length of the input.
     * Returns null randomly or if history is empty, to indicate an "error" in retrieving history.
     *
     * @return [History]: possibly null history of generated computations, with same length as real history (if not null)
     */
    private fun generateRandomHistory(): History? {
        val probabilityError = ternaryIf(sharedViewModel.history.isEmpty(), 1f, 0.2f)

        if (Random(Date().time).nextBoolean(probabilityError)) {
            return null
        }

        return List(sharedViewModel.history.size) { generateRandomHistoryItem() }
    }

    /**
     * Set UI based on randomHistory
     */
    private fun setUI() {
        val randomHistory = getRandomHistory()
        when {
            // error
            randomHistory == null -> {
                // set error message to blink
                val blinking: Animation = AlphaAnimation(0.0f, 1.0f)
                blinking.duration = 200

                blinking.startOffset = 10
                blinking.repeatMode = Animation.REVERSE
                blinking.repeatCount = Animation.INFINITE
                binding.errorMessage.startAnimation(blinking)

                binding.itemsRecycler.gone()
                binding.noHistoryMessage.gone()
                binding.errorMessage.visible()
            }
            // empty
            randomHistory.isEmpty() -> {
                binding.itemsRecycler.gone()
                binding.noHistoryMessage.visible()
                binding.errorMessage.gone()
            }
            // non-empty
            else -> {
                val recycler: RecyclerView = binding.itemsRecycler
                val adapter = HistoryItemAdapter(randomHistory)

                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(requireContext())

                binding.itemsRecycler.visible()
                binding.noHistoryMessage.gone()
                binding.errorMessage.gone()
            }
        }
    }

    override fun handleHistoryChange(previousHistory: History) {
        if (previousHistory != sharedViewModel.history) {
            history = previousHistory
            setUI()
        }
    }

    // TODO
//    override fun handleSettingsChange() {
//        AppLogger.e("randomness", Pair(randomness, sharedViewModel.historyRandomness).toString())
//        if (randomness != sharedViewModel.historyRandomness) {
//            randomness = sharedViewModel.historyRandomness
//            setUI()
//        }
//    }
}
