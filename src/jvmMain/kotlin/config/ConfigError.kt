package config

sealed class ConfigError(message: String, cause: Throwable): Exception(message, cause) {
    class LoadError(cause: Exception) : ConfigError("Failed to load configuration", cause)
    class SaveError(cause: Exception) : ConfigError("Failed to save configuration", cause)
}