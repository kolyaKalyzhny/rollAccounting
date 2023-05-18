package config

import java.util.*

object UIMessages {
    private val properties: Properties = Properties()

    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("uiMessages.properties")
        properties.load(inputStream)
    }


    fun getMessage(key: String): String {
        return properties.getProperty(key) ?: throw IllegalArgumentException("No message found for key: $key")
    }
}
