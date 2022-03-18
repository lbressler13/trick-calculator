package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.compute.runComputation
import com.example.trickcalculator.ext.*
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.attributions.AttributionsFragment
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: SharedViewModel

    private lateinit var computeText: StringList
    private var error: String? = null

    // settings
    private var shuffleNumbers: Boolean = false
    private var shuffleOperators: Boolean = true
    private var applyParens: Boolean = true
    private var clearOnError: Boolean = true
    private var applyDecimals: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // observe changes in viewmodel
        viewModel.getComputeText().observe(viewLifecycleOwner, getComputeTextObserver)
        viewModel.getError().observe(viewLifecycleOwner, getErrorObserver)
        viewModel.getShuffleNumbers().observe(viewLifecycleOwner, getShuffleNumbersObserver)
        viewModel.getShuffleOperators().observe(viewLifecycleOwner, getShuffleOperatorsObserver)
        viewModel.getApplyParens().observe(viewLifecycleOwner, getApplyParensObserver)
        viewModel.getClearOnError().observe(viewLifecycleOwner, getClearOnErrorObserver)
        viewModel.getApplyDecimals().observe(viewLifecycleOwner, getApplyDecimalsObserver)

        initButtons()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }

        (requireActivity() as MainActivity).binding.actionBar.root.setOnClickListener(null)

        return binding.root
    }

    private val getComputeTextObserver: Observer<StringList> = Observer {
        computeText = it
        binding.mainText.text = it.joinToString("")
    }

    /**
     * Display current error message, and clear compute data depending on settings
     */
    private val getErrorObserver: Observer<String?> = Observer {
        error = it
        if (it != null) {
            binding.errorText.text = it
            binding.errorText.visible()

            if (clearOnError) {
                viewModel.resetComputeData(clearError = false)
            }
        } else {
            binding.errorText.gone()
        }
    }

    private val getShuffleNumbersObserver: Observer<Boolean> = Observer { shuffleNumbers = it }
    private val getShuffleOperatorsObserver: Observer<Boolean> = Observer { shuffleOperators = it }
    private val getApplyParensObserver: Observer<Boolean> = Observer { applyParens = it }
    private val getClearOnErrorObserver: Observer<Boolean> = Observer { clearOnError = it }
    private val getApplyDecimalsObserver: Observer<Boolean> = Observer { applyDecimals = it }

    /**
     * Launch AttributionsFragment
     */
    private val infoButtonOnClick = {
        val numbersKey = requireContext().getString(R.string.key_shuffle_numbers)
        val operatorsKey = requireContext().getString(R.string.key_shuffle_operators)
        val parensKey = requireContext().getString(R.string.key_apply_parens)
        val clearOnErrorKey = requireContext().getString(R.string.key_clear_on_error)

        val currentSettings = bundleOf(
            numbersKey to shuffleNumbers,
            operatorsKey to shuffleOperators,
            parensKey to applyParens,
            clearOnErrorKey to clearOnError
        )

        val newFragment = AttributionsFragment.newInstance()
        newFragment.arguments = currentSettings

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Sets operation order and number order, and runs computation.
     * Updates viewmodel with resulting computed value or error message
     */
    private val equalsButtonOnClick = {
        if (computeText.isNotEmpty()) {
            viewModel.finalizeComputeText()

            // set action for each operator
            val operators = if (shuffleOperators) {
                listOf("+", "-", "x", "/", "^").shuffled()
            } else {
                listOf("+", "-", "x", "/", "^")
            }

            // maps operator symbols to their given functions
            val performOperation: OperatorFunction = { leftValue, rightValue, operator ->
                when (operator) {
                    operators[0] -> leftValue + rightValue
                    operators[1] -> leftValue - rightValue
                    operators[2] -> leftValue * rightValue
                    operators[3] -> leftValue / rightValue
                    operators[4] -> leftValue.pow(rightValue)
                    else -> throw Exception("Invalid operator $operator")
                }
            }

            val operatorRounds = listOf(
                listOf(operators[4]), // exponent
                operators.subList(2, 4), // multiply and divide
                operators.subList(0, 2), // add and subtract
            )

            val numberOrder = if (shuffleNumbers) {
                (0..9).shuffled()
            } else {
                (0..9).toList()
            }

            // try to run computation, and update compute text and error message
            try {
                val computedValue: ExactFraction =
                    runComputation(
                        computeText,
                        operatorRounds,
                        performOperation,
                        numberOrder,
                        applyParens,
                        applyDecimals
                    )

                viewModel.setComputedValue(computedValue)
                viewModel.useComputedAsComputeText()
                viewModel.setError(null)
            } catch (e: Exception) {
                viewModel.restoreComputeText()

                val error: String = if (e.message == null) {
                    "Computation error"
                } else {
                    var message: String = e.message!!.trim()

                    val firstChar = message[0]
                    if (firstChar.isLowerCase()) {
                        message = message.replaceFirst(firstChar, firstChar.uppercaseChar())
                    }
                    message
                }

                viewModel.setError("Error: $error")
            }
        }
    }

    /**
     * Add a string to the compute text
     *
     * @param addText [String]: text to add
     */
    private fun genericAddComputeOnClick(addText: String) {
        viewModel.appendComputeText(addText)
        if (error != null) {
            viewModel.setError(null)
        }
    }

    /**
     * Initialize all buttons in numpad
     */
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
        binding.clearButton.setOnClickListener { viewModel.resetComputeData() }
        binding.equalsButton.setOnClickListener { equalsButtonOnClick() }
        binding.backspaceButton.setOnClickListener {
            viewModel.setError(null)
            viewModel.backspaceComputeText()
        }
    }
}
