package xyz.lbres.trickcalculator.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.exactnumbers.exactfraction.ExactFraction
import xyz.lbres.kotlinutils.general.ternaryIf
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.compute.runComputation
import xyz.lbres.trickcalculator.databinding.FragmentMainBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.settings.Settings
import xyz.lbres.trickcalculator.ui.settings.initSettingsFragment
import xyz.lbres.trickcalculator.ui.settings.initSettingsObservers
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.History
import xyz.lbres.trickcalculator.utils.OperatorFunction
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible
import java.util.Date
import java.util.Random

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var computationViewModel: ComputationViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val random = Random(Date().time)
    private val settings = Settings()

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        computationViewModel =
            ViewModelProvider(requireActivity())[ComputationViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // observe changes in viewmodels
        sharedViewModel.history.observe(viewLifecycleOwner, historyObserver)
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)
        // additional observer to show/hide settings button, in addition to observer in initSettingsObservers
        sharedViewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)

        // init UI
        initNumpad()
        binding.mainText.movementMethod = UnprotectedScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        binding.useLastHistoryButton.setOnClickListener { useLastHistoryItemOnClick() }
        updateUI()

        initSettingsFragment(this, binding.settingsButton, R.id.navigateMainToSettings)

        return binding.root
    }

    /**
     * Enable/disable the last item button
     */
    private val historyObserver: Observer<History> = Observer {
        binding.useLastHistoryButton.isVisible = it.isNotEmpty()
    }

    /**
     * Show or hide settings button
     */
    private val showSettingsButtonObserver: Observer<Boolean> = Observer {
        binding.settingsButton.isVisible = it
    }

    /**
     * Launch AttributionsFragment
     */
    private val infoButtonOnClick = {
        requireMainActivity().runNavAction(R.id.navigateMainToAttribution)
    }

    /**
     * Launch HistoryFragment
     */
    private val historyButtonOnClick = {
        requireMainActivity().runNavAction(R.id.navigateMainToHistory)
    }

    /**
     * Use last history item as current computation
     */
    private val useLastHistoryItemOnClick = {
        val item = sharedViewModel.history.value?.lastOrNull()
        if (item != null) {
            computationViewModel.useHistoryItemAsComputeText(item)

            updateUI()
            scrollTextToTop()
        }
    }

    /**
     * Sets operation order and number order, and runs computation.
     * Updates viewmodel with resulting computed value or error message
     */
    private val equalsButtonOnClick = {
        if (computationViewModel.computeText.isNotEmpty()) {
            computationViewModel.saveComputation()

            // set action for each operator
            // only include exponent if exp is used
            val operators = when {
                !settings.shuffleOperators -> listOf("+", "-", "x", "/", "^")
                !computationViewModel.computeText.contains("^") -> listOf(
                    "+",
                    "-",
                    "x",
                    "/"
                ).shuffled(random) + listOf("^")
                else -> listOf("+", "-", "x", "/", "^").shuffled(random)
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

            val numberOrder =
                ternaryIf(settings.shuffleNumbers, (0..9).shuffled(random), (0..9).toList())

            // try to run computation, and update compute text and error message
            try {
                val computedValue: ExactFraction =
                    runComputation(
                        computationViewModel.computedValue,
                        computationViewModel.computeText,
                        operatorRounds,
                        performOperation,
                        numberOrder,
                        settings.applyParens,
                        settings.applyDecimals,
                        settings.shuffleComputation
                    )

                computationViewModel.setResult(null, computedValue)
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

                computationViewModel.setResult(error, null, settings.clearOnError)
                updateUI()
            }

            sharedViewModel.addToHistory(computationViewModel.generatedHistoryItem!!)
            computationViewModel.clearStoredHistoryItem()
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
     * Sets the text in the textbox, including ui modifications for first term
     */
    private fun updateUI() {
        val textview: TextView = binding.mainText
        val error = computationViewModel.error

        if (error != null) {
            @SuppressLint("SetTextI18n")
            binding.errorText.text = "Error: $error"
            binding.errorText.visible()
        } else {
            binding.errorText.gone()
        }

        var fullText = computationViewModel.computeText.joinToString("")
        // add computed value
        val computedString = computationViewModel.computedValue?.toDecimalString()
        if (computedString != null) {
            fullText = "[$computedString]$fullText"
        }

        textview.text = fullText
    }

    /**
     * Scroll main text to top of text box
     */
    private fun scrollTextToTop() {
        val movement = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
        movement.goToTop(binding.mainText)
    }

    /**
     * Scroll main text to bottom of text box
     */
    private fun scrollTextToBottom() {
        val movementMethod = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
        movementMethod.goToBottom(binding.mainText)
    }
}
