package kh.sample.compose.refreshing_list.ui.list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kh.sample.compose.refreshing_list.ui.mock.StoreInstanceMockDataManger
import kh.sample.compose.refreshing_list.ui.model.TextModel
import kh.sample.compose.refreshing_list.ui.paging.SampleRepositoryImpl
import kh.sample.compose.refreshing_list.ui.paging.TextRemoteDataSource
import kh.sample.compose.refreshing_list.ui.usecase.GetSampleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    isRefreshing: Boolean,
    viewModel: ListViewModel = viewModel(),
    onItemClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.asStateFlow().collectAsStateWithLifecycle()
    val uiPagingItems: LazyPagingItems<TextModel> =
        viewModel.sampleListPagingState.collectAsLazyPagingItems()

    var scrollIndex by rememberSaveable { mutableStateOf(0) }
    var scrollOffset by rememberSaveable { mutableStateOf(0) }
    var lastItemCount by rememberSaveable { mutableStateOf(0) }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(uiPagingItems.itemSnapshotList) {
        val currentItemsCount = uiPagingItems.itemCount

        if (uiState.isRefreshing && (currentItemsCount == lastItemCount)) {
            listState.scrollToItem(scrollIndex, scrollOffset)
            viewModel.updateRefresh(false)
        }

    }

    LaunchedEffect(uiState.isRefreshing) {
        if (uiState.isRefreshing) {
            uiPagingItems.refresh()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updateRefresh(isRefreshing)
        if (uiPagingItems.itemSnapshotList.isEmpty()) {
            viewModel.loadDataPaging()
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    // user stopped
                    scrollIndex = listState.firstVisibleItemIndex
                    scrollOffset = listState.firstVisibleItemScrollOffset
                    lastItemCount = uiPagingItems.itemCount
                }
            }
    }


    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopAppBar(
                title = {
                    Text("List")
                }
            )
            Box(modifier = Modifier.fillMaxSize()) {

                PullToRefreshBox(isRefreshing = uiState.isRefreshing, onRefresh = {
                    StoreInstanceMockDataManger.updateRefresh()
                    viewModel.updateRefresh(true)
                }) {
                    LazyColumn(state = listState) {
                        items(uiPagingItems.itemCount, key = {
                            it
                        }) { index ->
                            Column(modifier = Modifier.clickable {
                                onItemClick(uiPagingItems[index]!!.index)
                            }
                            ) {
                                val data = uiPagingItems[index]!!
                                Text(
                                    modifier = Modifier.padding(16.dp), text = "Test data no: ${
                                        data.index
                                    }"
                                )
                                Text(
                                    color = if (data.status) Color.Green else Color.Red,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = "Status = ${data.status}"
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ListUiState(
    val isRefreshing: Boolean = false,
)

class ListViewModel() : ViewModel() {
    val uiState = MutableStateFlow(ListUiState())

    val repository = SampleRepositoryImpl(
        TextRemoteDataSource()
    )
    private val useCase = GetSampleUseCase(
        repository
    )

    private val _sampleListState =
        MutableStateFlow(PagingData.empty<TextModel>())

    val sampleListPagingState = _sampleListState.cachedIn(viewModelScope)

    fun loadDataPaging() {
        viewModelScope.launch {
            useCase.invoke(Unit)
                .cachedIn(viewModelScope)
                .collect {
                    _sampleListState.value = it
                }
        }
    }

    fun updateRefresh(refresh: Boolean) {
        uiState.update {
            it.copy(isRefreshing = refresh)
        }
    }

}