package presentation.settings

sealed class SettingsEvent {
    data class SetDatePattern(val value: String) : SettingsEvent()
    data class SetLabelPattern(val value: String) : SettingsEvent()
    data class SetPrinterIp(val value: String) : SettingsEvent()
    data class SetPrinterPort(val value: String) : SettingsEvent()
    object SetDefaults : SettingsEvent()
    object SaveSettings : SettingsEvent()
}