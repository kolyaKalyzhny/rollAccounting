package domain.interfaces

interface ConfigurationService {
//    suspend fun get(key: String): String?
    suspend fun get(key: String): String
    suspend fun set(key: String, value: String)
    suspend fun setDefaultsKey(key: String)

    suspend fun setDefaultsAll()
}