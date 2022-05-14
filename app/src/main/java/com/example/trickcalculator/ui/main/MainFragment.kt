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
import com.example.trickcalculator.ui.history.HistoryItem
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.ui.shared.Settings
import com.example.trickcalculator.ui.shared.initSettingsDialog
import com.example.trickcalculator.ui.shared.initSettingsObservers

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var computationViewModel: ComputationViewModel
    private lateinit var sharedViewModel: SharedViewModel

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
        computationViewModel = ViewModelProvider(requireActivity())[ComputationViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // observe changes in viewmodels
        computationViewModel.computeText.observe(viewLifecycleOwner, computeTextObserver)
        computationViewModel.error.observe(viewLifecycleOwner, errorObserver)
        computationViewModel.usesComputedValue.observe(viewLifecycleOwner, usesComputedValueObserver)
        computationViewModel.lastHistoryItem.observe(viewLifecycleOwner, lastHistoryItemObserver)
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)
        // additional observer to show/hide settings button
        sharedViewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)
        sharedViewModel.isDevMode.observe(viewLifecycleOwner, isDevModeObserver)

        initNumpad()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        initActionBar()

        initSettingsDialog(this, sharedViewModel, settings, binding.settingsButton)

        return binding.root
    }

    private val usesComputedValueObserver: Observer<Boolean> = Observer { usesComputedValue = it }
    private val lastHistoryItemObserver: Observer<HistoryItem> = Observer {
        if (it != null) {
            sharedViewModel.addToHistory(it)
            computationViewModel.removeLastHistory()
        }
    }
    private val showSettingsButtonObserver: Observer<Boolean> = Observer {
        binding.settingsButton.isVisible = it || devMode
    }
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
            binding.errorText.text = "Error: $it"
            binding.errorText.visible()

            if (settings.clearOnError) {
                computationViewModel.resetComputeData(clearError = false)
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
            computationViewModel.finalizeComputeText()

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

                computationViewModel.setComputedValue(computedValue)
                computationViewModel.setError(null)
                computationViewModel.setLastHistoryItem()

                computationViewModel.useComputedAsComputeText()
            } catch (e: Exception) {
                computationViewModel.restoreComputeText()

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

                computationViewModel.setError(error)
                computationViewModel.setLastHistoryItem()
            }
        }
    }

    /**
     * Add a string to the compute text
     *
     * @param addText [String]: text to add
     */
    private fun genericAddComputeOnClick(addText: String) {
        computationViewModel.appendComputeText(addText)
        if (error != null) {
            computationViewModel.setError(null)
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
        binding.clearButton.setOnClickListener { computationViewModel.resetComputeData() }
        binding.equalsButton.setOnClickListener { equalsButtonOnClick() }
        binding.backspaceButton.setOnClickListener {
            computationViewModel.setError(null)
            computationViewModel.backspaceComputeText()
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
