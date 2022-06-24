package com.example.trickcalculator.ui.attributions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trickcalculator.MainActivity
import com.example.trickcalculator.R
import com.example.trickcalculator.databinding.FragmentImageAttributionsBinding
import com.example.trickcalculator.ui.settings.Settings
import com.example.trickcalculator.ui.settings.initSettingsFragment
import com.example.trickcalculator.ui.shared.SharedViewModel
import com.example.trickcalculator.ui.settings.initSettingsObservers

// TODO correct attributions

/**
 * Information about a single image attribution
 *
 * @param iconResId [Int]: resourceId of the drawable resource for the image
 * @param creator [String]: creator of image on Flaticon
 * @param url [String]: URL to view image
 */
data class Attribution(
    val iconResId: Int,
    val creator: String,
    val url: String
)

// attributions for all images in app
private val allAttributions = listOf(
    Attribution(R.drawable.launcher, "Pixel perfect", "https://www.flaticon.com/free-icon/keys_2891382"),
    Attribution(R.drawable.ic_arrow_left, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/left_3416141"),
    Attribution(R.drawable.ic_close, "Ilham Fitrotul Hayat", "www.flaticon.com/premium-icon/cross_4421536"),
    Attribution(R.drawable.ic_divide, "Smashicons", "www.flaticon.com/free-icon/divide_149702"),
    Attribution(R.drawable.ic_equals, "Freepik", "www.flaticon.com/free-icon/equal_56751"),
    Attribution(R.drawable.ic_history, "IconKanan", "www.flaticon.com/premium-icon/history_2901149"),
    Attribution(R.drawable.ic_info, "Freepik", "www.flaticon.com/free-icon/info-button_64494"),
    Attribution(R.drawable.ic_minus, "Freepik", "www.flaticon.com/free-icon/minus_56889"),
    Attribution(R.drawable.ic_plus, "Freepik", "www.flaticon.com/premium-icon/plus_3524388"),
    Attribution(R.drawable.ic_settings, "Freepik", "https://www.flaticon.com/premium-icon/gear_484613"),
    Attribution(R.drawable.ic_times, "Freepik", "www.flaticon.com/free-icon/multiply-mathematical-sign_43823")
)

/**
 * Fragment to display image attributions for all Flaticon images used in the app, as required by Flaticon
 */
class AttributionsFragment : Fragment() {
    private lateinit var binding: FragmentImageAttributionsBinding
    private lateinit var sharedViewModel: SharedViewModel

    private val settings = Settings()

    companion object {
        fun newInstance() = AttributionsFragment()
    }

    /**
     * Initialize fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageAttributionsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val recycler: RecyclerView = binding.attributionsRecycler
        val adapter = AttributionsAdapter(allAttributions)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())

        binding.closeButton.root.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        initActionBar()
        (requireActivity() as MainActivity).fragmentManager = childFragmentManager
        initSettingsObservers(settings, sharedViewModel, viewLifecycleOwner)

        return binding.root
    }

    /**
     * Set functionality in action bar
     */
    private fun initActionBar() {
        val actionBar = (requireActivity() as MainActivity).binding.actionBar
        initSettingsFragment(this, settings, actionBar.root)
        actionBar.title.text = requireContext().getString(R.string.title_action_bar)
    }
}
