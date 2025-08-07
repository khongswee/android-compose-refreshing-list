package kh.sample.compose.refreshing_list.ui.mock

/**
 * Using for tricky data has been updated status
 */
object StoreInstanceMockDataManger {
    var isRefresh = false
        private set

    fun updateRefresh() {
        isRefresh = true
    }

    fun resetRefresh() {
        isRefresh = false
    }
}