package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentAttributionsBinding
import com.example.trickcalculator.ui.BaseFragment
import com.example.trickcalculator.ui.attributions.authorattribution.AuthorAttributionAdapter
import com.example.trickcalculator.ui.attributions.constants.authorAttributions
import com.example.trickcalculator.ui.attributions.constants.flaticonAttrPolicyUrl
import com.example.trickcalculator.ui.attributions.constants.flaticonUrl
import com.example.trickcalculator.ui.settings.initSettingsFragment
import com.example.trickcalculator.utils.createUnderlineText

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : BaseFragment() {
    private lateinit var binding: FragmentAttributionsBinding
    private lateinit var viewModel: AttributionsViewModel

    private var flaticonMessageExpanded = false

    override var titleResId: Int = R.string.title_attributions
    override var setActionBarOnClick: ((View) -> Unit)? = { initSettingsFragment(this, it, R.id.navigateAttributionsToSettings) }

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
        // view model is tied to fragment, so expansions are reset for each instance of fragment
        viewModel = ViewModelProvider(this)[AttributionsViewModel::class.java]

        initializeAttributionsRecycler()

        viewModel.flaticonMessageExpanded.observe(viewLifecycleOwner, flaticonMessageExpandedObserver)
        binding.expandCollapseMessage.setOnClickListener { viewModel.setFlaticonMessageExpanded(!flaticonMessageExpanded) }
        binding.closeButton.root.setOnClickListener { popBackStack() }

        return binding.root
    }

    private fun initializeAttributionsRecycler() {
        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AuthorAttributionAdapter(authorAttributions, viewModel, viewLifecycleOwner)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.initAttributionsExpanded(authorAttributions.size)
    }

    private val flaticonMessageExpandedObserver: Observer<Boolean> = Observer {
        flaticonMessageExpanded = it
        setFlaticonMessage()
    }

    /**
     * Update Flaticon message and expand/collapse label based on value observed from ViewModel
     */
    private fun setFlaticonMessage() {
        val fullMessage = requireContext().getString(R.string.flaticon_message)
        val shortMessage = requireContext().getString(R.string.flaticon_message_short)

        val expandString = requireContext().getString(R.string.expand)
        val collapseString = requireContext().getString(R.string.collapse)

        if (flaticonMessageExpanded) {
            // expand text
            binding.flaticonPolicyMessage.text = fullMessage
            binding.expandCollapseMessage.text = createUnderlineText(collapseString)
        } else {
            // collapse text
            binding.flaticonPolicyMessage.text = shortMessage
            binding.expandCollapseMessage.text = createUnderlineText(expandString)
        }
        addFlaticonLinks()
    }

    /**
     * Add links to Flaticon message using URLClickableSpans
     */
    private fun addFlaticonLinks() {
        val text = binding.flaticonPolicyMessage.text.toString()
        val spannableString = SpannableString(text)

        URLClickableSpan.addToFirstWord(spannableString, "Flaticon", flaticonUrl)
        if (flaticonMessageExpanded) {
            URLClickableSpan.addToFirstWord(spannableString, "here", flaticonAttrPolicyUrl)
        }

        binding.flaticonPolicyMessage.movementMethod = LinkMovementMethod()
        binding.flaticonPolicyMessage.text = spannableString
    }
}
