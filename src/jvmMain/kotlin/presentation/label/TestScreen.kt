package presentation.label

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.UIResources

@Composable
fun TestScreen() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    AppTitle(UIResources.app_title)
                    Spacer(Modifier.height(8.dp))
                    ConnectionStatus(true)
                    Spacer(Modifier.height(8.dp))
                    Instructions(
                        listOf(
                            "Make sure the scanner is connected.",
                            "Scan the barcode.",
                            "Wait for the processed barcode.",
                            "Confirm to proceed with the next use case."
                        )
                    )

                }
            }
        }
    }
}

//@Composable
//fun AppTitle(title: String) {
//    Text(
//        text = title,
//        style = MaterialTheme.typography.h5,
//        color = Color.DarkGray
//    )
//}
//
//@Composable
//fun ConnectionStatus(isConnected: Boolean) {
//    val color = if (isConnected) Color.Green else Color.Red
//    val statusText = if (isConnected) "connected" else "disconnected"
//
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        Icon(
//            Icons.Default.Circle,
//            contentDescription = "Connection Status",
//            tint = color
//        )
//        Spacer(Modifier.width(8.dp))
//        Text(
//            text = "Status: $statusText",
//            style = MaterialTheme.typography.body1,
//            color = Color.Gray
//        )
//    }
//}
//
//
//@Composable
//fun Instructions(instructions: List<String>) {
//
//    Column {
//        Text(
//            text = "Instructions:",
//            style = MaterialTheme.typography.body1,
//            color = Color.LightGray,
//            modifier = Modifier.padding(bottom = 8.dp)
//        )
//
//        instructions.forEach { instruction ->
//            Text(
//                text = "- $instruction",
//                style = MaterialTheme.typography.body2,
//                color = Color.LightGray
//            )
//        }
//    }
//}

@Composable
@Preview
fun TestScreenPreview() {
    TestScreen()
}