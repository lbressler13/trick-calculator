package com.example.trickcalculator.ui.attributions.authorattribution

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
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

    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        initializeAttributionText(author, itemView.context)
        initializeAdapter(author.images)

        var showingIcons = initialShowingIcons
        if (initialShowingIcons) {
            showIcons()
        } else {
            hideIcons()
        }
        binding.showHideIcons.setOnClickListener {
            if (showingIcons) {
                hideIcons()
            } else {
                showIcons()
            }
            showingIcons = !showingIcons
            setShowingIcons(showingIcons)
        }
    }

    private fun showIcons() {
        binding.showHideIcons.text = itemView.context.getString(R.string.hide_icons)
        binding.imagesRecycler.visible()
    }

    private fun hideIcons() {
        binding.showHideIcons.text = itemView.context.getString(R.string.show_icons)
        binding.imagesRecycler.gone()
    }

    private fun initializeAttributionText(author: AuthorAttribution, context: Context) {
        val template = context.getString(R.string.author_attr_template)
        val text = template.format(author.name, flaticonDisplayUrl)
        val spannableText = SpannableString(text)

        // link to Flaticon site
        val flaticonStart = text.length - flaticonDisplayUrl.length
        spannableText.setSpan(
            UrlClickableSpan(flaticonUrl),
            flaticonStart,
            text.length,
            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )

        // link to creator
        val authorStart = text.indexOf(author.name)
        val authorEnd = authorStart + author.name.length
        spannableText.setSpan(
            UrlClickableSpan(author.url),
            authorStart,
            authorEnd,
            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.attribution.movementMethod = LinkMovementMethod()
        binding.attribution.text = spannableText
    }

    private fun initializeAdapter(images: List<ImageAttribution>) {
        val recycler: RecyclerView = binding.imagesRecycler
        val adapter = ImageAttributionAdapter(images)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(itemView.context)
    }
}
