package domain.usecase

import config.Config
import domain.interfaces.BarcodeRepository
import domain.interfaces.LabelCreationService
import domain.models.GS1128Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import utils.Resource

class ProcessBarcodeV1(
    private val barcodeRepository: BarcodeRepository,
    private val labelCreationService: LabelCreationService
) {

    //    suspend operator fun invoke(): Flow<Result<GS1128Label>> {
    suspend operator fun invoke(): Flow<Resource<GS1128Label>> {
        return barcodeRepository.listenToBarcodeOutput().map { result ->

            if (result is Resource.Error) {
                return@map Resource.Error(result.message ?: "unexpected error")
            }
            if (result is Resource.Loading) {
                return@map Resource.Loading(result.isLoading)
            }

            val barcodeData = result.data ?: throw Exception()

            val gtinResult = labelCreationService.extractGTIN(barcodeData)
            val gtin = gtinResult.getOrElse {
                return@map Resource.Error("something went wrong")
            }

            val gs1128LabelResult = labelCreationService.generateGS1128LabelV1(
                gtin = gtin,
                format = Config.gs1128Format
            )
            val gs1128Label = gs1128LabelResult.getOrElse {
                return@map Resource.Error("something went wrong")
            }
            Resource.Success(gs1128Label)
        }
            .catch { e: Throwable ->
                emit(Resource.Error(e.localizedMessage))
            }
    }
}