package com.example.trickcalculator.ui.attributions.authorattribution

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import com.example.trickcalculator.ui.attributions.AuthorAttribution
import com.example.trickcalculator.ui.attributions.UrlClickableSpan
import com.example.trickcalculator.ui.attributions.flaticonDisplayUrl
import com.example.trickcalculator.ui.attributions.flaticonUrl

class AuthorAttributionViewHolder(private val binding: ViewHolderAuthorAttributionBinding) : RecyclerView.ViewHolder(binding.root) {

    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        var showingIcons = initialShowingIcons

        initializeAttributionText(author, itemView.context)

        if (initialShowingIcons) {
            showIcons()
        } else {
            hideIcons()
        }

        binding.root.setOnClickListener {
            showingIcons = !showingIcons
            if (showingIcons) {
                showIcons()
            } else {
                hideIcons()
            }

            setShowingIcons(showingIcons)
        }
    }

    private fun initializeAttributionText(author: AuthorAttribution, context: Context) {
        val template = context.getString(R.string.author_attr_template)
        val text = template.format(author.name, flaticonDisplayUrl)
        val spannableText = SpannableString(text)

        val flaticonStart = text.length - flaticonDisplayUrl.length
        spannableText.setSpan(
            UrlClickableSpan(flaticonUrl),
            flaticonStart,
            text.length,
            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )

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

    private fun showIcons() {
        // binding.imagesRecycler.visible()
        // binding.showHideIcons.text = context.getString(R.string.hide_icons)
    }

    private fun hideIcons() {
        // binding.imagesRecycler.gone()
        // binding.showHideIcons.text = context.getString(R.string.show_icons)
    }
}
