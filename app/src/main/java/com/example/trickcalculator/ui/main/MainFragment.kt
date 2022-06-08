package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import exactfraction.ExactFraction
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
import com.example.trickcalculator.ui.settings.Settings
import com.example.trickcalculator.ui.settings.initSettingsFragment
import com.example.trickcalculator.ui.settings.initSettingsObservers

/**
 * Fragment to display main calculator functionality
 */
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var computationViewModel: ComputationViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var computeText: StringList
    private var error: String? = null
    private var computedValue: ExactFraction? = null

    private val settings = Settings()

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
        computationViewModel.computedValue.observe(viewLifecycleOwner, computedValueObserver)
        computationViewModel.lastHistoryItem.observe(viewLifecycleOwner, lastHistoryItemObserver)
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)
        // additional observer to show/hide settings button
        sharedViewModel.showSettingsButton.observe(viewLifecycleOwner, showSettingsButtonObserver)
        sharedViewModel.isDevMode.observe(viewLifecycleOwner, isDevModeObserver)

        initNumpad()
        binding.mainText.movementMethod = UnprotectedScrollingMovementMethod()
        binding.infoButton.setOnClickListener { infoButtonOnClick() }
        binding.historyButton.setOnClickListener { historyButtonOnClick() }
        initActionBar()
        initDeveloperOptions()

        initSettingsFragment(this, settings, binding.settingsButton)

        return binding.root
    }

    override fun getView(): View {
        return binding.root
    }

    private val computedValueObserver: Observer<ExactFraction?> = Observer {
        computedValue = it
        setMainText()
    }
    private val lastHistoryItemObserver: Observer<HistoryItem> = Observer {
        if (it != null) {
            sharedViewModel.addToHistory(it)
            computationViewModel.clearStoredHistoryItem()
        }
    }
    private val showSettingsButtonObserver: Observer<Boolean> = Observer {
        binding.settingsButton.isVisible = it
    }
    private val isDevModeObserver: Observer<Boolean> = Observer {
        binding.settingsButton.isVisible = settings.showSettingsButton
        if (this::computeText.isInitialized) {
            setMainText()
        }
    }

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
            computationViewModel.saveComputeText()

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
                        computedValue,
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

                val movement = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
                movement.goToTop(binding.mainText)
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
        scrollTextToBottom()
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
            scrollTextToBottom()
        }
    }

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        actionBar.root.setOnClickListener(null)
        actionBar.title.text = requireContext().getString(R.string.title_action_bar)
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
            fullText = "[${computedString}]$fullText"
        }

        textview.text = fullText
    }

    private fun scrollTextToBottom() {
        val movementMethod = binding.mainText.movementMethod as UnprotectedScrollingMovementMethod
        movementMethod.goToBottom(binding.mainText)
    }

    private fun initDeveloperOptions() {
        binding.devToolsButton.isVisible = BuildOptions.buildType == "dev"
        val dialog = DeveloperToolsDialog()
        binding.devToolsButton.setOnClickListener {
            dialog.show(childFragmentManager, DeveloperToolsDialog.TAG)
        }
    }
}
