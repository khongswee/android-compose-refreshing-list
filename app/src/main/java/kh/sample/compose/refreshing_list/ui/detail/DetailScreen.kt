package kh.sample.compose.refreshing_list.ui.detail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kh.sample.compose.refreshing_list.ui.list.ListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.asStateFlow().collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopAppBar(
                title = {
                    Text("Detail: ${uiState.number}")
                }
            )

            Button(modifier = Modifier.padding(horizontal = 12.dp), onClick = { }) {
                Text("Back and refresh")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(modifier = Modifier.padding(horizontal = 12.dp), onClick = {
                onBack()
            }) {
                Text("Back")
            }
        }
    }
}

class DetailViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val uiState = MutableStateFlow(DetailUIState())

    init {
        val number = savedStateHandle.get<Int>("number")
        uiState.update {
            it.copy(number = number ?: 0)
        }
        Log.d("DetailViewModel", "Number: $number")
    }

}

data class DetailUIState(val number: Int = 0)