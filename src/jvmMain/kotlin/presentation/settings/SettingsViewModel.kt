package presentation.settings

import config.Config
import config.UIResources
import domain.interfaces.ConfigurationService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.BaseViewModel

class SettingsViewModel(
    private val configurationService: ConfigurationService
) : BaseViewModel(onError = {}) {


    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState = _settingsState.asStateFlow()

    init {
        loadSettings()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SetDatePattern -> setDatePattern(event.value)
            is SettingsEvent.SetLabelPattern -> setLabelPattern(event.value)
            is SettingsEvent.SetPrinterIp -> setPrinterIp(event.value)
            is SettingsEvent.SetPrinterPort -> setPrinterPort(event.value)
            is SettingsEvent.SetBackendUrl -> setBackendUrl(event.value)
            is SettingsEvent.SetScannerName -> setScannerName(event.value)
            is SettingsEvent.SetDefaults -> setDefaults()
            is SettingsEvent.SaveSettings -> saveSettings()
        }
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val datePattern = configurationService.get(Config.DATE_PATTERN_KEY)
                val labelPattern = configurationService.get(Config.LABEL_PATTERN_KEY)
                val printerIp = configurationService.get(Config.PRINTER_IP_KEY)
                val printerPort = configurationService.get(Config.PRINTER_PORT_KEY)
                val scannerName = configurationService.get(Config.SCANNER_NAME_KEY)
                val backendUrl = configurationService.get(Config.BACKEND_URL_KEY)

                _settingsState.update { currentState ->
                    currentState.copy(
                        datePattern = datePattern,
                        labelPattern = labelPattern,
                        printerIp = printerIp,
                        printerPort = printerPort,
                        scannerName = scannerName,
                        backendUrl = backendUrl
                    )
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
                emitError(e.localizedMessage)
            }
        }
    }

    private fun setDefaults() {
        viewModelScope.launch {
            try {
                configurationService.setDefaultsAll()
                loadSettings()
                emitSuccess(UIResources.settings_resetting_defaults)
            } catch (e: Exception) {
                emitError(e.localizedMessage)
            }
        }
    }


    private fun setDatePattern(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(datePattern = value)
        }
    }

    private fun setLabelPattern(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(labelPattern = value)
        }
    }

    private fun setPrinterIp(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(printerIp = value)
        }
    }

    private fun setPrinterPort(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(printerPort = value)
        }
    }

    private fun setBackendUrl(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(backendUrl = value)
        }
    }

    private fun setScannerName(value: String) {
        _settingsState.update { currentState ->
            currentState.copy(scannerName = value)
        }
    }


    private fun saveSettings() {
        viewModelScope.launch {
            saveSettingsFlow().collect { result ->
                if (result) {
                    emitSuccess(UIResources.settings_saving_succeeded)
                }
            }
        }
    }


    private fun saveSettingsFlow(): Flow<Boolean> {
        return flow {
            try {
                val currentState = settingsState.value

                for (property in currentState.toMap()) {
                    if (property.value.isNotEmpty())
                        configurationService.set(
                            key = property.key, value = property.value
                        )
                }
                emit(true)
            } catch (e: Exception) {
                emitError(e.localizedMessage)
            }
        }
    }


}