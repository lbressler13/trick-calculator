package xyz.lbres.trickcalculator.ui.history

import androidx.lifecycle.ViewModel
import xyz.lbres.trickcalculator.utils.History

// info about history and randomness that are currently displayed, for purpose of knowing when to make updates
class HistoryViewModel : ViewModel() {
    var randomness: Int? = null
    var history: History? = null
        private set
    var randomizedHistory: History? = null

    fun setHistory(newValue: History?) {
        if (newValue != null) {
            history = List(newValue.size) { newValue[it] }
        }
    }
}
