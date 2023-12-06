package xyz.lbres.trickcalculator.ui.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.simpleIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.compute.runComputation
import xyz.lbres.trickcalculator.databinding.FragmentCalculatorBinding
import xyz.lbres.trickcalculator.ext.view.gone
import xyz.lbres.trickcalculator.ext.view.visible
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.history.HistoryItem
import xyz.lbres.trickcalculator.ui.history.HistoryViewModel
import xyz.lbres.trickcalculator.ui.settings.SettingsViewModel
import xyz.lbres.trickcalculator.utils.OperatorFunction
import xyz.lbres.trickcalculator.utils.seededShuffled

/**
 * Fragment to display main calculator functionality
 */
class CalculatorFragment : BaseFragment() {
    private lateinit var binding: FragmentCalculatorBinding
    private lateinit var computationViewModel: ComputationViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var historyViewModel: HistoryViewModel
    override val navigateToSettings = R.id.navigateCalculatorToSettings

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalculatorBinding.inflate(layoutInflater)
        computationViewModel = ViewModelProvider(requireActivity())[ComputationViewModel::class.java]
        settingsViewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]
        historyViewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

        // init UI
        initNumpad()
        binding.mainText.movementMethod = UnprotectedScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.settingsButton.setOnClickListener { settingsButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        binding.useLastHistoryButton.setOnClickListener { useLastHistoryItemOnClick() }
        updateUI()

        return binding.root
    }

    /**
     * Update visibility of buttons when fragment is resumed
     */
    override fun onResume() {
        super.onResume()
        binding.settingsButton.isVisible = settingsViewModel.showSettingsButton
        binding.useLastHistoryButton.isVisible = historyViewModel.history.isNotEmpty()
    }

    /**
     * Launch AttributionsFragment
     */
    private val infoButtonOnClick = {
        requireBaseActivity().runNavAction(R.id.navigateCalculatorToAttribution)
    }

    /**
     * Launch HistoryFragment
     */
    private val historyButtonOnClick = {
        val initialLoadKey = getString(R.string.initial_fragment_load_key)
        val args = bundleOf(initialLoadKey to true)

        requireBaseActivity().runNavAction(R.id.navigateCalculatorToHistory, args)
    }

    /**
     * Use last history item as current computation
     */
    private val useLastHistoryItemOnClick = {
        val item = historyViewModel.history.lastOrNull()
        if (item != null) {
            computationViewModel.useHistoryItemAsComputeText(item)

            updateUI()
            scrollTextToTop()
        }
    }

    /**
     * Launch SettingsFragment
     */
    private val settingsButtonOnClick = {
        requireBaseActivity().runNavAction(R.id.navigateCalculatorToSettings)
    }

    /**
     * Sets operation order and number order, and runs computation.
     * Updates viewmodel with resulting computed value or error message
     */
    private val equalsButtonOnClick = {
        if (computationViewModel.computeText.isNotEmpty()) {
            // set action for each operator
            // only include exponent if exp is used
            val operators = when {
                !settingsViewModel.shuffleOperators -> listOf("+", "-", "x", "/", "^")
                !computationViewModel.computeText.contains("^") -> listOf("+", "-", "x", "/").seededShuffled() + "^"
                else -> listOf("+", "-", "x", "/", "^").seededShuffled()
            }

            // maps operator symbols to their given functions
            val performOperation: OperatorFunction = { leftValue, rightValue, operator ->
                when (operator) {
                    operators[0] -> leftValue + rightValue
                    operators[1] -> leftValue - rightValue
                    operators[2] -> leftValue * rightValue
                    operators[3] -> leftValue / rightValue
                    operators[4] -> leftValue.pow(rightValue)
                    else -> throw Exception("Invalid operator: $operator")
                }
            }

            val operatorRounds = listOf(
                listOf(operators[4]), // exponent
                operators.subList(2, 4), // multiply and divide
                operators.subList(0, 2) // add and subtract
            )

            val numberOrder = simpleIf(settingsViewModel.shuffleNumbers, (0..9).seededShuffled(), (0..9).toList())

            var newHistoryItem: HistoryItem?

            // try to run computation, and update compute text and error message
            try {
                val computedValue: ExactFraction =
                    runComputation(
                        computationViewModel.computedValue,
                        computationViewModel.computeText,
                        operatorRounds,
                        performOperation,
                        numberOrder,
                        settingsViewModel.applyParens,
                        settingsViewModel.applyDecimals,
                        settingsViewModel.randomizeSigns,
                        settingsViewModel.shuffleComputation
                    )

                newHistoryItem = computationViewModel.setResult(null, computedValue)
                updateUI()
                scrollTextToTop()
            } catch (e: Exception) {
                val error: String = if (e.message == null) {
                    "Computation error"
                } else {
                    var message: String = e.message!!.trim()

                    if (message.isNotEmpty() && message[0].isLowerCase()) {
                        val firstChar = message[0]
                        message = message.replaceFirst(firstChar, firstChar.uppercaseChar())
                    }

                    message
                }

                newHistoryItem = computationViewModel.setResult(error, null, settingsViewModel.clearOnError)
                updateUI()
            }

            if (newHistoryItem != null) {
                historyViewModel.addToHistory(newHistoryItem)
            }

            binding.useLastHistoryButton.isVisible = historyViewModel.history.isNotEmpty()
        }
    }

    /**
     * Add a string to the compute text
     *
     * @param addText [String]: text to add
     */
    private fun genericAddComputeOnClick(addText: String) {
        computationViewModel.appendComputeText(addText)
        updateUI()
        scrollTextToBottom()
    }

    /**
     * Initialize all buttons in numpad
     */
    private fun initNumpad() {
        // text buttons
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
        binding.clearButton.setOnClickListener {
            computationViewModel.resetComputeData()
            updateUI()
        }
        binding.equalsButton.setOnClickListener { equalsButtonOnClick() }
        binding.backspaceButton.setOnClickListener {
            computationViewModel.backspaceComputeText()
            updateUI()
            scrollTextToBottom()
        }
    }

    /**
     * Updates the UI for the current error, computed value, and compute text.
     * Includes adding brackets around computed value, if applicable
     */
    private fun updateUI() {
        val textview: TextView = binding.mainText
        val error = computationViewModel.error

        // update error
        if (error != null) {
            @SuppressLint("SetTextI18n")
            binding.errorText.text = "Error: $error"
            binding.errorText.visible()
        } else {
            binding.errorText.gone()
        }

        // update main textview
        var fullText = computationViewModel.computeText.joinToString("")
        // add computed value
        val computedString = computationViewModel.computedValue?.toDecimalString()
        if (computedString != null) {
            fullText = "[$computedString]$fullText"
        }

        textview.text = fullText
    }

    /**
     * Hide useLastHistory button when history is cleared
     */
    override fun handlePostDevTools() {
        if (historyViewModel.history.isEmpty()) {
            binding.useLastHistoryButton.gone()
        }
    }

    /**
     * Scroll main text to top of text box
     */
    private fun scrollTextToTop() {
        val movement = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
        movement.toTop(binding.mainText)
    }

    /**
     * Scroll main text to bottom of text box
     */
    private fun scrollTextToBottom() {
        val movementMethod = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
        movementMethod.toBottom(binding.mainText)
    }
}
