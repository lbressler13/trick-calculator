package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentAttributionsBinding
import com.example.trickcalculator.ui.attributions.authorattribution.AuthorAttributionAdapter
import com.example.trickcalculator.ui.settings.Settings
import com.example.trickcalculator.ui.settings.initSettingsFragment
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.settings.initSettingsObservers

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : Fragment() {
    private lateinit var binding: FragmentAttributionsBinding
    private lateinit var sharedViewModel: SharedViewModel

    private val settings = Settings()

    companion object {
        fun newInstance() = AttributionsFragment()
    }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttributionsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AuthorAttributionAdapter(authorAttributions)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        initFlaticonMessage()
        initActionBar()
        (requireActivity() as MainActivity).fragmentManager = childFragmentManager
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)

        return binding.root
    }

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        initSettingsFragment(this, settings, actionBar.root)
        actionBar.title.text = requireContext().getString(R.string.title_attributions)
    }

    private fun initFlaticonMessage() {
        val text = binding.topText.text.toString()
        val spannableString = SpannableString(text)

        val flaticonStart = text.indexOf("Flaticon")
        val flaticonEnd = flaticonStart + "Flaticon".length
        spannableString.setSpan(
            UrlClickableSpan(flaticonUrl),
            flaticonStart,
            flaticonEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        val policyStart = text.indexOf("here")
        val policyEnd = policyStart + "here".length
        spannableString.setSpan(
            UrlClickableSpan(flaticonAttrPolicyUrl),
            policyStart,
            policyEnd,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.topText.movementMethod = LinkMovementMethod()
        binding.topText.text = spannableString
    }
}
