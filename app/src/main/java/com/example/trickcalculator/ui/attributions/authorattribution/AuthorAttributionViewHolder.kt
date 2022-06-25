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

class AuthorAttributionViewHolder(private val binding: ViewHolderAuthorAttributionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val expandedIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_down)
    private val collapsedIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_right)
    private val expandedContentDescription = itemView.context.getString(R.string.expand_dropdown_cd)
    private val collapsedContentDescription = itemView.context.getString(R.string.collapse_dropdown_cd)

    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        initializeAttributionText(author, itemView.context)
        initializeAdapter(author.images)
        var showingIcons = initialShowingIcons

        setIconsDisplay(initialShowingIcons)

        binding.expandCollapseButton.setOnClickListener {
            showingIcons = !showingIcons
            setShowingIcons(showingIcons)
            setIconsDisplay(showingIcons)
        }
    }

    private fun setIconsDisplay(visible: Boolean) {
        if (visible) {
            // show icons
            binding.expandCollapseButton.setImageDrawable(expandedIcon)
            binding.expandCollapseButton.contentDescription = expandedContentDescription
            binding.imagesLayout.visible()
        } else {
            // hide icons
            binding.expandCollapseButton.setImageDrawable(collapsedIcon)
            binding.expandCollapseButton.contentDescription = collapsedContentDescription
            binding.imagesLayout.gone()
        }
    }

    private fun initializeAttributionText(author: AuthorAttribution, context: Context) {
        val text = context.getString(R.string.author_attr_template, author.name, flaticonDisplayUrl)
        val spannableString = SpannableString(text)

        // link to flaticon site
        UrlClickableSpan.addToFirstWord(spannableString, flaticonDisplayUrl, flaticonUrl)
        // link to creator
        UrlClickableSpan.addToFirstWord(spannableString, author.name, author.url)

        binding.attribution.movementMethod = LinkMovementMethod()
        binding.attribution.text = spannableString
    }

    private fun initializeAdapter(images: List<ImageAttribution>) {
        val recycler: RecyclerView = binding.imagesRecycler
        val adapter = ImageAttributionAdapter(images)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(itemView.context)
    }
}
