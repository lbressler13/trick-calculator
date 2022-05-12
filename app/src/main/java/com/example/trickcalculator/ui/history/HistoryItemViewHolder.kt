package com.example.trickcalculator.ui.history

import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderHistoryItemBinding
import com.example.trickcalculator.exactfraction.ExactFraction
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.substringTo
import com.example.trickcalculator.ext.visible

class HistoryItemViewHolder(private val binding: ViewHolderHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun update(item: HistoryItem) {
        setComputation(item.computation)
        setResult(item.result)
        setError(item.error)

        if (item.result != null) {
            binding.resultTextview.visible()
            binding.errorTextview.gone()
        } else {
            binding.resultTextview.gone()
            binding.errorTextview.visible()
        }

    }

    private fun setResult(result: ExactFraction?) = setTextView(
        binding.resultTextview,
        result?.toDecimalString(5) ?: "",
        3
    )

    private fun setError(error: String?) = setTextView(
        binding.errorTextview,
        error ?: "",
         2
    )

    private fun setComputation(computation: String) = setTextView(
        binding.computeTextview,
        computation,
        2
    )

    private fun setTextView(textview: TextView, text: String, maxLines: Int) {
        textview.text = text

        textview.doOnLayout {
            if (textview.lineCount > maxLines) {
                val textEnd = textview.layout.getLineStart(maxLines) - 3
                textview.text = text.substringTo(textEnd) + "..."
            }
        }
    }
}