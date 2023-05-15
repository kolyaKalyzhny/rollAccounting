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
import presentation.common.LocalLabelViewModel

@Composable
fun LabelScreen() {

    val labelViewModel = LocalLabelViewModel.current
    val scans by labelViewModel.labelState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyColumn {
            items(scans.scans) {
                Text(text = it)
            }
        }
    }
}