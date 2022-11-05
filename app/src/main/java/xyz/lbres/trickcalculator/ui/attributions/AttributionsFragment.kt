package xyz.lbres.trickcalculator.ui.attributions

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.FragmentAttributionsBinding
import xyz.lbres.trickcalculator.ui.BaseFragment
import xyz.lbres.trickcalculator.ui.attributions.authorattribution.AuthorAttributionAdapter
import xyz.lbres.trickcalculator.ui.attributions.constants.authorAttributions
import xyz.lbres.trickcalculator.ui.attributions.constants.flaticonAttrPolicyUrl
import xyz.lbres.trickcalculator.ui.attributions.constants.flaticonUrl
import xyz.lbres.trickcalculator.utils.createUnderlineText

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : BaseFragment() {
    private lateinit var binding: FragmentAttributionsBinding
    private lateinit var viewModel: AttributionsViewModel

    override var titleResId: Int = R.string.title_attributions
    override var actionBarOnClick: (() -> Unit)? =
        { requireMainActivity().runNavAction(R.id.navigateAttributionsToSettings) }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttributionsBinding.inflate(layoutInflater)
        // view model is tied to fragment lifecycle, so expansions are reset for each instance of fragment
        viewModel = ViewModelProvider(this)[AttributionsViewModel::class.java]

        setFlaticonMessage()
        initializeAttributionsRecycler()

        binding.expandCollapseMessage.setOnClickListener {
            viewModel.setFlaticonMessageExpanded(!viewModel.flaticonMessageExpanded)
            setFlaticonMessage()
        }
        binding.closeButton.root.setOnClickListener { requireMainActivity().popBackStack() }

        return binding.root
    }

    /**
     * Initialize RecyclerView to display author attributions
     */
    private fun initializeAttributionsRecycler() {
        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AuthorAttributionAdapter(authorAttributions, viewModel)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.initAttributionsExpanded(authorAttributions.size)
    }

    /**
     * Update Flaticon message and expand/collapse label based on value from ViewModel
     */
    private fun setFlaticonMessage() {
        val fullMessage = requireContext().getString(R.string.flaticon_message)
        val shortMessage = requireContext().getString(R.string.flaticon_message_short)

        val expandString = requireContext().getString(R.string.expand)
        val collapseString = requireContext().getString(R.string.collapse)

        if (viewModel.flaticonMessageExpanded) {
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
        if (viewModel.flaticonMessageExpanded) {
            URLClickableSpan.addToFirstWord(spannableString, "here", flaticonAttrPolicyUrl)
        }

        binding.flaticonPolicyMessage.movementMethod = LinkMovementMethod()
        binding.flaticonPolicyMessage.text = spannableString
    }
}
