package xyz.lbres.trickcalculator.ui.settings

import androidx.lifecycle.LifecycleOwner
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel

/**
 * Initialize basic observers to update a settings object when settings change in the ViewModel.
 * Settings must be observed using LiveData because they can be changed at any time using the settings dialog.
 *
 * @param settings [Settings]: settings object to update
 * @param viewModel [SharedViewModel]: view model to observe
 * @param lifecycleOwner [LifecycleOwner]
 */
fun initSettingsObservers(settings: Settings, viewModel: SharedViewModel, lifecycleOwner: LifecycleOwner) {
    viewModel.applyDecimals.observe(lifecycleOwner) { settings.applyDecimals = it }
    viewModel.applyParens.observe(lifecycleOwner) { settings.applyParens = it }
    viewModel.clearOnError.observe(lifecycleOwner) { settings.clearOnError = it }
    viewModel.historyRandomness.observe(lifecycleOwner) { settings.historyRandomness = it }
    viewModel.showSettingsButton.observe(lifecycleOwner) { settings.showSettingsButton = it }
    viewModel.shuffleComputation.observe(lifecycleOwner) { settings.shuffleComputation = it }
    viewModel.shuffleNumbers.observe(lifecycleOwner) { settings.shuffleNumbers = it }
    viewModel.shuffleOperators.observe(lifecycleOwner) { settings.shuffleOperators = it }
}
