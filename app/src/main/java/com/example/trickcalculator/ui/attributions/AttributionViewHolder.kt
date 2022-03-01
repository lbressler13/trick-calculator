package com.example.trickcalculator.ui.attributions

import android.content.Context
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderAttributionBinding

class AttributionViewHolder(private val binding: ViewHolderAttributionBinding, private val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(attr: Attribution) {
        // binding.image.background = AppCompatResources.getDrawable(context, attr.iconResId)
        binding.image.setImageResource(attr.iconResId)
        binding.creator.text = attr.creator
        // TODO onclick for link
        binding.link.setOnClickListener { Log.e(null, attr.url) }
    }
}