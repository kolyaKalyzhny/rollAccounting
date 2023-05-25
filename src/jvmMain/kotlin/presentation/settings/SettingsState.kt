package presentation.settings

import config.Config


data class SettingsState(
    val datePattern: String = "",
    val labelPattern: String = "",
    val printerIp: String = "",
    val printerPort: String = "",
    val scannerName: String = "",
    val backendUrl: String = "",
) {
    fun toList() = listOf(datePattern, labelPattern, printerIp, printerPort, scannerName, backendUrl)

    fun toMap() = mapOf(
//        "datePattern" to datePattern,
        Config.DATE_PATTERN_KEY to datePattern,
        Config.LABEL_PATTERN_KEY to labelPattern,
        Config.PRINTER_IP_KEY to printerIp,
        Config.PRINTER_PORT_KEY to printerPort,
        Config.SCANNER_NAME_KEY to scannerName,
        Config.BACKEND_URL_KEY to backendUrl
    )
}
