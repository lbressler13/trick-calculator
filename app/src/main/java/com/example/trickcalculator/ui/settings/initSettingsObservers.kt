package com.example.trickcalculator.ui.settings

import androidx.lifecycle.LifecycleOwner
import com.example.trickcalculator.ui.shared.SharedViewModel

fun initSettingsObservers(settings: Settings, viewModel: SharedViewModel, lifecycleOwner: LifecycleOwner) {
    viewModel.shuffleNumbers.observe(lifecycleOwner) { settings.shuffleNumbers = it }
    viewModel.shuffleOperators.observe(lifecycleOwner) { settings.shuffleOperators = it }
    viewModel.applyParens.observe(lifecycleOwner) { settings.applyParens = it }
    viewModel.clearOnError.observe(lifecycleOwner) { settings.clearOnError = it }
    viewModel.applyDecimals.observe(lifecycleOwner) { settings.applyDecimals = it }
    viewModel.showSettingsButton.observe(lifecycleOwner) { settings.showSettingsButton = it }
    viewModel.historyRandomness.observe(lifecycleOwner) { settings.historyRandomness = it }
    viewModel.shuffleComputation.observe(lifecycleOwner) { settings.shuffleComputation = it }
}
