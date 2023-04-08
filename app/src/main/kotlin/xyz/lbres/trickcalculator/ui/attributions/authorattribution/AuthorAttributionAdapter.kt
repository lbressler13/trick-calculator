package xyz.lbres.trickcalculator.ui.attributions.authorattribution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.lbres.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import xyz.lbres.trickcalculator.ui.attributions.AttributionsViewModel
import xyz.lbres.trickcalculator.ui.attributions.AuthorAttribution

/**
 * Adapter for author attributions for the RecyclerView in the AttributionsFragment
 *
 * @param authors [List]<[AuthorAttribution]>: list of AuthorAttribution objects
 * @param viewModel [AttributionsViewModel]: view model containing information about which attributions are expanded
 */
class AuthorAttributionAdapter(private val authors: List<AuthorAttribution>, private val viewModel: AttributionsViewModel) :
    RecyclerView.Adapter<AuthorAttributionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorAttributionViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding = ViewHolderAuthorAttributionBinding.inflate(layoutInflater, parent, false)
        return AuthorAttributionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorAttributionViewHolder, position: Int) {
        val author = authors[position]
        val setShowingIcons: (Boolean) -> Unit = { viewModel.attributionsExpanded[position] = it }
        holder.update(author, viewModel.attributionsExpanded[position], setShowingIcons)
    }

    override fun getItemCount(): Int = authors.size
}
