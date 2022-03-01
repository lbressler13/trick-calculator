package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.ext.disableAllChildren
import com.example.trickcalculator.ext.enable
import com.example.trickcalculator.ext.enableAllChildren
import com.example.trickcalculator.runComputation
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

        initButtons()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        initSettingsDialog()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }

        return binding.root
    }

    private val getComputeTextObserver: Observer<StringList> = Observer {
        computeText = it
        if (error == null) {
            binding.mainText.text = it.joinToString("")
        }
    }

    private val getErrorObserver: Observer<String?> = Observer {
        error = it
        if (it != null) {
            binding.mainText.text = it
            setErrorNumpad()
        } else {
            binding.mainText.text = computeText.joinToString("")
        }
    }

    private val getShuffleNumbersObserver: Observer<Boolean> = Observer { shuffleNumbers = it }
    private val getShuffleOperatorsObserver: Observer<Boolean> = Observer { shuffleOperators = it }

    private val infoButtonOnClick = {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, AttributionsFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun initButtons() {
        // number buttons
        binding.oneButton.setOnClickListener { viewModel.appendComputeText("1") }
        binding.twoButton.setOnClickListener { viewModel.appendComputeText("2") }
        binding.threeButton.setOnClickListener { viewModel.appendComputeText("3") }
        binding.fourButton.setOnClickListener { viewModel.appendComputeText("4") }
        binding.fiveButton.setOnClickListener { viewModel.appendComputeText("5") }
        binding.sixButton.setOnClickListener { viewModel.appendComputeText("6") }
        binding.sevenButton.setOnClickListener { viewModel.appendComputeText("7") }
        binding.eightButton.setOnClickListener { viewModel.appendComputeText("8") }
        binding.nineButton.setOnClickListener { viewModel.appendComputeText("9") }
        binding.zeroButton.setOnClickListener { viewModel.appendComputeText("0") }

        // operation buttons
        binding.plusButton.setOnClickListener { viewModel.appendComputeText("+") }
        binding.minusButton.setOnClickListener { viewModel.appendComputeText("-") }
        binding.timesButton.setOnClickListener { viewModel.appendComputeText("x") }
        binding.divideButton.setOnClickListener { viewModel.appendComputeText("/") }

        binding.backspaceButton.setOnClickListener { viewModel.backspaceComputeText() }

        binding.clearButton.setOnClickListener {
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

        binding.equalsButton.setOnClickListener {
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
                        numberOrder
                    )

                viewModel.setComputedValue(computedValue)
                viewModel.useComputedAsComputeText()
                viewModel.setError(null)
            } catch (e: Exception) {
                viewModel.setError(e.message)
            }
        }
    }

    private fun setErrorNumpad() {
        binding.numpadLayout.disableAllChildren()

        val disabledColor = TypedValue()
        requireContext().theme.resolveAttribute(
            R.attr.disabledForeground,
            disabledColor,
            true
        )

        binding.numpadLayout.children.forEach {
            setImageButtonTint(
                it,
                disabledColor.resourceId,
                requireContext()
            )
        }

        // enable button to clear error
        val enabledColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorOnPrimary, enabledColor, true)

        binding.clearButton.enable()
        setImageButtonTint(
            binding.clearButton,
            enabledColor.resourceId,
            requireContext()
        )
    }

    private fun initSettingsDialog() {
        val settingsDialog = MainSettingsDialogFragment()
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val requestKey = requireContext().getString(R.string.key_settings_request)

        // update viewmodel with response from dialog
        childFragmentManager.setFragmentResultListener(
            requestKey,
            viewLifecycleOwner,
            { _: String, result: Bundle ->
                val returnedShuffleNumbers: Boolean = result.getBoolean(numbersKey, shuffleNumbers)
                viewModel.setShuffleNumbers(returnedShuffleNumbers)

                val returnedShuffleOperators: Boolean = result.getBoolean(operatorsKey, shuffleOperators)
                viewModel.setShuffleOperators(returnedShuffleOperators)
            }
        )

        binding.settingsButton.setOnClickListener {
            settingsDialog.arguments = bundleOf(
                numbersKey to shuffleNumbers,
                operatorsKey to shuffleOperators,
            )
            settingsDialog.show(childFragmentManager, MainSettingsDialogFragment.TAG)
        }
    }
}