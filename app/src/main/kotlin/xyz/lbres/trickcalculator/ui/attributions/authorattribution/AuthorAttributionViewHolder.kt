package xyz.lbres.trickcalculator.ui.attributions.authorattribution

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.trickcalculator.R
import xyz.lbres.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import xyz.lbres.trickcalculator.ui.attributions.AuthorAttribution
import xyz.lbres.trickcalculator.ui.attributions.ImageAttribution
import xyz.lbres.trickcalculator.ui.attributions.URLClickableSpan
import xyz.lbres.trickcalculator.ui.attributions.constants.flaticonDisplayUrl
import xyz.lbres.trickcalculator.ui.attributions.constants.flaticonUrl
import xyz.lbres.trickcalculator.ui.attributions.imageattribution.ImageAttributionAdapter
import xyz.lbres.trickcalculator.ext.view.gone
import xyz.lbres.trickcalculator.ext.view.visible

/**
 * ViewHolder for a single author attribution. Includes dropdown for image attributions.
 *
 * @param binding [ViewHolderAuthorAttributionBinding]: view binding for the view holder
 */
class AuthorAttributionViewHolder(private val binding: ViewHolderAuthorAttributionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // constant icons and content descriptions
    private val expandIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_right)
    private val collapseIcon = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_chevron_down)
    private val expandContentDescription = itemView.context.getString(R.string.expand_images_dropdown_cd)
    private val collapseContentDescription = itemView.context.getString(R.string.collapse_images_dropdown_cd)

    // update UI to show information about current author
    fun update(
        author: AuthorAttribution,
        initialShowingIcons: Boolean,
        setShowingIcons: (Boolean) -> Unit
    ) {
        initializeAttributionText(author)
        initializeAdapter(author.images)
        var showingIcons = initialShowingIcons

        showHideIcons(initialShowingIcons)

        binding.expandCollapseButton.setOnClickListener {
            showingIcons = !showingIcons
            setShowingIcons(showingIcons) // update viewmodel
            showHideIcons(showingIcons) // update ui
        }
    }

    // show or hide icons
    private fun showHideIcons(visible: Boolean) {
        if (visible) {
            // show icons + update dropdown icon
            binding.expandCollapseButton.setImageDrawable(collapseIcon)
            binding.expandCollapseButton.contentDescription = collapseContentDescription
            binding.imagesLayout.visible()
        } else {
            // hide icons + update dropdown icon
            binding.expandCollapseButton.setImageDrawable(expandIcon)
            binding.expandCollapseButton.contentDescription = expandContentDescription
            binding.imagesLayout.gone()
        }
    }

    // initialize text with author's name and add links
    private fun initializeAttributionText(author: AuthorAttribution) {
        // get text template and format with URLs
        val text = itemView.context.getString(R.string.author_attr_template, author.name, flaticonDisplayUrl)
        val spannableString = SpannableString(text)

        // link to flaticon site
        URLClickableSpan.addToFirstWord(spannableString, flaticonDisplayUrl, flaticonUrl)
        // link to creator
        URLClickableSpan.addToFirstWord(spannableString, author.name, author.url)

        binding.attribution.movementMethod = LinkMovementMethod()
        binding.attribution.text = spannableString
    }

    // initialize images dropdown
    private fun initializeAdapter(images: List<ImageAttribution>) {
        val recycler: RecyclerView = binding.imagesRecycler
        val adapter = ImageAttributionAdapter(images)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(itemView.context)
    }
}
