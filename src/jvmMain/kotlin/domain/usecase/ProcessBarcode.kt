package domain.usecase

import config.Config
import domain.BarcodeException
import domain.interfaces.BarcodeRepository
import domain.interfaces.LabelCreationService
import domain.models.GS1128Label

class ProcessBarcode(
    private val barcodeRepository: BarcodeRepository,
    private val labelCreationService: LabelCreationService,
) {
    suspend operator fun invoke(): Result<GS1128Label> {
        return try {
            val barcodeDataResult = barcodeRepository.scanBarcode()
            val barcodeData = barcodeDataResult.getOrElse { throw it }

            val gtinResult = labelCreationService.extractGTIN(barcodeData)
            val gtin = gtinResult.getOrElse { throw it }

            val gs1128LabelResult = labelCreationService.generateGS1128LabelV1(
                gtin = gtin,
                format = Config.gs1128Format
            )
            val gs1128Label = gs1128LabelResult.getOrElse { throw it }
            gs1128LabelResult
        } catch (exception: BarcodeException) {
            //TODO: handle the barcode validation exceptions
            Result.failure(exception)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}