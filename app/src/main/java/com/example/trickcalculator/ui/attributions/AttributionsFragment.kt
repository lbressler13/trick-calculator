package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentImageAttributionsBinding
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.shared.SharedSettingsDialog
import com.example.trickcalculator.ui.shared.SharedViewModel

data class Attribution(
    val iconResId: Int,
    val creator: String,
    val url: String
)

private val allAttributions = listOf(
    Attribution(R.drawable.ic_arrow_left, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/left_3416141"),
    Attribution(R.drawable.ic_close, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/cross_4421536"),
    Attribution(R.drawable.ic_divide, "Smashicons", "www.flaticon.com/free-icon/divide_149702"),
    Attribution(R.drawable.ic_equals, "Freepik", "www.flaticon.com/free-icon/equal_56751"),
    Attribution(R.drawable.ic_info, "Freepik", "www.flaticon.com/free-icon/info-button_64494"),
    Attribution(R.drawable.ic_minus, "Freepik", "www.flaticon.com/free-icon/minus_56889"),
    Attribution(R.drawable.ic_plus, "Freepik", "www.flaticon.com/premium-icon/plus_3524388"),
    Attribution(R.drawable.ic_settings, "Freepik", "www.flaticon.com/premium-icon/gear_484613"),
    Attribution(R.drawable.ic_times, "Freepik", "www.flaticon.com/free-icon/multiply-mathematical-sign_43823")
)

class AttributionsFragment : Fragment() {
    private lateinit var binding: FragmentImageAttributionsBinding
    private lateinit var viewModel: SharedViewModel

    // settings
    private var shuffleNumbers: Boolean = false
    private var shuffleOperators: Boolean = true
    private var applyParens: Boolean = true
    private var clearOnError: Boolean = true

    companion object {
        fun newInstance() = AttributionsFragment()
    }

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

        viewModel.getShuffleNumbers().observe(viewLifecycleOwner, getShuffleNumbersObserver)
        viewModel.getShuffleOperators().observe(viewLifecycleOwner, getShuffleOperatorsObserver)
        viewModel.getApplyParens().observe(viewLifecycleOwner, getApplyParensObserver)
        viewModel.getClearOnError().observe(viewLifecycleOwner, getClearOnErrorObserver)

        (requireActivity() as MainActivity).binding.actionBar.settingsButton.visible()
        initSettingsDialog()

        return binding.root
    }

    private val getShuffleNumbersObserver: Observer<Boolean> = Observer { shuffleNumbers = it }
    private val getShuffleOperatorsObserver: Observer<Boolean> = Observer { shuffleOperators = it }
    private val getApplyParensObserver: Observer<Boolean> = Observer { applyParens = it }
    private val getClearOnErrorObserver: Observer<Boolean> = Observer { clearOnError = it }

    private fun initSettingsDialog() {
        val settingsDialog = SharedSettingsDialog()
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val parensKey = requireContext().getString(R.string.key_apply_parens)
        val clearOnErrorKey = requireContext().getString(R.string.key_clear_on_error)
        val requestKey = requireContext().getString(R.string.key_settings_request)

//        val shuffleNumbers = arguments?.getBoolean(numbersKey) ?: false
//        val shuffleOperators = arguments?.getBoolean(operatorsKey) ?: true
//        val applyParens = arguments?.getBoolean(parensKey) ?: true
//        val clearOnError = arguments?.getBoolean(clearOnErrorKey) ?: true

        // update viewmodel with response from dialog
        childFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner,
            { _: String, result: Bundle ->
                val returnedShuffleNumbers: Boolean = result.getBoolean(numbersKey, shuffleNumbers)
                viewModel.setShuffleNumbers(returnedShuffleNumbers)

                val returnedShuffleOperators: Boolean =
                    result.getBoolean(operatorsKey, shuffleOperators)
                viewModel.setShuffleOperators(returnedShuffleOperators)

                val returnedApplyParens: Boolean = result.getBoolean(parensKey, applyParens)
                viewModel.setApplyParens(returnedApplyParens)

                val returnedClearOnError: Boolean = result.getBoolean(clearOnErrorKey, clearOnError)
                viewModel.setClearOnError(returnedClearOnError)
            }
        )

        val settingsButton: View = (requireActivity() as MainActivity).binding.actionBar.settingsButton

        settingsButton.setOnClickListener {
            settingsDialog.arguments = bundleOf(
                numbersKey to shuffleNumbers,
                operatorsKey to shuffleOperators,
                parensKey to applyParens,
                clearOnErrorKey to clearOnError
            )
            settingsDialog.show(childFragmentManager, SharedSettingsDialog.TAG)
        }
    }
}