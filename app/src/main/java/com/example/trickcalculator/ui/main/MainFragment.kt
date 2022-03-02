package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.compute.runComputation
import com.example.trickcalculator.ext.*
import com.example.trickcalculator.ui.attributions.AttributionsFragment
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.setImageButtonTint

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var computeText: StringList
    private var error: String? = null

    // settings
    private var shuffleNumbers: Boolean = false
    private var shuffleOperators: Boolean = true
    private var applyParens: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // observe changes in viewmodel
        viewModel.getComputeText().observe(viewLifecycleOwner, getComputeTextObserver)
        viewModel.getError().observe(viewLifecycleOwner, getErrorObserver)
        viewModel.getShuffleNumbers().observe(viewLifecycleOwner, getShuffleNumbersObserver)
        viewModel.getShuffleOperators().observe(viewLifecycleOwner, getShuffleOperatorsObserver)
        viewModel.getApplyParens().observe(viewLifecycleOwner, getApplyParensObserver)

        initButtons()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        initSettingsDialog()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }

        return binding.root
    }

    private val getComputeTextObserver: Observer<StringList> = Observer {
        computeText = it
        binding.mainText.text = it.joinToString("")
    }

    private val getErrorObserver: Observer<String?> = Observer {
        error = it
        if (it != null) {
            binding.errorText.text = it
            binding.errorText.visible()
        } else {
            binding.errorText.gone()
        }
    }

    private val getShuffleNumbersObserver: Observer<Boolean> = Observer { shuffleNumbers = it }
    private val getShuffleOperatorsObserver: Observer<Boolean> = Observer { shuffleOperators = it }
    private val getApplyParensObserver: Observer<Boolean> = Observer { applyParens = it }

    private val infoButtonOnClick = {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, AttributionsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private val clearButtonOnClick = {
        viewModel.resetComputeData()

        val enabledColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnPrimary, enabledColor, true)

        binding.numpadLayout.enableAllChildren()
        binding.numpadLayout.children.forEach {
            setImageButtonTint(
                it,
                enabledColor.resourceId,
                requireContext()
            )
        }
    }

    // set op order, num order, run computation, and update viewmodel
    private val equalsButtonOnClick = {
        if (computeText.isNotEmpty()) {
            // set action for each operator
            val operators = if (shuffleOperators) {
                listOf("+", "-", "x", "/").shuffled()
            } else {
                listOf("+", "-", "x", "/")
            }
            val performOperation: OperatorFunction = { leftValue, rightValue, operator ->
                when (operator) {
                    operators[0] -> leftValue + rightValue
                    operators[1] -> leftValue - rightValue
                    operators[2] -> leftValue * rightValue
                    operators[3] -> leftValue / rightValue
                    else -> 0
                }
            }

            val numberOrder = if (shuffleNumbers) {
                (0..9).shuffled()
            } else {
                (0..9).toList()
            }

            try {
                val computedValue: Int =
                    runComputation(
                        computeText,
                        operators.subList(2, 4), // multiply and divide ops
                        operators.subList(0, 2), // add and subtract ops
                        performOperation,
                        numberOrder,
                        applyParens
                    )

                viewModel.setComputedValue(computedValue)
                viewModel.useComputedAsComputeText()
                viewModel.setError(null)
            } catch (e: Exception) {
                viewModel.setError("Error: ${e.message}")
            }
        }
    }

    private fun genericAddComputeOnClick(addText: String) {
        viewModel.appendComputeText(addText)
        if (error != null) {
            viewModel.setError(null)
        }
    }

    private fun initButtons() {
        binding.numpadLayout.children.forEach {
            if (it is Button && it.text != null && it != binding.clearButton) {
                it.setOnClickListener { _ -> genericAddComputeOnClick(it.text.toString()) }
            }
        }

        // operation buttons
        binding.plusButton.setOnClickListener { genericAddComputeOnClick("+") }
        binding.minusButton.setOnClickListener { genericAddComputeOnClick("-") }
        binding.timesButton.setOnClickListener { genericAddComputeOnClick("x") }
        binding.divideButton.setOnClickListener { genericAddComputeOnClick("/") }

        // functional buttons
        binding.clearButton.setOnClickListener { clearButtonOnClick() }
        binding.equalsButton.setOnClickListener { equalsButtonOnClick() }
        binding.backspaceButton.setOnClickListener {
            viewModel.setError(null)
            viewModel.backspaceComputeText()
        }
    }

    private fun initSettingsDialog() {
        val settingsDialog = MainSettingsDialogFragment()
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val parensKey = requireContext().getString(R.string.key_apply_parens)
        val requestKey = requireContext().getString(R.string.key_settings_request)

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
            }
        )

        binding.settingsButton.setOnClickListener {
            settingsDialog.arguments = bundleOf(
                numbersKey to shuffleNumbers,
                operatorsKey to shuffleOperators,
                parensKey to applyParens
            )
            settingsDialog.show(childFragmentManager, MainSettingsDialogFragment.TAG)
        }
    }
}