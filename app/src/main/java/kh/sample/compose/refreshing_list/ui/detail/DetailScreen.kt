package kh.sample.compose.refreshing_list.ui.detail

import android.annotation.SuppressLint
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(number: Int,onBack: () -> Unit) {

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TopAppBar(
                title = {
                    Text("Detail: $number")
                }
            )

            Button(modifier = Modifier.padding(16.dp), onClick = { /*TODO*/ }) {
                Text("Load data")
            }
            Button(modifier = Modifier.padding(16.dp), onClick = {
                onBack()
            }) {
                Text("Back")
            }
        }
    }
}