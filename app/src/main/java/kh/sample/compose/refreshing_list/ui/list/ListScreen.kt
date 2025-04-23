package kh.sample.compose.refreshing_list.ui.list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(viewModel: ListViewModel = viewModel(), onItemClick: (Int) -> Unit) {

    val itemsMock by viewModel.listUiState.asStateFlow().collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopAppBar(
                title = {
                    Text("List")
                }
            )
            LazyColumn {
                items(itemsMock) {
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

class ListViewModel() : ViewModel() {
    val listUiState = MutableStateFlow(listOf<Int>())

    fun loadData() {
        viewModelScope.launch {
            delay(2000)
            listUiState.value = List<Int>(30) {
                it
            }
        }
    }

    fun refreshLoad() {
        loadData()
    }

}