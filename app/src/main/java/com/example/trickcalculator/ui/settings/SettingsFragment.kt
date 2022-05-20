package com.example.trickcalculator.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentSettingsBinding
import com.example.trickcalculator.ui.shared.SharedViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setUiFromArgs()

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        actionBar.root.setOnClickListener(null)

        return binding.root
    }

    /**
     * Get information from fragment args to set initial config in UI
     */
    private fun setUiFromArgs() {
        setSwitchUiFromArgs(R.string.key_shuffle_numbers, binding.shuffleNumbersSwitch)
        setSwitchUiFromArgs(R.string.key_shuffle_operators, binding.shuffleOperatorsSwitch)
        setSwitchUiFromArgs(R.string.key_apply_parens, binding.applyParensSwitch)
        setSwitchUiFromArgs(R.string.key_clear_on_error, binding.clearOnErrorSwitch)
        setSwitchUiFromArgs(R.string.key_apply_decimals, binding.applyDecimalsSwitch)
        setSwitchUiFromArgs(R.string.key_settings_button, binding.settingsButtonSwitch)

        val mainFragmentKey = requireContext().getString(R.string.key_main_fragment)
        val isMainFragment = this.arguments?.getBoolean(mainFragmentKey)
        binding.settingsButtonSwitch.isVisible = isMainFragment != true

        setHistoryRadioGroup()
    }

    /**
     * Get information from fragment args to set if a switch should be checked
     *
     * @param keyId [Int]: resourceId of key to access argument
     * @param switch [SwitchCompat]: switch to check
     */
    private fun setSwitchUiFromArgs(keyId: Int, switch: SwitchCompat) {
        val key: String = requireContext().getString(keyId)
        val value: Boolean? = this.arguments?.getBoolean(key)
        if (value != null) {
            switch.isChecked = value
        }
    }

    private fun setHistoryRadioGroup() {
        val group = binding.historyRandomnessGroup
        val key: String = requireContext().getString(R.string.key_random_history)
        val value: Int = this.arguments?.getInt(key) ?: 0
        when (value) {
            0 -> group.check(binding.historyButton0.id)
            1 -> group.check(binding.historyButton1.id)
            2 -> group.check(binding.historyButton2.id)
            3 -> group.check(binding.historyButton3.id)
        }
    }

    private fun getHistoryGroupValue(): Int {
        return when (binding.historyRandomnessGroup.checkedRadioButtonId) {
            binding.historyButton0.id -> 0
            binding.historyButton1.id -> 1
            binding.historyButton2.id -> 2
            binding.historyButton3.id -> 3
            else -> 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.setShuffleNumbers(binding.shuffleNumbersSwitch.isChecked)
        sharedViewModel.setShuffleOperators(binding.shuffleOperatorsSwitch.isChecked)
        sharedViewModel.setApplyParens(binding.applyParensSwitch.isChecked)
        sharedViewModel.setClearOnError(binding.clearOnErrorSwitch.isChecked)
        sharedViewModel.setApplyDecimals(binding.applyDecimalsSwitch.isChecked)
        sharedViewModel.setShowSettingsButton(binding.settingsButtonSwitch.isChecked)
        sharedViewModel.setHistoryRandomness(getHistoryGroupValue())
    }
}
