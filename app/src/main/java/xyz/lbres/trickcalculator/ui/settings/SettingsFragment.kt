package xyz.lbres.trickcalculator.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentSettingsBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.shared.SharedViewModel

/**
 * Fragment to display all configuration options for calculator
 */
class SettingsFragment : BaseFragment() {
    override var titleResId: Int = R.string.title_settings // fragment-specific value

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var settingsUI: SettingsUI

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        settingsUI = SettingsUI(this, binding.root, sharedViewModel, viewLifecycleOwner)

        specializedFragmentCode()

        return binding.root
    }

    /**
     * Code that is run in fragment but not dialog
     */
    private fun specializedFragmentCode() {
        // show or hide settings button based on number of previous fragments
        val backStackSize = requireParentFragment().childFragmentManager.backStackEntryCount
        if (backStackSize > 1) {
            settingsUI.showSettingsButtonSwitch()
        }

        // close button
        binding.closeButton.setOnClickListener { requireMainActivity().popBackStack() }

        // Save settings when another fragment is opened. Preserves current settings when dialog is opened
        childFragmentManager.addFragmentOnAttachListener { _, _ ->
            settingsUI.saveSettingsToViewModel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsUI.saveSettingsToViewModel()
        settingsUI.closePreviousFragment()
    }
}
