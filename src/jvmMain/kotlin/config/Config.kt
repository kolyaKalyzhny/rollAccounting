package config

import java.io.FileOutputStream
import java.util.Properties

object Config {
    private val properties: Properties = Properties()

    const val DATE_PATTERN_KEY = "date_pattern"
    const val LABEL_PATTERN_KEY = "GS1128_format"
    const val PRINTER_IP_KEY = "printer_ip"
    const val PRINTER_PORT_KEY = "printer_port"
    const val SCANNER_NAME_KEY = "scanner_port_descriptor"
    const val BACKEND_URL_KEY = "backend_url"

    init {
        val inputStream = javaClass.classLoader.getResourceAsStream("config.properties")
        properties.load(inputStream)
    }


    fun updateConfigProperties() {

    }


    val barcodeLength: Int
        get() = properties.getProperty("barcode_length").toInt()
    val gs1128Format: String
        get() = properties.getProperty("GS1128_format").toString()
    var datePattern: String
        get() = properties.getProperty("date_pattern").toString()
        set(value) {
            properties.setProperty("date_pattern", value)
            saveProps()
        }
    val portCheckhealth: Long
        get() = properties.getProperty("port_checkhealth").toLong()
    val printerIp: String
        get() = properties.getProperty("printer_ip")
    val printerPort: Int
        get() = properties.getProperty("printer_port").toInt()
    val backendUrl: String
        get() = properties.getProperty("backend_url")
    val scannerPortDescriptor
        get() = properties.getProperty("scanner_port_descriptor")



    private fun saveProps() {
        val outputStream = FileOutputStream("config.properties")
        properties.store(outputStream, null)
        outputStream.close()
    }
}