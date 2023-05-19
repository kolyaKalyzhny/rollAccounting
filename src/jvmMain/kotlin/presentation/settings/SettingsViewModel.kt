package presentation.settings

import config.Config
import domain.interfaces.ConfigurationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import presentation.BaseViewModel


class SettingsViewModel(
    private val configurationService: ConfigurationService
) : BaseViewModel(onError = {}) {


    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    init {
        onEvent(SettingsEvent.SetDefaults)
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetDatePattern -> setDatePattern(event.value)
            is SettingsEvent.SetLabelPattern -> setLabelPattern(event.value)
            is SettingsEvent.SetPrinterIp -> setPrinterIp(event.value)
            is SettingsEvent.SetPrinterPort -> setPrinterPort(event.value)
            is SettingsEvent.SetDefaults -> setDefaults()
            is SettingsEvent.SaveSettings -> saveSettings()
        }
    }

    private fun setDefaults() {
        val datePattern = Config.datePattern
        val labelPattern = Config.gs1128Format
        val printerIp = Config.printerIp
        val printerPort = Config.printerPort.toString()

        _settingsState.update { currentState ->
            currentState.copy(
                datePattern = datePattern,
                labelPattern = labelPattern,
                printerIp = printerIp,
                printerPort = printerPort
            )
        }
    }


    private fun setDatePattern(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(datePattern = currentState.datePattern + value)
        }
    }

    private fun setLabelPattern(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(labelPattern = currentState.labelPattern + value)
        }
    }

    private fun setPrinterIp(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(printerIp = currentState.printerIp + value)
        }
    }

    private fun setPrinterPort(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(printerPort = currentState.printerPort + value)
        }
    }

    private fun saveSettings() {
        TODO("set all the config fields" +
                "Notify USER on success")
    }

    fun updateConfig() {
        val number = (0..10).random()
        Config.datePattern = number.toString()
    }

    fun getFresh() {
        val pattern = Config.datePattern
        println(pattern)
    }

}