package presentation.label

import domain.interfaces.BarcodeRepository
import domain.models.GS1128Label
import domain.usecase.OutputProductLabel
import domain.usecase.ProcessBarcodeV1
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.BaseViewModel
import utils.Resource

class LabelViewModel(
    private val processBarcodeV1: ProcessBarcodeV1,
    private val barcodeRepository: BarcodeRepository,
    private val outputProductLabel: OutputProductLabel
) : BaseViewModel(onError = {}) {

    private val _labelState = MutableStateFlow(LabelState())
    val labelState = _labelState.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    val errors = _errors.asSharedFlow()

    private val _outputLabelPrompt = MutableSharedFlow<List<GS1128Label>>()
    val outputLabelPrompt = _outputLabelPrompt.asSharedFlow()


    private val _labels = MutableStateFlow<List<GS1128Label>>(emptyList())
    val labels = _labels.asStateFlow()

    init {
//        handleProcessedBarcode()
//        handleScannerStatus()
//        testScanner()

    }

    private fun handleProcessedBarcode() {
        viewModelScope.launch {
            processBarcodeV1().collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        _errors.emit(result.message ?: "unexpected error occurred")
                    }

                    is Resource.Success -> {
                        if (result.data == null) return@collect

                        _outputLabelPrompt.emit(labelState.value.labels + result.data)
                        _labelState.update { currentState ->
                            currentState.copy(labels = currentState.labels + result.data)
                        }
                    }
                }
            }
        }
    }

    private fun handleScannerStatus() {
        viewModelScope.launch {
            barcodeRepository.emitScannerStatus().collect { result ->
                _labelState.update { currentState ->
                    if (currentState.isScannerConnected != result) {
                        currentState.copy(isScannerConnected = result)
                    } else currentState
                }
            }
        }
    }

    private fun confirmPrinting() {
        viewModelScope.launch {
            if (labels.value.isEmpty()) return@launch

            outputProductLabel(labels.value.first())
            _labels.update { list ->
                list.drop(1)
            }
        }
    }


    fun showSnackbars() {
        viewModelScope.launch {
            val myFlow = flow<Unit> {
                for (i in 0..10) {
                    _errors.emit("number is $i")
                }
            }
            myFlow.collect()
        }
    }


    private fun testScanner() {
        viewModelScope.launch {
            barcodeRepository.listenToBarcodeOutput().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        println(result.message ?: "no error message has been delivered")
                        _errors.emit(result.message ?: "unexpected error occurred")
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