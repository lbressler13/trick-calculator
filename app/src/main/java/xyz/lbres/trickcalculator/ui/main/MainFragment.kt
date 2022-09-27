package xyz.lbres.trickcalculator.ui.main

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
import xyz.lbres.kotlinutils.list.StringList
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.compute.runComputation
import xyz.lbres.trickcalculator.databinding.FragmentMainBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.history.HistoryItem
import xyz.lbres.trickcalculator.ui.settings.Settings
import xyz.lbres.trickcalculator.ui.settings.initSettingsFragment
import xyz.lbres.trickcalculator.ui.settings.initSettingsObservers
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel
import xyz.lbres.trickcalculator.utils.History
import xyz.lbres.trickcalculator.utils.OperatorFunction
import xyz.lbres.trickcalculator.utils.gone
import xyz.lbres.trickcalculator.utils.visible

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var computationViewModel: ComputationViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var computeText: StringList
    private var error: String? = null
    private var computedValue: ExactFraction? = null

    private val settings = Settings()
    private var lastHistoryItem: HistoryItem? = null

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
        computationViewModel.computeText.observe(viewLifecycleOwner, computeTextObserver)
        computationViewModel.error.observe(viewLifecycleOwner, errorObserver)
        computationViewModel.computedValue.observe(viewLifecycleOwner, computedValueObserver)
        computationViewModel.generatedHistoryItem.observe(viewLifecycleOwner, generatedHistoryItemObserver)
        sharedViewModel.history.observe(viewLifecycleOwner, historyObserver)
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)
        // additional observer to show/hide settings button, in addition to observer in initSettingsObservers
        sharedViewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)

        // init UI
        initNumpad()
        binding.mainText.movementMethod = UnprotectedScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        binding.useLastHistoryButton.setOnClickListener { lastHistoryItemOnClick() }

        initSettingsFragment(this, binding.settingsButton, R.id.navigateMainToSettings)

        return binding.root
    }

    /**
     * Save generated history value in shared view model and delete
     */
    private val generatedHistoryItemObserver: Observer<HistoryItem> = Observer {
        if (it != null) {
            sharedViewModel.addToHistory(it)
            computationViewModel.clearStoredHistoryItem()
        }
    }

    /**
     * Save most recent history item and enable/disable the last item button
     */
    private val historyObserver: Observer<History> = Observer {
        lastHistoryItem = it.lastOrNull()
        binding.useLastHistoryButton.isVisible = it.isNotEmpty()
    }

    /**
     * Show or hide settings button
     */
    private val showSettingsButtonObserver: Observer<Boolean> = Observer {
        binding.settingsButton.isVisible = it
    }

    /**
     * Save computed value in local variable and update textbox
     */
    private val computedValueObserver: Observer<ExactFraction?> = Observer {
        computedValue = it
        setMainText()
    }

    /**
     * Save compute text in local variable and update textbox
     */
    private val computeTextObserver: Observer<StringList> = Observer {
        computeText = it
        setMainText()
    }

    /**
     * Display current error message, and clear compute data depending on settings
     */
    private val errorObserver: Observer<String> = Observer {
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
    private val lastHistoryItemOnClick = {
        if (lastHistoryItem != null) {
            val item = lastHistoryItem!!
            computationViewModel.useHistoryItemAsComputeText(item)

            scrollTextToTop()
        }
    }

    /**
     * Sets operation order and number order, and runs computation.
     * Updates viewmodel with resulting computed value or error message
     */
    private val equalsButtonOnClick = {
        if (computeText.isNotEmpty()) {
            computationViewModel.saveComputation()

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

            val numberOrder = ternaryIf(settings.shuffleNumbers, (0..9).shuffled(), (0..9).toList())

            // try to run computation, and update compute text and error message
            try {
                val computedValue: ExactFraction =
                    runComputation(
                        computedValue,
                        computeText,
                        operatorRounds,
                        performOperation,
                        numberOrder,
                        settings.applyParens,
                        settings.applyDecimals,
                        settings.shuffleComputation
                    )

                computationViewModel.setComputedValue(computedValue)
                computationViewModel.setError(null)
                computationViewModel.generateHistoryItem()

                computationViewModel.clearComputeText()

                scrollTextToTop()
            } catch (e: Exception) {
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
                computationViewModel.generateHistoryItem()
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
        scrollTextToBottom()
    }

    /**
     * Initialize all buttons in numpad
     */
    private fun initNumpad() {
        // text buttons, except clear
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

        // functional buttons, including clear
        binding.clearButton.setOnClickListener { computationViewModel.resetComputeData() }
        binding.equalsButton.setOnClickListener { equalsButtonOnClick() }
        binding.backspaceButton.setOnClickListener {
            computationViewModel.setError(null)
            computationViewModel.backspaceComputeText()
            scrollTextToBottom()
        }
    }

    /**
     * Sets the text in the textbox, including ui modifications for first term
     */
    private fun setMainText() {
        val textview: TextView = binding.mainText
        var fullText = computeText.joinToString("")
        // add computed value
        val computedString = computedValue?.toDecimalString()
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
