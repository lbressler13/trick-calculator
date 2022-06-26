package com.example.trickcalculator.ui.attributions.authorattribution

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.attributions.*
import com.example.trickcalculator.ui.attributions.imageattribution.ImageAttributionAdapter

/**
 * ViewHolder for a single author attribution. Includes dropdown for image attributions.
 *
 * @param binding [ViewHolderAuthorAttributionBinding]: view binding for the view holder
 */
class AuthorAttributionViewHolder(private val binding: ViewHolderAuthorAttributionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // constant resources that are used in the code
    private val expandedIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_down)
    private val collapsedIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_right)
    private val expandedContentDescription = itemView.context.getString(R.string.expand_dropdown_cd)
    private val collapsedContentDescription = itemView.context.getString(R.string.collapse_dropdown_cd)

    // update UI to show information about current author, including whether or not their images should be visible
    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        initializeAttributionText(author)
        initializeAdapter(author.images)
        var showingIcons = initialShowingIcons

        setIconsDisplay(initialShowingIcons)

        binding.expandCollapseButton.setOnClickListener {
            showingIcons = !showingIcons
            setShowingIcons(showingIcons)
            setIconsDisplay(showingIcons)
        }
    }

    // show or hide icons
    private fun setIconsDisplay(visible: Boolean) {
        if (visible) {
            // show icons + update dropdown icon
            binding.expandCollapseButton.setImageDrawable(expandedIcon)
            binding.expandCollapseButton.contentDescription = expandedContentDescription
            binding.imagesLayout.visible()
        } else {
            // hide icons + update dropdown icon
            binding.expandCollapseButton.setImageDrawable(collapsedIcon)
            binding.expandCollapseButton.contentDescription = collapsedContentDescription
            binding.imagesLayout.gone()
        }
    }

    // initialize text with author's name and url + relevant links
    private fun initializeAttributionText(author: AuthorAttribution) {
        // get text template and format with URLs
        val text = itemView.context.getString(R.string.author_attr_template, author.name, flaticonDisplayUrl)
        val spannableString = SpannableString(text)

        // link to flaticon site
        UrlClickableSpan.addToFirstWord(spannableString, flaticonDisplayUrl, flaticonUrl)
        // link to creator
        UrlClickableSpan.addToFirstWord(spannableString, author.name, author.url)

        binding.attribution.movementMethod = LinkMovementMethod()
        binding.attribution.text = spannableString
    }

    // initialize adapter for images dropdown
    private fun initializeAdapter(images: List<ImageAttribution>) {
        val recycler: RecyclerView = binding.imagesRecycler
        val adapter = ImageAttributionAdapter(images)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(itemView.context)
    }
}
