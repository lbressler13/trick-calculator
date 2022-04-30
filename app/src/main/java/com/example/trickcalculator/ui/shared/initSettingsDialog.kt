package com.example.trickcalculator.ui.shared

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.trickcalculator.R

/**
 * Initialize handling of settings dialog
 */
fun initSettingsDialog(
    fragment: Fragment,
    viewModel: SharedViewModel,
    settings: Settings,
    viewToClick: View
) {
    val context = fragment.requireContext()

    val settingsDialog = SharedSettingsDialog()
    val numbersKey = context.getString(R.string.key_shuffle_numbers)
    val operatorsKey = context.getString(R.string.key_shuffle_operators)
    val parensKey = context.getString(R.string.key_apply_parens)
    val clearOnErrorKey = context.getString(R.string.key_clear_on_error)
    val decimalsKey = context.getString(R.string.key_apply_decimals)
    val requestKey = context.getString(R.string.key_settings_request)

    // update viewmodel with response from dialog
    fragment.childFragmentManager.setFragmentResultListener(
        requestKey,
        fragment.viewLifecycleOwner,
        { _: String, result: Bundle ->
            val returnedShuffleNumbers: Boolean = result.getBoolean(numbersKey, settings.shuffleNumbers)
            viewModel.setShuffleNumbers(returnedShuffleNumbers)

            val returnedShuffleOperators: Boolean =
                result.getBoolean(operatorsKey, settings.shuffleOperators)
            viewModel.setShuffleOperators(returnedShuffleOperators)

            val returnedApplyParens: Boolean = result.getBoolean(parensKey, settings.applyParens)
            viewModel.setApplyParens(returnedApplyParens)

            val returnedClearOnError: Boolean = result.getBoolean(clearOnErrorKey, settings.clearOnError)
            viewModel.setClearOnError(returnedClearOnError)

            val returnedApplyDecimals: Boolean = result.getBoolean(decimalsKey, settings.applyDecimals)
            viewModel.setApplyDecimals(returnedApplyDecimals)
        }
    )

    viewToClick.setOnClickListener {
        settingsDialog.arguments = bundleOf(
            numbersKey to settings.shuffleNumbers,
            operatorsKey to settings.shuffleOperators,
            parensKey to settings.applyParens,
            clearOnErrorKey to settings.clearOnError,
            decimalsKey to settings.applyDecimals
        )
        settingsDialog.show(fragment.childFragmentManager, SharedSettingsDialog.TAG)
    }
}
