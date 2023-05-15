package presentation.common

import androidx.compose.runtime.compositionLocalOf
import presentation.label.LabelViewModel

val LocalLabelViewModel = compositionLocalOf<LabelViewModel> {
    error("No LabelViewModel provided")
}