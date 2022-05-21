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

        runFragmentCode()
        setUiFromArgs(binding, requireContext(), arguments)

        return binding.root
    }

    private fun runFragmentCode() {
        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        actionBar.root.setOnClickListener(null)
        actionBar.title.text = requireActivity().getString(R.string.title_settings)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToViewModel(sharedViewModel, binding)
    }
}
