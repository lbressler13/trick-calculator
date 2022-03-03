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
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.compute.runComputation
import com.example.trickcalculator.ext.*
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.attributions.AttributionsFragment
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.setImageButtonTint

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

    companion object {
        fun newInstance() = MainFragment()
    }

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
                    else -> 0f
                }
            }

            val numberOrder = if (shuffleNumbers) {
                (0..9).shuffled()
            } else {
                (0..9).toList()
            }

            try {
                val computedValue: Float =
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
}