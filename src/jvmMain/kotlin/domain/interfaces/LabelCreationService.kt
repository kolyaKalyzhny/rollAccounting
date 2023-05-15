package domain.interfaces

import domain.models.*
import java.time.LocalDateTime

interface LabelCreationService {
    suspend fun extractGTIN(barcode: Barcode): Result<GTIN>
    suspend fun generateGS1128Label(gtin: GTIN, dateTime: LocalDateTime, format: String): Result<BarcodeImage>
    suspend fun generateGS1128LabelV1(gtin: GTIN, format: String): Result<GS1128Label>
}