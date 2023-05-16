package data

import config.Config
import data.interfaces.RestService
import domain.interfaces.BarcodeRepository
import domain.interfaces.scanner.ScannerManager
import domain.interfaces.scanner.ScannerService
import domain.models.Barcode
import domain.models.GS1128Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import utils.Resource
import utils.RetryPolicy
import utils.withRetry

class BarcodeRepositoryImpl(
    private val scannerService: ScannerService,
    private val scannerManager: ScannerManager,
    private val restService: RestService
) : BarcodeRepository {

//    init {
//        scannerService.connect()
//    }

    override suspend fun scanBarcode(): Result<Barcode> {
        return try {
            scannerService.readBarcode()
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun sendLabelToServer(label: GS1128Label): Flow<Resource<Unit>> {
        return restService.post(
            //TODO(this is a bad practice. Instead, use a common url, that you
            // declare when building a OkHttpClient inside your DI container
            // So that you could skip the invariable part.
            // e.g. https://localhost:8080
            // and simply pass the desired endpoint
            // e.g. /process-gtin)
            url = Config.backendUrl,
            body = label.value,
            headers = emptyMap(),
            responseType = Unit::class.java
        )
    }

    override suspend fun listenToBarcodeOutput(): Flow<Resource<Barcode>> {
        return scannerManager.getBarcodeFlow(retryPolicy = RetryPolicy())

    }

    override fun emitScannerStatus(): Flow<Result<Unit>> {
        return scannerService.monitorConnection(Config.portCheckhealth)
    }

    override fun mockConnection():
            Flow<Resource<Unit>> = flow {
        emit(Resource.Loading(true))

        val conn = withRetry(RetryPolicy(delayMillis = 3000L)) { scannerService.connect() }
        emit(Resource.Loading(false))

        if (conn.isSuccess) {
            emit(Resource.Success(Unit))
        } else {
            emit(Resource.Error("Unable to connect to scanner service"))
//            throw ScannerServiceException("something went wrong")
        }
    }


    fun close() {
        scannerService.disconnect()
    }
}