package kh.sample.compose.refreshing_list.ui.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(
    isRefreshing: Boolean = false,
    viewModel: ListViewModel = viewModel(),
    onItemClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.asStateFlow().collectAsStateWithLifecycle()

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.refreshLoad()
        }
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopAppBar(
                title = {
                    Text("List")
                }
            )
            if (uiState.isRefreshing) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }
            LazyColumn {
                items(uiState.items) {
                    Column(modifier = Modifier.clickable {
                        onItemClick(it)
                    }
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = "Test data no: $it")
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

data class ListUiState(
    val items: List<Int> = listOf(),
    val isRefreshing: Boolean = false
)

class ListViewModel() : ViewModel() {
    val uiState = MutableStateFlow(ListUiState())

    init {
        loadData()
    }

    private fun loadData() {
        Log.d("ListViewModel", "===> loadData")
        viewModelScope.launch {
            uiState.update {
                it.copy(isRefreshing = true)
            }
            delay(2000)
            uiState.update {
                it.copy(
                    isRefreshing = false, items = List<Int>(30) { it })
            }
        }
    }

    fun refreshLoad() {
        Log.d("ListViewModel", "===> refresh")
        loadData()
    }

}