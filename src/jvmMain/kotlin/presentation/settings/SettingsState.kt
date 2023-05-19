package presentation.settings


data class SettingsState(
    val datePattern: String = "",
    val labelPattern: String = "",
    val printerIp: String = "",
    val printerPort: String = ""
)
