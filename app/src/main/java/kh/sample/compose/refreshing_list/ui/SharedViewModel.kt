package kh.sample.compose.refreshing_list.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SharedViewModel : ViewModel() {
    val isRefreshingObservable = MutableStateFlow(false)
    fun updateRefresh(isRefreshing: Boolean) {
        isRefreshingObservable.value = isRefreshing
    }
}