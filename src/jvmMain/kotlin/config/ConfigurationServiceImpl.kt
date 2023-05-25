package config

import domain.interfaces.ConfigurationService

class ConfigurationServiceImpl(
    private val defaults: ConfigurationManager,
    private val overrides: ConfigurationManager
) : ConfigurationService {

    override suspend fun get(key: String): String {
        val overridden = overrides.get(key)
        val default = defaults.get(key)

        if (!overridden.isNullOrEmpty()) return overridden

        if (!default.isNullOrEmpty()) return default

        throw ConfigError.FetchError(key)
    }

    override suspend fun set(key: String, value: String) {
        if (defaults.get(key) == value) return

        overrides.set(key, value)
        overrides.save()
    }

    override suspend fun setDefaultsKey(key: String) {
        try {
            overrides.clearKey(key)
            overrides.save()
        } catch (e: Exception) {
            throw ConfigError.ClearError(e)
        }

    }

    override suspend fun setDefaultsAll() {
        try {
            overrides.clearAll()
            overrides.save()
        } catch (e: Exception) {
            throw ConfigError.ClearError(e)
        }
    }

}