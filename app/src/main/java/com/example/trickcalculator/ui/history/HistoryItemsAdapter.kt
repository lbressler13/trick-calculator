package com.example.trickcalculator.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding

class HistoryItemsAdapter(private val items: List<HistoryItem>) :
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

    override fun getItemCount(): Int {
        return items.size
    }
}