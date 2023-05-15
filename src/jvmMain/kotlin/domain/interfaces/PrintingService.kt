package domain.interfaces

import domain.models.GS1128Label

interface PrintingService {
    suspend fun printLabel(label: GS1128Label): Result<Unit>
}