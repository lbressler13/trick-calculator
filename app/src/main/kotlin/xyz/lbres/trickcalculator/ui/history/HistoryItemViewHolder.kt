package xyz.lbres.trickcalculator.ui.history

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.kotlinutils.general.simpleIf
import xyz.lbres.trickcalculator.databinding.ViewHolderHistoryItemBinding

/**
 * ViewHolder for a single history item view
 *
 * @param binding [ViewHolderHistoryItemBinding]: view binding for the view holder
 */
class HistoryItemViewHolder(private val binding: ViewHolderHistoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // update UI to show information about current history item
    fun update(item: HistoryItem) {
        binding.computeText.text = item.computation.joinToString("")
        binding.resultText.text = item.result?.toDecimalString(5) ?: ""
        binding.errorText.text = item.error ?: ""

        binding.resultText.visibility = simpleIf(item.result == null, View.GONE, View.VISIBLE)
        binding.errorText.visibility = simpleIf(item.result == null, View.VISIBLE, View.GONE)
    }
}
