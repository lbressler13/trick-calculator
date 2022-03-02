package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentImageAttributionsBinding

data class Attribution(
    val iconResId: Int,
    val creator: String,
    val url: String
)

private val allAttributions = listOf(
    Attribution(R.drawable.ic_arrow_left, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/left_3416141"),
    Attribution(R.drawable.ic_close, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/cross_4421536"),
    Attribution(R.drawable.ic_divide, "Smashicons", "www.flaticon.com/free-icon/divide_149702"),
    Attribution(R.drawable.ic_equals, "Freepik", "www.flaticon.com/free-icon/equal_56751"),
    Attribution(R.drawable.ic_info, "Freepik", "www.flaticon.com/premium-icon/info_471662"),
    Attribution(R.drawable.ic_minus, "Freepik", "www.flaticon.com/free-icon/minus_56889"),
    Attribution(R.drawable.ic_plus, "Freepik", "www.flaticon.com/premium-icon/plus_3524388"),
    Attribution(R.drawable.ic_settings, "Freepik", "www.flaticon.com/premium-icon/gear_484613"),
    Attribution(R.drawable.ic_times, "Freepik", "www.flaticon.com/free-icon/multiply-mathematical-sign_43823")
)

class AttributionsFragment : Fragment() {
    private lateinit var binding: FragmentImageAttributionsBinding

    companion object {
        fun newInstance() = AttributionsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageAttributionsBinding.inflate(layoutInflater)

        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AttributionsAdapter(allAttributions)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.closeButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

}