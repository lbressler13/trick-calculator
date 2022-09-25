package xyz.lbres.trickcalculator.ui.attributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinutils.list.ext.copyWithReplacement

/**
 * ViewModel to track information about expanded/collapsed views in the Attributions fragment UI
 */
class AttributionsViewModel : ViewModel() {
    // whether or not the Flaticon message at top of screen is expanded
    private val mFlaticonMessageExpanded = MutableLiveData<Boolean>().apply { value = false }
    val flaticonMessageExpanded: LiveData<Boolean> = mFlaticonMessageExpanded
    fun setFlaticonMessageExpanded(newValue: Boolean) { mFlaticonMessageExpanded.value = newValue }

    // information about attributions
    private val mAttributionsExpanded = MutableLiveData<List<Boolean>>().apply { value = listOf() }
    val attributionsExpanded: LiveData<List<Boolean>> = mAttributionsExpanded

    // if attributionsExpanded isn't already set, initialize to all closed
    fun initAttributionsExpanded(numAttributions: Int) {
        if (attributionsExpanded.value?.size != numAttributions) {
            mAttributionsExpanded.value = List(numAttributions) { false }
        }
    }

    // set if a specific attribution is expanded
    fun setExpandedAt(newValue: Boolean, index: Int) {
        val currentList = attributionsExpanded.value!!
        val newList = currentList.copyWithReplacement(index, newValue)
        mAttributionsExpanded.value = newList
    }
}
