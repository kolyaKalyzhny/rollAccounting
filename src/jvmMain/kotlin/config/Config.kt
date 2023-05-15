package config

import java.util.Properties

object Config {
    private val properties: Properties = Properties()

    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
        properties.load(inputStream)
    }

    val barcodeLength: Int
        get() = properties.getProperty("barcode_length").toInt()
    val gs1128Format: String
        get() = properties.getProperty("GS1128_format").toString()
    val datePattern: String
        get() = properties.getProperty("date_pattern").toString()
    val portCheckhealth: Long
        get() = properties.getProperty("port_checkhealth").toLong()
    val printerIp: String
        get() = properties.getProperty("printer_ip")
    val printerPort: Int
        get() = properties.getProperty("printer_port").toInt()
    val backendUrl: String
        get() = properties.getProperty("backend_url")
}