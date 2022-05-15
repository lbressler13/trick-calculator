package com.example.trickcalculator.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import kotlin.random.Random


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var sharedViewModel: SharedViewModel

    private var randomness: Int? = null
    private var history: History? = null
    private var randomHistory: History? = null

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // should only get called once
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

        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun setRandomHistory() {
        randomHistory = when (randomness) {
            0 -> history
            1 -> history!!.shuffled()
            2 -> shuffleValues()
            3 -> generateRandomHistory()
            else -> history
        }
    }

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

    private fun generateRandomHistory(): History? {
        val probabilityError = if (history.isNullOrEmpty()) 1f else 0.2f

        if (Random.Default.nextBoolean(probabilityError)) {
            return null
        }

        return List(history!!.size) { generateRandomHistoryItem() }
    }

    private fun setUI() {
        when {
            randomHistory == null -> {
                val blinking: Animation = AlphaAnimation(0.0f, 1.0f)
                blinking.duration = 200 // You can manage the blinking time with this parameter

                blinking.startOffset = 10
                blinking.repeatMode = Animation.REVERSE
                blinking.repeatCount = Animation.INFINITE
                binding.errorLayout.startAnimation(blinking)

                binding.itemsRecycler.gone()
                binding.noHistoryMessage.gone()
                binding.errorLayout.visible()
            }
            randomHistory!!.isEmpty() -> {
                binding.itemsRecycler.gone()
                binding.noHistoryMessage.visible()
                binding.errorLayout.gone()
            }
            else -> {
                val recycler: RecyclerView = binding.itemsRecycler
                val adapter = HistoryItemsAdapter(randomHistory!!)

                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(requireContext())

                binding.itemsRecycler.visible()
                binding.noHistoryMessage.gone()
                binding.errorLayout.gone()
            }
        }
    }
}
