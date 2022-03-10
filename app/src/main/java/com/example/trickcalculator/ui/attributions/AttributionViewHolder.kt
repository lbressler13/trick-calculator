package com.example.trickcalculator.ui.attributions

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.ViewHolderAttributionBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible

/**
 * ViewHolder for a single image attribution view.
 *
 * @param binding [ViewHolderAttributionBinding]: view binding for the view holder
 * @param context [Context]: activity context
 */
class AttributionViewHolder(private val binding: ViewHolderAttributionBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    // update UI to show information about the current attribution, including whether or not link should be visible
    fun update(attr: Attribution, initialShowingLink: Boolean, setShowingLink: (Boolean) -> Unit) {
        var showingLink = initialShowingLink
        binding.image.setImageResource(attr.iconResId)
        binding.creator.text = attr.creator
        binding.link.text = attr.url

        if (initialShowingLink) {
            showLink()
        } else {
            hideLink()
        }

        binding.root.setOnClickListener {
            if (showingLink) {
                hideLink()
            } else {
                showLink()
            }
            showingLink = !showingLink
            setShowingLink(showingLink)
        }
    }

    private fun hideLink() {
        binding.link.gone()
        binding.showHideLink.text = context.getString(R.string.show_link)
    }

    private fun showLink() {
        binding.link.visible()
        binding.showHideLink.text = context.getString(R.string.hide_link)
    }
}