package config

interface ConfigurationManager {
    suspend fun get(key: String): String?
    suspend fun set(key: String, value: String)
    suspend fun save()
}