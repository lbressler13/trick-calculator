package com.example.trickcalculator.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding
import com.example.trickcalculator.utils.History

/**
 * Adapter for history item views for the RecyclerView in the HistoryFragment
 *
 * @param items [History]: list of HistoryItem objects
 */
class HistoryItemsAdapter(private val items: History) :
    RecyclerView.Adapter<HistoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewHolderHistoryItemBinding.inflate(layoutInflater, parent, false)
        return HistoryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val item = items[position]
        holder.update(item)
    }

    override fun getItemCount(): Int = items.size
}