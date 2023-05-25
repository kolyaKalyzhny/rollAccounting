package config

sealed class ConfigError(key: String, cause: Throwable? = null) : Exception(key, cause) {
    class LoadError(cause: Throwable? = null) :
        ConfigError("Failed to load configuration file")

    class FetchError(key: String, cause: Throwable? = null) :
        ConfigError("Failed to fetch settings for key: $key", cause)

    class SaveError(cause: Throwable? = null) : ConfigError("Failed to save configuration", cause)
    class ClearError(cause: Throwable? = null) :
        ConfigError("Failed to save empty custom configuration when setting default values", cause)
}