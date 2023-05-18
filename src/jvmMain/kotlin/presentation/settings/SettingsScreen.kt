package presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import config.Config

@Composable
fun SettingsScreen() {
    val viewModel = SettingsViewModel()

    Column {
        Button(onClick = { viewModel.updateConfig() }) {
            Text("change props")
        }

        Button(onClick = { viewModel.getFresh() }) {
            Text("get pattern")
        }

        Text("Hello, ${Config.datePattern}")
    }
}
