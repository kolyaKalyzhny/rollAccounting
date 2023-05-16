package data

import domain.interfaces.BarcodeRepository
import domain.models.Barcode
import domain.models.GS1128Label
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import utils.Resource
import kotlin.random.Random

class MockBarcodeRepository : BarcodeRepository {
    private val barcodes = listOf(
        Barcode("1234567890123456789012345678901"),
        Barcode("2345678901234567890123456789012"),
        Barcode("3456789012345678901234567890123"),
        Barcode("4567890123456789012345678901234"),
        Barcode("5678901234567890123456789012345")
    )

    private val barcodeV1 = List(5) { Barcode(generateRandomBarcode()) }

    override suspend fun scanBarcode(): Result<Barcode> {
        TODO("Not yet implemented")
    }

    override suspend fun sendLabelToServer(label: GS1128Label): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun listenToBarcodeOutput(): Flow<Resource<Barcode>> = flow {
//        barcodeV1.forEach {
        barcodes.forEach {
            emit(Resource.Success(it))
            delay(1000)
        }
    }


    private fun generateRandomBarcode(): String {
        return (1..31).map { Random.nextInt(0, 10) }.joinToString("")
    }

    override fun emitScannerStatus(): Flow<Result<Unit>> = flow {
        TODO()
    }

    override fun mockConnection(): Flow<Resource<Unit>> {
        TODO("Not yet implemented")
    }

}