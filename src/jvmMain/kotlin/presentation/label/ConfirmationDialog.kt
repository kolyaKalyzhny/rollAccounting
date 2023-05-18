package presentation.label

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

data class DialogState(
    val content: String,
    val isVisible: Boolean
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmationDialog(onDismiss: () -> Unit, title: String, content: String) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = content) },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "Confirm")
            }
        }
    )
}