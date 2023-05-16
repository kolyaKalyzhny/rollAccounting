package domain.interfaces

import domain.models.Barcode
import domain.models.GS1128Label
import kotlinx.coroutines.flow.Flow
import utils.Resource

interface BarcodeRepository {
    suspend fun scanBarcode(): Result<Barcode>
    suspend fun sendLabelToServer(label: GS1128Label): Flow<Resource<Unit>>
//            Result<Unit>

    //    suspend fun listenToBarcodeOutput(): Flow<Result<Barcode>>
    suspend fun listenToBarcodeOutput(): Flow<Resource<Barcode>>
//    fun emitScannerStatus(): Flow<Result<Unit>>
    fun emitScannerStatus(): Flow<Boolean>

    fun mockConnection(): Flow<Resource<Unit>>
}

