package domain.models

import config.Config
import config.Messages
import domain.BarcodeException

data class Barcode(
    val value: String
) {
    init {
        require(value.length == Config.barcodeLength) { BarcodeException("barcode_length_error") }
        //TODO: check whether the value contains GS
    }
}
