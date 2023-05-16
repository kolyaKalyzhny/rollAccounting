package presentation.label

import domain.interfaces.BarcodeRepository
import domain.models.GS1128Label
import domain.usecase.OutputProductLabel
import domain.usecase.ProcessBarcodeV1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import utils.Resource

class LabelViewModel(
    private val processBarcodeV1: ProcessBarcodeV1,
    private val barcodeRepository: BarcodeRepository,
    private val outputProductLabel: OutputProductLabel
) {

    private val _labelState = MutableStateFlow(LabelState())
    val labelState = _labelState.asStateFlow()

    private lateinit var coroutineScope: CoroutineScope
    private val _labels = MutableStateFlow<List<GS1128Label>>(emptyList())
    val labels = _labels.asStateFlow()

    init {
//        handleProcessedBarcode()
//        handleScannerStatus()
//        testScanner()


//        TODO(
//            "collect scannerManager's connectionFlow " +
//                    "and update to UI accordingly"
//        )
    }

    private fun handleProcessedBarcode() {
        coroutineScope.launch {
            processBarcodeV1().collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        if (result.data == null) return@collect

                        _labelState.update { currentState ->
                            currentState.copy(labels = currentState.labels + result.data)
                        }
                    }
                }
            }
        }
    }

    private fun handleScannerStatus() {
        coroutineScope.launch {
            barcodeRepository.emitScannerStatus().collect { result ->
                val isConnected = result.isSuccess
                _labelState.update { currentState ->
                    if (currentState.isScannerConnected != isConnected) {
                        currentState.copy(isScannerConnected = isConnected)
                    } else currentState
                }
            }
        }
    }

    private fun confirmPrinting() {
        coroutineScope.launch {
            if (labels.value.isEmpty()) return@launch

            outputProductLabel(labels.value.first())
            _labels.update { list ->
                list.drop(1)
            }
        }
    }


    private fun testScanner() {
        coroutineScope.launch {
            barcodeRepository.listenToBarcodeOutput().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        println(result.message ?: "no error message has been delivered")
                    }

                    is Resource.Loading -> {
                        println("A loading event has just been received")
                    }

                    is Resource.Success -> {
                        println("Here's a received barcode: ${result.data ?: "nothing scanned?"}")

                        result.data.let { barcode ->
                            _labelState.update { labelState ->
                                labelState.copy(scans = labelState.scans.plus(barcode!!.value))
                            }
                        }
                    }
                }

            }
        }
    }
}