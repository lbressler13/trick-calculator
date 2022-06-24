package com.example.trickcalculator.ui.attributions.imageattribution

import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderImageAttributionBinding
import com.example.trickcalculator.ui.attributions.ImageAttribution
import com.example.trickcalculator.ui.attributions.UrlClickableSpan

class ImageAttributionViewHolder(private val binding: ViewHolderImageAttributionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(image: ImageAttribution) {
        // initialize icon
        val contentDescription = itemView.context.getString(image.contentDescriptionId)
        binding.image.contentDescription = contentDescription
        binding.image.setImageResource(image.iconResId)

        // initialize link
        val spannableString = SpannableString(image.url)
        spannableString.setSpan(
            UrlClickableSpan(image.url), 0, image.url.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.link.movementMethod = LinkMovementMethod()
        binding.link.text = spannableString
    }
}
