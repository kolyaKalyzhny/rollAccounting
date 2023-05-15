package test

import domain.interfaces.CustomLogger
import domain.interfaces.scanner.ScannerManager
import domain.interfaces.scanner.ScannerService
import domain.models.Barcode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import utils.Resource
import utils.RetryPolicy

interface MyService {
    fun performSomeTask()
}

class MyServiceImpl : MyService {
    override fun performSomeTask() {
        println("some heavy operation is in the process")
    }
}

class LoggingMyService(private val myService: MyService) : MyService {
    override fun performSomeTask() {
        println("Entering performSomeTask()")
        myService.performSomeTask()
        println("Exiting performSomeTask()")
    }
}


class LoggingScannerManager(
    private val scannerManager: ScannerManager,
    private val coroutineScope: CoroutineScope,
    private val scannerService: ScannerService,
    private val logger: CustomLogger,
) : ScannerManager {

    override val connectionStatusFlow: SharedFlow<Boolean> = scannerManager.connectionStatusFlow

    init {
        // keep the init block or invoke startMonitoring manually
    }

    override fun startMonitoring(intervalMillis: Long) {
        scannerManager.startMonitoring(intervalMillis)
    }

    override fun stopMonitoring() {
        scannerManager.stopMonitoring()
    }

    override suspend fun connectToScanner() {
        TODO("Not yet implemented")
    }

    override suspend fun getBarcodeFlow(retryPolicy: RetryPolicy): Flow<Resource<Barcode>> {
        TODO("Not yet implemented")
    }

}

fun main() {
    val myService = MyServiceImpl()
    val loggingMyService = LoggingMyService(myService)
    loggingMyService.performSomeTask()
}


class ScannerServiceImpl : ScannerService {
    override fun isConnected(): Boolean {
        TODO("Not yet implemented")
    }

    override fun connect(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun disconnect(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun readBarcode(): Result<Barcode> {
        TODO("Not yet implemented")
    }

    override fun emitBarcode(): Flow<Resource<Barcode>> {
        TODO("Not yet implemented")
    }

    override fun monitorConnection(checkIntervalMillis: Long): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }
}

interface ScannerManagerV1 {
    suspend fun connectionWithRetries(): Result<Unit>
}

//interface Retryable {
//    suspend fun <T> withRetries(maxAttempts: Int, intervalMillis: Long, block: suspend () -> T)
//}


