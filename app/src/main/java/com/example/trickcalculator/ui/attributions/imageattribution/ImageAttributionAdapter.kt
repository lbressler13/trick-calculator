package com.example.trickcalculator.ui.attributions.imageattribution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderImageAttributionBinding
import com.example.trickcalculator.ui.attributions.ImageAttribution

/**
 * Adapter for image attribution views for the RecyclerView in an author attribution
 *
 * @param images [List<ImageAttribution>]: list of ImageAttribution objects
 */
class ImageAttributionAdapter(private val images: List<ImageAttribution>) :
    RecyclerView.Adapter<ImageAttributionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAttributionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val binding = ViewHolderImageAttributionBinding.inflate(layoutInflater, parent, false)
        return ImageAttributionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageAttributionViewHolder, position: Int) {
        val image = images[position]
        holder.update(image)
    }

    override fun getItemCount(): Int = images.size
}
