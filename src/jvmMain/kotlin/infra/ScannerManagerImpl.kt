package infra

import config.Messages
import domain.exceptions.ScannerServiceException
import domain.interfaces.CustomLogger
import domain.interfaces.scanner.ScannerManager
import domain.interfaces.scanner.ScannerService
import domain.models.Barcode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import utils.Resource
import utils.RetryPolicy
import utils.withRetry

class ScannerManagerImpl(
    private val coroutineScope: CoroutineScope,
    private val scannerService: ScannerService,
    private val logger: CustomLogger,
    private val maxConnectionRetries: Int = 3,
    private val monitoringIntervalMillis: Long = 5000L,
) : ScannerManager {

    private val _connectionStatusFlow = MutableSharedFlow<Boolean>(replay = 1)

    override val connectionStatusFlow: SharedFlow<Boolean> = _connectionStatusFlow.asSharedFlow()


    init {
        coroutineScope.launch {
            connectToScanner()
            startMonitoring(monitoringIntervalMillis)
        }
    }


    private var connectionCheckJob: Job? = null

    override fun startMonitoring(intervalMillis: Long) {
        connectionCheckJob?.cancel()
        connectionCheckJob = coroutineScope.launch {
            while (true) {
                val isConnected = scannerService.isConnected()
                _connectionStatusFlow.emit(isConnected)

                if (!isConnected) {
                    connectToScanner()
                }

                delay(intervalMillis)
            }
        }

    }

    override fun stopMonitoring() {
        connectionCheckJob?.cancel()
        connectionCheckJob = null
    }

    override suspend fun connectToScanner() {
        logger.info(Messages.getMessage("logging_establishing_connection"))
        var retries = 0

        while (retries < maxConnectionRetries) {
            val result = scannerService.connect()
            if (result.isSuccess) {
                return
            }
            retries++
            delay(5000)
        }

        logger.warn(Messages.getMessage("logging_connection_failed"))
    }

    override suspend fun getBarcodeFlow(retryPolicy: RetryPolicy): Flow<Resource<Barcode>> = flow {
        emit(Resource.Loading(true))
        val conn = withRetry(retryPolicy) { scannerService.connect() }
        emit(Resource.Loading(false))

        if (conn.isSuccess) {
            emitAll(scannerService.emitBarcode())
        } else {
            emit(Resource.Error("Unable to connect to scanner service"))
//            throw ScannerServiceException("something went wrong")
        }

    }


}