package xyz.lbres.trickcalculator.ui.attributions.imageattribution

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.trickcalculator.databinding.ViewHolderImageAttributionBinding
import xyz.lbres.trickcalculator.ui.attributions.ImageAttribution
import xyz.lbres.trickcalculator.ui.attributions.URLClickableSpan

/**
 * ViewHolder for a single image attribution
 *
 * @param binding [ViewHolderImageAttributionBinding]: view binding for the view holder
 */
class ImageAttributionViewHolder(private val binding: ViewHolderImageAttributionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // update UI to show information about current image
    fun update(image: ImageAttribution) {
        // initialize icon
        val contentDescription = itemView.context.getString(image.contentDescriptionId)
        binding.image.contentDescription = contentDescription
        binding.image.setImageResource(image.iconResId)

        // initialize link
        val spannableString = SpannableString(image.url)
        URLClickableSpan.addToText(spannableString, image.url)
        binding.link.movementMethod = LinkMovementMethod()
        binding.link.text = spannableString
    }
}
