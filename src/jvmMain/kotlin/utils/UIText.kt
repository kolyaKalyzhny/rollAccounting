package utils

import androidx.compose.runtime.Composable
import config.UIMessages


sealed class UIText {
    data class DynamicString(val value: String) : UIText()
    data class StringKey(val key: String) : UIText()


    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringKey -> UIMessages.getMessage(key)
        }
    }

    fun asPlainString(): String {
        return when (this) {
            is DynamicString -> value
            is StringKey -> UIMessages.getMessage(key)
        }
    }

}