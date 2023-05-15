package infra

import domain.interfaces.CustomLogger
import org.tinylog.Logger

class FileLogger : CustomLogger {
    override fun info(message: String) {
        Logger.info(message, *emptyArray<Any>())
    }

    override fun warn(message: String) {
        Logger.warn(message, *emptyArray<Any>())
    }

    override fun debug(message: String) {
        Logger.debug(message, *emptyArray<Any>())
    }

    override fun error(message: String) {
        Logger.error(message, *emptyArray<Any>())
    }

}