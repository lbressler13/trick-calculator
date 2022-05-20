package com.example.trickcalculator.ui.history

import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible

/**
 * ViewHolder for a single history item view
 *
 * @param binding [ViewHolderHistoryItemBinding]: view binding for the view holder
 */
class HistoryItemViewHolder(private val binding: ViewHolderHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // update UI to show information about current history item
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