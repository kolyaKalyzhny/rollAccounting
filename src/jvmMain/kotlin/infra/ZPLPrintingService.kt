package infra

import config.Config
import data.interfaces.NetworkService
import data.remote.TcpNetworkService
import domain.interfaces.PrintingService
import domain.interfaces.printer.ZPLCommandCreator
import domain.models.GS1128Label
import kotlinx.coroutines.flow.Flow

class ZPLPrintingService(
    private val networkService: NetworkService,
    private val zplCommandCreator: ZPLCommandCreator
) : PrintingService {
    override suspend fun printLabel(label: GS1128Label): Result<Unit> {
        return try {
            val zplCommand = createZPLCommand(label)
            val resultFlow = sendZPLCommand(zplCommand)
            return collectResult(resultFlow)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun createZPLCommand(label: GS1128Label): String {
        return zplCommandCreator.createZPLCommand(data = label.value)
    }

    private fun sendZPLCommand(zplCommand: String): Flow<Result<Unit>> {
        return networkService.send(ip = Config.printerIp, port = Config.printerPort, data = zplCommand)
    }

    private suspend fun collectResult(resultFlow: Flow<Result<Unit>>): Result<Unit> {
        var result: Result<Unit> = Result.failure(Exception("no response from the printer"))
        resultFlow.collect { result = it }
        return result
    }
}