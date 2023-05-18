package config

import java.util.Properties

object Messages {
    private val properties: Properties = Properties()

    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("messages.properties")
        properties.load(inputStream)
    }


    fun getMessage(key: String, vararg args: Any): String {
        val message = properties.getProperty(key) ?: throw IllegalArgumentException("No message found for key: $key")
        return String.format(message, *args)
    }
//    val GTIN_EMPTY: String
//        get() = properties.getProperty()

    //        "GTIN value cannot be empty"
    val GTIN_LENGTH = "GTIN value must be exactly 14 characters long"
}