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
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentHistoryBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible

/**
 * Fragment to display computation history, possibly with some level of randomness
 */
class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override val navigateToSettings = R.id.navigateHistoryToSettings

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
        historyViewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

        historyViewModel.updateValues(sharedViewModel.historyRandomness, sharedViewModel.history)
        setUI()

        binding.closeButton.root.setOnClickListener { closeFragment() }

        return binding.root
    }

    /**
     * Set UI based on randomized history
     */
    private fun setUI() {
        val randomHistory = historyViewModel.randomizedHistory
        val displayError = randomHistory?.isEmpty() == true && historyViewModel.randomness == 3

        when {
            // error
            displayError -> {
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
            randomHistory?.isEmpty() == true -> {
                binding.itemsRecycler.gone()
                binding.noHistoryMessage.visible()
                binding.errorMessage.gone()
            }
            // non-empty
            else -> {
                val recycler: RecyclerView = binding.itemsRecycler
                val adapter = HistoryItemAdapter(randomHistory ?: emptyList())

                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(requireContext())

                binding.itemsRecycler.visible()
                binding.noHistoryMessage.gone()
                binding.errorMessage.gone()
            }
        }
    }

    /**
     * Update UI when history is cleared
     */
    override fun handleHistoryCleared() {
        historyViewModel.updateValues(sharedViewModel.historyRandomness, sharedViewModel.history)
        setUI()
    }

    /**
     * Clear values in history viewmodel when destroying fragment
     */
    override fun onDestroy() {
        super.onDestroy()
        historyViewModel.clearValues()
    }
}
