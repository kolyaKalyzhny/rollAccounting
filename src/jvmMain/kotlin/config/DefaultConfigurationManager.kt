package config

import java.util.Properties

class DefaultConfigurationManager : ConfigurationManager {
    private val properties = Properties()


    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
        properties.load(inputStream)
    }

    override suspend fun get(key: String): String? {


        val message = properties.getProperty(key) ?: throw IllegalArgumentException("No message found for key: $key")
        return String.format(message)

//        return properties.getProperty(key)
    }


    override suspend fun set(key: String, value: String) {
        throw UnsupportedOperationException("cannot modify default configurations")
    }

    override suspend fun save() {
        throw UnsupportedOperationException("cannot save default configurations")
    }

    override suspend fun clearKey(key: String) {
        throw UnsupportedOperationException("cannot clearKey default configurations")
    }

    override suspend fun clearAll() {
        throw UnsupportedOperationException("cannot clearAll default configurations")
    }
}