package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
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
import android.content.res.ColorStateList
import android.util.Log

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: SharedViewModel

    private lateinit var computeText: StringList
    private var error: String? = null
    private var usesComputedValue = false

    // settings
    private var shuffleNumbers: Boolean = false
    private var shuffleOperators: Boolean = true
    private var applyParens: Boolean = true
    private var clearOnError: Boolean = false
    private var applyDecimals: Boolean = true

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
        viewModel.usesComputedValue.observe(viewLifecycleOwner, usesComputedValueObserver)
        viewModel.isDevMode.observe(viewLifecycleOwner, isDevModeObserver)

        initButtons()
        initDevModeSwitch()
        binding.mainText.movementMethod = ScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        initActionBar()

        return binding.root
    }

    private val shuffleNumbersObserver: Observer<Boolean> = Observer { shuffleNumbers = it }
    private val shuffleOperatorsObserver: Observer<Boolean> = Observer { shuffleOperators = it }
    private val applyParensObserver: Observer<Boolean> = Observer { applyParens = it }
    private val clearOnErrorObserver: Observer<Boolean> = Observer { clearOnError = it }
    private val applyDecimalsObserver: Observer<Boolean> = Observer { applyDecimals = it }
    private val usesComputedValueObserver: Observer<Boolean> = Observer { usesComputedValue = it }
    private val isDevModeObserver: Observer<Boolean> = Observer { devMode = it }

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

            if (clearOnError) {
                viewModel.resetComputeData(clearError = false)
            }
        } else {
            binding.errorText.gone()
        }
    }

    /**
     * Set UI and onCheckChangeListener
     */
    private fun initDevModeSwitch() {
        val switch = (requireActivity() as MainActivity).binding.actionBar.devModeSwitch
        switch.setOnCheckedChangeListener { view, isChecked -> viewModel.setIsDevMode(isChecked) }

        val checkedColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.actionBarSwitchTrackCheckedColor, checkedColor, true)
        val uncheckedColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.actionBarSwitchTrackUncheckedColor, uncheckedColor, true)

        val buttonStates = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf()
            ), intArrayOf(
                uncheckedColor.data,
                checkedColor.data,
                uncheckedColor.data
            )
        )
        switch.trackDrawable.setTintList(buttonStates)

        val isDevBuild = BuildOptions.buildType == "dev"
        switch.isChecked = isDevBuild
        viewModel.setIsDevMode(BuildOptions.buildType == "dev")
    }

    /**
     * Launch AttributionsFragment
     */
    private val infoButtonOnClick: () -> Unit = {
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
            // only include exponent if exp is used
            val operators = when {
                !shuffleOperators -> listOf("+", "-", "x", "/", "^")
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

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        actionBar.root.setOnClickListener(null)

        // only visible and enabled in dev flavor
        actionBar.devModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setIsDevMode(isChecked)
        }
    }

    /**
     * Sets the text in the textbox, including ui modifications for first term
     */
    private fun setMainText() {
        if (devMode) {
            if (computeText.isNotEmpty() && usesComputedValue) {
                val textColor = TypedValue()
                requireContext().theme.resolveAttribute(R.attr.colorOnPrimary, textColor, true)

                val text = computeText.joinToString("")
                val parentWidth = binding.mainText.width - binding.mainText.paddingStart - binding.mainText.paddingEnd

                val spannableString = SpannableString(text)
                spannableString.setSpan(
                    BorderSpan(textColor.data, parentWidth),
                    0,
                    computeText[0].length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                binding.mainText.text = spannableString
            } else {
                binding.mainText.text = computeText.joinToString("")
            }
        } else {
            binding.mainText.text = computeText.joinToString("")
        }
    }
}
