package com.example.trickcalculator.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.FragmentHistoryBinding
import exactfraction.ExactFraction
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.utils.History
import android.view.animation.Animation
import com.example.trickcalculator.ext.nextBoolean
import com.example.trickcalculator.ui.ActivityFragment
import java.util.*
import kotlin.random.Random


/**
 * Fragment to display computation history, possibly with some level of randomness
 */
class HistoryFragment : ActivityFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var sharedViewModel: SharedViewModel

    private var randomness: Int? = null
    private var history: History? = null
    private var randomHistory: History? = null

    companion object {
        fun newInstance() = HistoryFragment()
    }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.history.observe(viewLifecycleOwner) {
            history = it
            if (history != null && randomness != null) {
                setRandomHistory()
                setUI()
            }
        }
        sharedViewModel.historyRandomness.observe(viewLifecycleOwner) {
            randomness = it
            if (history != null && randomness != null) {
                setRandomHistory()
                setUI()
            }
        }

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    /**
     * Generate history, using the degree of randomness specified in the viewmodel
     */
    private fun setRandomHistory() {
        randomHistory = when (randomness) {
            0 -> history // no randomness
            1 -> history!!.shuffled() // shuffled order
            2 -> shuffleValues() // shuffled values
            3 -> generateRandomHistory() // random generation
            else -> history
        }
    }

    /**
     * Shuffle history computations and results/errors.
     * Returns a list that contains all computations and results/errors, but not necessarily in their original pari.
     *
     * @return [History]: history where computations and values have been shuffled
     */
    private fun shuffleValues(): History {
        val currentHistory = history!!

        val computations: List<String> = currentHistory.map { it.computation }.shuffled()
        val values: List<Pair<ExactFraction?, String?>> = currentHistory.map { Pair(it.result, it.error) }.shuffled()

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
        val probabilityError = if (history.isNullOrEmpty()) 1f else 0.2f

        if (Random(Date().time).nextBoolean(probabilityError)) {
            return null
        }

        return List(history!!.size) { generateRandomHistoryItem() }
    }

    /**
     * Set UI based on randomHistory
     */
    private fun setUI() {
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
            randomHistory!!.isEmpty() -> {
                binding.itemsRecycler.gone()
                binding.noHistoryMessage.visible()
                binding.errorMessage.gone()
            }
            // non-empty
            else -> {
                val recycler: RecyclerView = binding.itemsRecycler
                val adapter = HistoryItemsAdapter(randomHistory!!)

                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(requireContext())

                binding.itemsRecycler.visible()
                binding.noHistoryMessage.gone()
                binding.errorMessage.gone()
            }
        }
    }
}
