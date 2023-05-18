package domain.interfaces

interface ConfigurationService {
    suspend fun get(key: String): String?
    suspend fun set(key: String, value: String)
}