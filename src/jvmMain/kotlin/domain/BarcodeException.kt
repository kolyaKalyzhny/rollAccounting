package domain

import config.Messages

class BarcodeException(messageKey: String, vararg args: Any) :
    IllegalArgumentException(Messages.getMessage(messageKey, *args))

