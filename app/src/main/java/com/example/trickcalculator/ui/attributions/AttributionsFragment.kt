package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentImageAttributionsBinding
import com.example.trickcalculator.ui.shared.Settings
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.shared.initSettingsDialog

/**
 * Information about a single image attribution
 *
 * @param iconResId [Int]: resourceId of the drawable resource for the image
 * @param creator [String]: creator of image on Flaticon
 * @param url [String]: URL to view image
 */
data class Attribution(
    val iconResId: Int,
    val creator: String,
    val url: String
)

// attributions for all images in app
private val allAttributions = listOf(
    Attribution(R.drawable.launcher, "Pixel perfect", "https://www.flaticon.com/free-icon/keys_2891382"),
    Attribution(R.drawable.ic_arrow_left, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/left_3416141"),
    Attribution(R.drawable.ic_close, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/cross_4421536"),
    Attribution(R.drawable.ic_divide, "Smashicons", "www.flaticon.com/free-icon/divide_149702"),
    Attribution(R.drawable.ic_equals, "Freepik", "www.flaticon.com/free-icon/equal_56751"),
    Attribution(R.drawable.ic_history, "IconKanan", "www.flaticon.com/premium-icon/history_2901149"),
    Attribution(R.drawable.ic_info, "Freepik", "www.flaticon.com/free-icon/info-button_64494"),
    Attribution(R.drawable.ic_minus, "Freepik", "www.flaticon.com/free-icon/minus_56889"),
    Attribution(R.drawable.ic_plus, "Freepik", "www.flaticon.com/premium-icon/plus_3524388"),
    Attribution(R.drawable.ic_settings, "Freepik", "https://www.flaticon.com/premium-icon/gear_484613"),
    Attribution(R.drawable.ic_times, "Freepik", "www.flaticon.com/free-icon/multiply-mathematical-sign_43823")
)

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : Fragment() {
    private lateinit var binding: FragmentImageAttributionsBinding
    private lateinit var viewModel: SharedViewModel

    private val settings = Settings(
        shuffleNumbers = false,
        shuffleOperators = false,
        applyParens = true,
        clearOnError = false,
        applyDecimals = true,
        showSettingsButton = false,
        historyRandomness = 0
    )

    companion object {
        fun newInstance() = AttributionsFragment()
    }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageAttributionsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AttributionsAdapter(allAttributions)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        val actionBar: View = (requireActivity() as MainActivity).binding.actionBar.root
        initSettingsDialog(this, viewModel, settings, actionBar)

        // observe changes in viewmodel
        viewModel.shuffleNumbers.observe(viewLifecycleOwner, shuffleNumbersObserver)
        viewModel.shuffleOperators.observe(viewLifecycleOwner, shuffleOperatorsObserver)
        viewModel.applyParens.observe(viewLifecycleOwner, applyParensObserver)
        viewModel.clearOnError.observe(viewLifecycleOwner, clearOnErrorObserver)
        viewModel.applyDecimals.observe(viewLifecycleOwner, applyDecimalsObserver)
        viewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)
        viewModel.historyRandomness.observe(viewLifecycleOwner, historyRandomnessObserver)

        return binding.root
    }

    private val shuffleNumbersObserver: Observer<Boolean> = Observer { settings.shuffleNumbers = it }
    private val shuffleOperatorsObserver: Observer<Boolean> = Observer { settings.shuffleOperators = it }
    private val applyParensObserver: Observer<Boolean> = Observer { settings.applyParens = it }
    private val clearOnErrorObserver: Observer<Boolean> = Observer { settings.clearOnError = it }
    private val applyDecimalsObserver: Observer<Boolean> = Observer { settings.applyDecimals = it }
    private val showSettingsButtonObserver: Observer<Boolean> = Observer { settings.showSettingsButton = it }
    private val historyRandomnessObserver: Observer<Int> = Observer { settings.historyRandomness = it }
}