package domain.interfaces.scanner

import domain.models.Barcode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import utils.Resource
import utils.RetryPolicy

interface ScannerManager {
    val connectionStatusFlow: SharedFlow<Boolean>
    fun startMonitoring(intervalMillis: Long)
    fun stopMonitoring()
    suspend fun connectToScanner()
//    suspend fun getBarcodeFlow(retryPolicy: RetryPolicy): Flow<Result<Barcode>>
    suspend fun getBarcodeFlow(retryPolicy: RetryPolicy): Flow<Resource<Barcode>>
}