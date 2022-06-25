package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
    private lateinit var viewModel: AttributionsViewModel

    private val settings = Settings()

    private var textExpanded = false

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
        viewModel = ViewModelProvider(this)[AttributionsViewModel::class.java]

        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AuthorAttributionAdapter(authorAttributions, viewModel, viewLifecycleOwner)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.setAttributionCount(authorAttributions.size)

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        addFlaticonLinks()
        initActionBar()
        (requireActivity() as MainActivity).fragmentManager = childFragmentManager
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)

        binding.expandCollapseButton.setOnClickListener { viewModel.setTextExpanded(!textExpanded) }
        viewModel.textExpanded.observe(viewLifecycleOwner, textExpandedObserver)

        return binding.root
    }

    private val textExpandedObserver: Observer<Boolean> = Observer {
        textExpanded = it
        setTopText()
    }

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        initSettingsFragment(this, settings, actionBar.root)
        actionBar.title.text = requireContext().getString(R.string.title_attributions)
    }

    private fun setTopText() {
        val expandedIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_chevron_up)
        val collapsedIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_chevron_down)
        val expandedContentDescription = requireContext().getString(R.string.expand_dropdown_cd)
        val collapsedContentDescription = requireContext().getString(R.string.collapse_dropdown_cd)
        val expandedText = requireContext().getString(R.string.flaticon_message)
        val collapsedText = requireContext().getString(R.string.flaticon_message_short)

        if (textExpanded) {
            // expand text
            binding.topText.text = expandedText
            binding.expandCollapseButton.setImageDrawable(expandedIcon)
            binding.expandCollapseButton.contentDescription = expandedContentDescription
        } else {
            // collapse text
            binding.topText.text = collapsedText
            binding.expandCollapseButton.setImageDrawable(collapsedIcon)
            binding.expandCollapseButton.contentDescription = collapsedContentDescription
        }
        addFlaticonLinks()
    }

    private fun addFlaticonLinks() {
        val text = binding.topText.text.toString()
        val spannableString = SpannableString(text)

        UrlClickableSpan.addToFirstWord(spannableString, "Flaticon", flaticonUrl)
        if (textExpanded) {
            UrlClickableSpan.addToFirstWord(spannableString, "here", flaticonAttrPolicyUrl)
        }

        binding.topText.movementMethod = LinkMovementMethod()
        binding.topText.text = spannableString
    }
}
