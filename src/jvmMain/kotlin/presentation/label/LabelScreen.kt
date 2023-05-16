package presentation.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import koin
import presentation.common.LocalLabelViewModel

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = koin.get()
) {

//    val labelViewModel = LocalLabelViewModel.current
//    val scans by labelViewModel.labelState.collectAsState()
    val scans by viewModel.labelState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Label Screen", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        LazyColumn {
            items(scans.scans) {
                Text(text = it)
            }
        }
    }
}