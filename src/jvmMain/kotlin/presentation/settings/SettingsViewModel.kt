package presentation.settings

import config.Config
import domain.interfaces.ConfigurationService
import presentation.BaseViewModel

class SettingsViewModel(
    private val configurationService: ConfigurationService
) : BaseViewModel(onError = {}) {

    fun updateConfig() {
        val number = (0..10).random()
        Config.datePattern = number.toString()
    }

    fun getFresh() {
        val pattern = Config.datePattern
        println(pattern)
    }

}