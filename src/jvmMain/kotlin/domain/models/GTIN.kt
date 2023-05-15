package domain.models

import domain.exceptions.ErrorMessages
import domain.exceptions.GTINValidationException

data class GTIN(
    val value: String
) {
    init {
        require(value.isNotEmpty()) { throw GTINValidationException(ErrorMessages.GTIN_EMPTY) }
//        TODO: validation
    }
}


