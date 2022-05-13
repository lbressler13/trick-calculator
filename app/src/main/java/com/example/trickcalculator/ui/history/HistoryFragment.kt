package com.example.trickcalculator.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentHistoryBinding
import com.example.trickcalculator.ext.gone
import com.example.trickcalculator.ext.visible
import com.example.trickcalculator.ui.shared.SharedViewModel

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var viewModel: SharedViewModel

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // should only get called once
        viewModel.history.observe(viewLifecycleOwner) { setUI(it) }

        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    private fun setUI(historyItems: List<HistoryItem>) {
        if (historyItems.isEmpty()) {
            binding.itemsRecycler.gone()
            binding.noHistoryMessage.visible()
        } else {
            val recycler: RecyclerView = binding.itemsRecycler
            val adapter = HistoryItemsAdapter(historyItems)

            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
