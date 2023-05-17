package presentation

import domain.interfaces.BarcodeRepository
import domain.interfaces.TestRepository
import domain.usecase.OutputProductLabel
import domain.usecase.ProcessBarcodeV1

class TestViewModel(
    private val barcodeRepository: BarcodeRepository,
    private val outputProductLabel: OutputProductLabel,
    private val processBarcode: ProcessBarcodeV1
) {
}