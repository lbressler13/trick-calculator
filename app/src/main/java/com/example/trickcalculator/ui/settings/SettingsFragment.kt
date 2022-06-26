package com.example.trickcalculator.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentSettingsBinding
import com.example.trickcalculator.ui.ActivityFragment
import com.example.trickcalculator.ui.shared.SharedViewModel

class SettingsFragment : ActivityFragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedViewModel: SharedViewModel
    override var titleResId: Int = R.string.title_settings

    var resetPressed = false
    var randomizePressed = false

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        runFragmentCode()
        setUiFromArgs(this, sharedViewModel, binding)

        return binding.root
    }

    /**
     * Code that is specific to fragment and is not run in dialog
     */
    private fun runFragmentCode() {
        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToViewModel(this, sharedViewModel, binding)
        closePreviousFragment(this)
    }
}
