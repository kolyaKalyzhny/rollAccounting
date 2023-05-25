package config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Properties

class FileConfigurationManager : ConfigurationManager {
    private val properties = Properties()
    private val file = File("config_override.properties")


    init {
        if (file.exists()) {
            try {
                val inputStream = FileInputStream(file)
                properties.load(inputStream)
            } catch (e: Exception) {
                throw ConfigError.LoadError(e)
            }
        }
    }


    override suspend fun get(key: String): String? = withContext(Dispatchers.IO) {
        properties.getProperty(key)

//        val message = properties.getProperty(key) ?: throw IllegalArgumentException("No message found for key: $key")
//        String.format(message)
    }

    override suspend fun set(key: String, value: String): Unit = withContext(Dispatchers.IO) {
        properties.setProperty(key, value)
    }

    override suspend fun save() = withContext(Dispatchers.IO) {
        try {
            val outputSteam = FileOutputStream(file)
            properties.store(outputSteam, null)
        } catch (e: IOException) {
            throw ConfigError.SaveError(e)
        }
    }

    override suspend fun clearKey(key: String) {
        properties.remove(key)
    }

    override suspend fun clearAll() {
        properties.clear()
    }
}