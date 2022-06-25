package com.example.trickcalculator.ui.attributions.authorattribution

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.databinding.ViewHolderAuthorAttributionBinding
import com.example.trickcalculator.ui.attributions.AttributionsViewModel
import com.example.trickcalculator.ui.attributions.AuthorAttribution

class AuthorAttributionAdapter(private val authors: List<AuthorAttribution>, private val viewModel: AttributionsViewModel, private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<AuthorAttributionViewHolder>() {
    // whether or not icons are visible for each author attribution
    private var showingIcons = List(authors.size) { false }

    init {
        viewModel.attributionsExpanded.observe(lifecycleOwner) {
            showingIcons = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorAttributionViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)

        val binding = ViewHolderAuthorAttributionBinding.inflate(layoutInflater, parent, false)
        return AuthorAttributionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AuthorAttributionViewHolder, position: Int) {
        val author = authors[position]
        val setShowingIcons: (Boolean) -> Unit = { viewModel.setExpandedAt(it, position) }
        holder.update(author, showingIcons[position], setShowingIcons)
    }

    override fun getItemCount(): Int = authors.size
}
