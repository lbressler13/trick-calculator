package com.example.trickcalculator.ui.attributions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderAttributionBinding

class AttributionsAdapter(private val attributions: List<Attribution>) :
    RecyclerView.Adapter<AttributionViewHolder>() {
    private val showingLinks: MutableList<Boolean> = MutableList(attributions.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttributionViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding = ViewHolderAttributionBinding.inflate(layoutInflater, parent, false)
        return AttributionViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: AttributionViewHolder, position: Int) {
        val attr = attributions[position]
        val setShowingLink: (Boolean) -> Unit = { showingLinks[position] = it }
        holder.update(attr, showingLinks[position], setShowingLink)
    }

    override fun getItemCount(): Int {
        return attributions.size
    }
}