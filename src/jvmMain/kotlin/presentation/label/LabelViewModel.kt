package presentation.label

import domain.interfaces.BarcodeRepository
import domain.interfaces.PrintingService
import domain.models.GS1128Label
import domain.models.GTIN
import domain.usecase.OutputProductLabel
import domain.usecase.ProcessBarcodeV1
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import presentation.BaseViewModel
import utils.Resource

// So far:
// 1) Monitor Scanner's connection
// 2) When it is connected, cancel the previous job (if any) and start `ProcessBarcode()`
// 3) ProcessBarcode:
//    a. Result.Error -> emit an error event --> display a snackbar
//    b. Result.Success -> emit a prompt event --> display a confirmation dialog
// 4) Set onClickListener when hitting the "Confirm" button of the dialog
// 5) The confirmation triggers execution of the `OutputProductLabel()`
// 6)

class LabelViewModel(
    private val processBarcodeV1: ProcessBarcodeV1,
    private val barcodeRepository: BarcodeRepository,
    private val outputProductLabel: OutputProductLabel,
    private val printingService: PrintingService
) : BaseViewModel(onError = {}) {

    private val _labelState = MutableStateFlow(LabelState())
    val labelState = _labelState.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    val errors = _errors.asSharedFlow()


    private val _success = MutableSharedFlow<Boolean>()
    val success = _success.asSharedFlow()

    private val _outputLabelPrompt = MutableSharedFlow<GS1128Label>()
    val outputLabelPrompt = _outputLabelPrompt.asSharedFlow()


    private val _labels = MutableStateFlow<List<GS1128Label>>(emptyList())
    val labels = _labels.asStateFlow()


    private var processedBarcodeJob: Job? = null
    private var lastScannerState = false

    init {
        handleScannerStatus()
//        handleProcessedBarcode()
//        testScanner()

    }


    fun printLabel() {
        val gtin = "011234567890123211705231509"
        val label = GS1128Label(gtin)
        viewModelScope.launch {
            val result = printingService.printLabel(label)
            println(result)
        }
    }

    fun emitSuccessEvent() {
        viewModelScope.launch {
            _success.emit(true)
        }
    }


    private fun handleProcessedBarcode() {
        processedBarcodeJob = viewModelScope.launch {
            processBarcodeV1().collect { result ->
                when (result) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {
                        _errors.emit(result.message ?: "unexpected error occurred")
                    }

                    is Resource.Success -> {
                        if (result.data == null) return@collect

                        _outputLabelPrompt.emit(result.data)
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

                if (!lastScannerState && result) {
                    processedBarcodeJob?.cancel()
                    handleProcessedBarcode()
                }
                lastScannerState = result
            }
        }
    }

    private fun confirmPrinting() {
        viewModelScope.launch {
            if (labels.value.isEmpty()) return@launch

            val currentGtin = labelState.value.labels.first()
            _labelState.update { currentState -> currentState.copy(labels = currentState.labels.drop(1)) }

            outputProductLabel(currentGtin).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _errors.emit(result.message ?: "unexpected error occurred")
                    }

                    is Resource.Loading -> {
                        // Show a spinner
                    }

                    is Resource.Success -> {
                        // Show a success dialog
                        _labelState.update { currentState ->
                            currentState.copy(processedAmount = currentState.processedAmount + 1)
                        }
                        _success.emit(true)
                    }
                }
            }
        }
    }


    fun showSnackbars() {
        viewModelScope.launch {
            val myFlow = flow<Unit> {
                for (i in 0..10) {
                    _errors.emit("number is $i")
                    delay(5500)
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