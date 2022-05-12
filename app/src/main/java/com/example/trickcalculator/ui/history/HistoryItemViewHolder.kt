package com.example.trickcalculator.ui.history

import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible

class HistoryItemViewHolder(private val binding: ViewHolderHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(item: HistoryItem) {
        binding.computeTextview.text = item.computation

        if (item.result != null) {
            binding.resultTextview.text = item.result.toDecimalString(5)
            binding.resultTextview.visible()
            binding.errorTextview.gone()
        } else {
            val error = item.error ?: "Error"
            binding.errorTextview.text = error
            binding.resultTextview.gone()
            binding.errorTextview.visible()
        }
    }
}