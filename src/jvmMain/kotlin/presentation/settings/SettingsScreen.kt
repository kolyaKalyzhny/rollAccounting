package presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp
import config.Config
import config.UIResources

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    widthFraction: Float = 0.5f,
    heightFraction: Float = 0.4f,
    maxWidth: Int,
    maxHeight: Int
) {
    val defaultIpAddress = "192.168.0.1"

    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }
    val maxHeightDp = with(LocalDensity.current) { maxHeight.toDp() }
    val ipAddressState = remember { mutableStateOf(defaultIpAddress) }

    Card(
        modifier = Modifier
            .width(maxWidthDp * widthFraction)
            .height(maxHeightDp * heightFraction),
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            OutlinedTextField(
                value = ipAddressState.value,
                onValueChange = { ipAddressState.value = it },
                placeholder = { Text(UIResources.printer_ip_address) },
                label = { Text("hello") }
//                placeholder = { Text(UIResources.printer_ip_address) },
//                label = { Text(UIResources.printer_ip_address) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = ipAddressState.value,
                onValueChange = {},
                label = { Text(UIResources.printer_ip_address) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = "",
                onValueChange = {},
                label = { Text(UIResources.printer_port) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = "",
                onValueChange = {},
                label = { Text(UIResources.scanner_name) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = "",
                onValueChange = {},
                label = { Text(UIResources.gtin_pattern) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = "",
                onValueChange = {},
                label = { Text(UIResources.date_pattern) }
            )

            Row {
                Button(onClick = {}) {
                    Text(UIResources.settings_save_changes)
                }
            }

            Text("Hello, ${Config.datePattern}")
        }
    }
}
