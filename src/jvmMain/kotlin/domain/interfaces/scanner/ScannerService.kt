package domain.interfaces.scanner

import domain.models.Barcode
import kotlinx.coroutines.flow.Flow
import utils.Resource

interface ScannerService {
    fun isConnected(): Boolean
    fun connect(): Result<Unit>
    fun disconnect(): Result<Unit>
    fun readBarcode(): Result<Barcode>
//    fun emitBarcode(): Flow<Result<Barcode>>
    fun emitBarcode(): Flow<Resource<Barcode>>
    fun monitorConnection(checkIntervalMillis: Long): Flow<Result<Unit>>
}