package domain.usecase

import domain.interfaces.BarcodeRepository
import domain.interfaces.PrintingService
import domain.models.GS1128Label
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import utils.Resource

class OutputProductLabel(
    private val barcodeRepository: BarcodeRepository,
    private val printingService: PrintingService
) {
    suspend operator fun invoke(label: GS1128Label): Flow<Resource<Unit>> = flow {
        val printingResult = printingService.printLabel(label)
        printingResult.getOrElse {
            emit(Resource.Error(it.localizedMessage))
            return@flow
        }
        emitAll(barcodeRepository.sendLabelToServer(label))
    }
}