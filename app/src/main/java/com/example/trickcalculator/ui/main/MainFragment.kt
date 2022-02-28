package com.example.trickcalculator.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentMainBinding
import com.example.trickcalculator.ext.disableAllChildren
import com.example.trickcalculator.ext.enable
import com.example.trickcalculator.ext.enableAllChildren
import com.example.trickcalculator.runComputation
import com.example.trickcalculator.utils.OperatorFunction
import com.example.trickcalculator.utils.StringList
import com.example.trickcalculator.utils.setImageButtonTint
import java.lang.Exception

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var computeText: StringList

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

        initButtons()
        binding.mainText.movementMethod = ScrollingMovementMethod()

        return binding.root
    }

    private val getComputeTextObserver: Observer<StringList> = Observer {
        computeText = it
        binding.mainText.text = it.joinToString("")
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

            binding.numpadLayout.enableAllChildren()
            binding.numpadLayout.children.forEach {
                setImageButtonTint(
                    it,
                    R.color.black,
                    requireContext()
                )
            }
        }

        binding.equalsButton.setOnClickListener {
            // shuffle action for each operator
            val operators = listOf("+", "-", "x", "/").shuffled()
            val performOperation: OperatorFunction = { leftValue, rightValue, operator ->
                when (operator) {
                    operators[0] -> leftValue + rightValue
                    operators[1] -> leftValue - rightValue
                    operators[2] -> leftValue * rightValue
                    operators[3] -> leftValue / rightValue
                    else -> 0
                }
            }

            // val numberOrder = if (shuffleNumbers) {
            //     (0..9).shuffled()
            // } else {
            //     (0..9).toList()
            // }
            val numberOrder = (0..9).toList()

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
            } catch (e: Exception) {
                binding.mainText.text = e.message
                binding.numpadLayout.disableAllChildren()
                binding.numpadLayout.children.forEach {
                    setImageButtonTint(
                        it,
                        R.color.disabled_foreground_gray,
                        requireContext()
                    )
                }

                // need to be able to clear error
                binding.clearButton.enable()
                setImageButtonTint(
                    binding.clearButton,
                    R.color.black,
                    requireContext()
                )
            }
        }
    }
}