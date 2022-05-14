package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.compute.runComputation
import com.example.trickcalculator.ext.*
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.attributions.AttributionsFragment
import com.example.trickcalculator.ui.history.HistoryFragment
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.ui.shared.Settings
import com.example.trickcalculator.ui.shared.initSettingsDialog

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: SharedViewModel

    private lateinit var computeText: StringList
    private var error: String? = null
    private var usesComputedValue = false

    private var maxDigits = -1

    private val settings = Settings(
        shuffleNumbers = false,
        shuffleOperators = false,
        applyParens = true,
        clearOnError = false,
        applyDecimals = true,
        showSettingsButton = false,
        historyRandomness = 0
    )

    private var devMode = false

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
        viewModel.computeText.observe(viewLifecycleOwner, computeTextObserver)
        viewModel.error.observe(viewLifecycleOwner, errorObserver)
        viewModel.shuffleNumbers.observe(viewLifecycleOwner, shuffleNumbersObserver)
        viewModel.shuffleOperators.observe(viewLifecycleOwner, shuffleOperatorsObserver)
        viewModel.applyParens.observe(viewLifecycleOwner, applyParensObserver)
        viewModel.clearOnError.observe(viewLifecycleOwner, clearOnErrorObserver)
        viewModel.applyDecimals.observe(viewLifecycleOwner, applyDecimalsObserver)
        viewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)
        viewModel.historyRandomness.observe(viewLifecycleOwner, historyRandomnessObserver)
        viewModel.usesComputedValue.observe(viewLifecycleOwner, usesComputedValueObserver)
        viewModel.isDevMode.observe(viewLifecycleOwner, isDevModeObserver)

        initNumpad()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        initActionBar()

        initSettingsDialog(this, viewModel, settings, binding.settingsButton)

        return binding.root
    }

    private val shuffleNumbersObserver: Observer<Boolean> =
        Observer { settings.shuffleNumbers = it }
    private val shuffleOperatorsObserver: Observer<Boolean> =
        Observer { settings.shuffleOperators = it }
    private val applyParensObserver: Observer<Boolean> = Observer { settings.applyParens = it }
    private val clearOnErrorObserver: Observer<Boolean> = Observer { settings.clearOnError = it }
    private val applyDecimalsObserver: Observer<Boolean> = Observer { settings.applyDecimals = it }
    private val usesComputedValueObserver: Observer<Boolean> = Observer { usesComputedValue = it }
    private val showSettingsButtonObserver: Observer<Boolean> = Observer {
        settings.showSettingsButton = it
        binding.settingsButton.isVisible = it || devMode
    }
    private val historyRandomnessObserver: Observer<Int> = Observer { settings.historyRandomness = it }
    private val isDevModeObserver: Observer<Boolean> = Observer {
        devMode = it
        binding.settingsButton.isVisible = it || settings.showSettingsButton
    }

    private val computeTextObserver: Observer<StringList> = Observer {
        computeText = it
        setMainText()
    }

    /**
     * Display current error message, and clear compute data depending on settings
     */
    private val errorObserver: Observer<String?> = Observer {
        error = it
        if (it != null) {
            binding.errorText.text = it
            binding.errorText.visible()

            if (settings.clearOnError) {
                viewModel.resetComputeData(clearError = false)
            }
        } else {
            binding.errorText.gone()
        }
    }

    private fun setMaxDigits() {
        // TODO make this work
        // val textview = binding.mainText
        // textview.text = "0"
        // while (textview.layout.lineCount < 2) {
        //     textview.text = textview.text.toString() + 'c'
        // }

        // maxDigits = textview.text.length - 1
        // textview.text = ""
        maxDigits = 14
    }

    /**
     * Launch AttributionsFragment
     */
    private val infoButtonOnClick: () -> Unit = {
        val newFragment = AttributionsFragment.newInstance()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    /**
     * Launch HistoryFragment
     */
    private val historyButtonOnClick = {
        val newFragment = HistoryFragment.newInstance()

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
            // only include exponent if exp is used
            val operators = when {
                !settings.shuffleOperators -> listOf("+", "-", "x", "/", "^")
                computeText.indexOf("^") == -1 -> listOf(
                    "+",
                    "-",
                    "x",
                    "/"
                ).shuffled() + listOf("^")
                else -> listOf("+", "-", "x", "/", "^").shuffled()
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

            val numberOrder = if (settings.shuffleNumbers) {
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
                        settings.applyParens,
                        settings.applyDecimals
                    )

                viewModel.setComputedValue(computedValue)
                viewModel.setError(null)
                viewModel.addCurrentToHistory()

                viewModel.useComputedAsComputeText()
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
                viewModel.addCurrentToHistory()
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
    private fun initNumpad() {
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

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        actionBar.root.setOnClickListener(null)
    }

    /**
     * Sets the text in the textbox, including ui modifications for first term
     */
    private fun setMainText() {
        val textview: TextView = binding.mainText
        val fullText = computeText.joinToString("")

        if (devMode) {
            if (maxDigits == -1) {
                setMaxDigits()
            }

            if (computeText.isNotEmpty() && usesComputedValue) {
                // val spannableString = addBorder(computeText, maxDigits, requireContext(), textview)
                // textview.text = spannableString
                textview.text = fullText
            } else {
                textview.text = fullText
            }
        } else {
            textview.text = fullText
        }
    }
}
