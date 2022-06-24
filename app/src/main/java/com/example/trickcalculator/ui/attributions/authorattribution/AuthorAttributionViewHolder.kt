package com.example.trickcalculator.ui.attributions.authorattribution

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.attributions.AuthorAttribution
import com.example.trickcalculator.ui.attributions.UrlClickableSpan

class AuthorAttributionViewHolder(
    private val binding: ViewHolderAuthorAttributionBinding,
    private val context: Context
) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        var showingIcons = initialShowingIcons

        initializeAttributionText(author)

        // TODO link

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

    private fun initializeAttributionText(author: AuthorAttribution) {
        val prefix = "Icons made by "
        val suffix = " from www.flaticon.com"
        val text = "$prefix${author.name}$suffix"
        val spannableText = SpannableString(text)

        val flaticonStart = text.length - "www.flaticon.com".length
        spannableText.setSpan(
            UrlClickableSpan("https://www.flaticon.com"),
            flaticonStart,
            text.length,
            SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
        )

        val authorStart = prefix.length
        val authorEnd = text.length - suffix.length
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
