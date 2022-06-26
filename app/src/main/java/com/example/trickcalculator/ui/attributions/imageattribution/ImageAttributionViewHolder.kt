package com.example.trickcalculator.ui.attributions.imageattribution

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import com.example.trickcalculator.databinding.ViewHolderImageAttributionBinding
import com.example.trickcalculator.ui.attributions.ImageAttribution
import com.example.trickcalculator.ui.attributions.UrlClickableSpan

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

        // initialize link to icon
        val spannableString = SpannableString(image.url)
        UrlClickableSpan.addToFirstWord(spannableString, image.url, image.url)
        binding.link.movementMethod = LinkMovementMethod()
        binding.link.text = spannableString
    }
}
