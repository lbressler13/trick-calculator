package com.example.trickcalculator.ui.attributions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trickcalculator.ext.copyWithReplacement

/**
 * ViewModel to track information about view in the Attributions fragment UI
 */
class AttributionsViewModel : ViewModel() {
    // whether or not the top text is expanded
    private val mTextExpanded = MutableLiveData<Boolean>().apply { value = false }
    val textExpanded: LiveData<Boolean> = mTextExpanded
    fun setTextExpanded(newValue: Boolean) { mTextExpanded.value = newValue }

    // information about attributions
    private var attributionCount: Int? = null
    private val mAttributionsExpanded = MutableLiveData<List<Boolean>>().apply { value = listOf() }
    val attributionsExpanded: LiveData<List<Boolean>> = mAttributionsExpanded

    // set current number of attributions and init attributionsExpanded list
    fun setAttributionCount(newValue: Int) {
        if (attributionCount != newValue) {
            attributionCount = newValue
            mAttributionsExpanded.value = List(newValue) { false }
        }
    }

    // set if a specific attribution is expanded
    fun setExpandedAt(newValue: Boolean, index: Int) {
        val currentList = attributionsExpanded.value!!
        val newList = currentList.copyWithReplacement(index, newValue)
        mAttributionsExpanded.value = newList
    }
}
