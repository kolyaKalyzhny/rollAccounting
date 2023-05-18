package presentation.label

import domain.models.GS1128Label

data class LabelState(
    val labels: List<GS1128Label> = emptyList(),
    val isScannerConnected: Boolean = false,
    val scans: List<String> = emptyList(),
    val processedAmount: Int = 0
)
