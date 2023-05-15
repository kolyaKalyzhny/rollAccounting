package infra

import domain.interfaces.scanner.ScannerManager
import domain.interfaces.scanner.ScannerService
import domain.models.Barcode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import utils.Resource
import utils.RetryPolicy

class ScannerManagerImplV1(
    private val coroutineScope: CoroutineScope,
    private val scannerService: ScannerService,
    private val maxRetries: Int = 3,
    private val intervalMillis: Long = 5000L
) : ScannerManager {

    private val _connectionStatusFlow = MutableSharedFlow<Boolean>(replay = 1)

    override val connectionStatusFlow: SharedFlow<Boolean> = _connectionStatusFlow.asSharedFlow()

    private var connectionCheckJob: Job? = null

    override fun startMonitoring(intervalMillis: Long) {
        connectionCheckJob?.cancel()
        connectionCheckJob = coroutineScope.launch {
            while (true) {
                val isConnected = scannerService.isConnected()

                if (!isConnected) {
                    connectToScanner()
                }

                _connectionStatusFlow.emit(isConnected)
                delay(intervalMillis)
            }
        }

    }

    override fun stopMonitoring() {
        connectionCheckJob?.cancel()
        connectionCheckJob = null
    }

    override suspend fun connectToScanner() {
        var retries = 0

        while (retries < maxRetries) {
            val result = scannerService.connect()
            if (result.isSuccess) {
                return
            }
            retries++
            delay(5000)
        }
    }

    override suspend fun getBarcodeFlow(retryPolicy: RetryPolicy): Flow<Resource<Barcode>> {
        TODO("Not yet implemented")
    }

}