package presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import config.UIResources

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    widthFraction: Float = 1f,
    heightFraction: Float = 1f,
    maxWidth: Int,
    maxHeight: Int
) {

    val settingsState by viewModel.settingsState.collectAsState()

    val maxWidthDp = with(LocalDensity.current) { maxWidth.toDp() }
    val maxHeightDp = with(LocalDensity.current) { maxHeight.toDp() }


    Card(
        modifier = Modifier
            .width(maxWidthDp * widthFraction)
            .height(maxHeightDp * heightFraction),
        shape = RoundedCornerShape(16.dp),
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = settingsState.backendUrl,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetBackendUrl(it)) },
                    label = { Text(UIResources.backend_url) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = settingsState.printerIp,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetPrinterIp(it)) },
                    label = { Text(UIResources.printer_ip_address) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = settingsState.printerPort,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetPrinterPort(it)) },
                    label = { Text(UIResources.printer_port) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = settingsState.scannerName,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetScannerName(it)) },
                    label = { Text(UIResources.scanner_name) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = settingsState.labelPattern,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetLabelPattern(it)) },
                    label = { Text(UIResources.gtin_pattern) }
                )

                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = settingsState.datePattern,
                    onValueChange = { viewModel.onEvent(SettingsEvent.SetDatePattern(it)) },
                    label = { Text(UIResources.date_pattern) }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { viewModel.onEvent(SettingsEvent.SetDefaults) }) {
                    Text(UIResources.settings_set_defaults)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { viewModel.onEvent(SettingsEvent.SaveSettings) }) {
                    Text(UIResources.settings_save_changes)
                }
            }
        }
    }
}
