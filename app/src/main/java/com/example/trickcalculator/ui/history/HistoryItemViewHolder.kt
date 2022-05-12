package com.example.trickcalculator.ui.history

import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible

class HistoryItemViewHolder(private val binding: ViewHolderHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(item: HistoryItem) {
        binding.computeText.text = item.computation
        binding.resultText.text = item.result?.toDecimalString(5) ?: ""
        binding.errorText.text = item.error ?: ""

        if (item.result != null) {
            binding.resultText.visible()
            binding.errorText.gone()
        } else {
            binding.resultText.gone()
            binding.errorText.visible()
        }

    }
}