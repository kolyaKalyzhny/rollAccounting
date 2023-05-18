package presentation.label

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AutoDismissSnackbar(
    showSnackBar: Boolean,
    durationMillis: Long = 3000L,
    onDismiss: () -> Unit = {}
) {
    if (showSnackBar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Операция завершена успешно")
        }

        LaunchedEffect(Unit) {
            delay(durationMillis)
            onDismiss()
        }
    }
}