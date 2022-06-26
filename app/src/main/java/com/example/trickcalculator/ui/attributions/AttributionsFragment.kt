package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentAttributionsBinding
import com.example.trickcalculator.ui.ActivityFragment
import com.example.trickcalculator.ui.attributions.authorattribution.AuthorAttributionAdapter
import com.example.trickcalculator.ui.settings.initSettingsFragment

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : ActivityFragment() {
    private lateinit var binding: FragmentAttributionsBinding
    private lateinit var viewModel: AttributionsViewModel

    private var textExpanded = false

    override var titleResId: Int = R.string.title_attributions
    override var setActionBarOnClick: ((View) -> Unit)? = { initSettingsFragment(this, it) }

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
        // view model is tied to the fragment, not activity
        viewModel = ViewModelProvider(this)[AttributionsViewModel::class.java]

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        initializeAttributionsList()
        addFlaticonLinks()

        binding.expandCollapseButton.setOnClickListener { viewModel.setTextExpanded(!textExpanded) }
        viewModel.textExpanded.observe(viewLifecycleOwner, textExpandedObserver)

        return binding.root
    }

    private fun initializeAttributionsList() {
        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AuthorAttributionAdapter(authorAttributions, viewModel, viewLifecycleOwner)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.setAttributionCount(authorAttributions.size)
    }

    private val textExpandedObserver: Observer<Boolean> = Observer {
        textExpanded = it
        setTopText()
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
