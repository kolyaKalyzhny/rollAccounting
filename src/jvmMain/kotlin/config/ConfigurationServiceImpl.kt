package config

import domain.interfaces.ConfigurationService

class ConfigurationServiceImpl(
    private val defaults: ConfigurationManager,
    private val overrides: ConfigurationManager
) : ConfigurationService {


    override suspend fun get(key: String): String? {
        return overrides.get(key) ?: defaults.get(key)
    }

    override suspend fun set(key: String, value: String) {
        overrides.set(key, value)
        overrides.save()
    }

}