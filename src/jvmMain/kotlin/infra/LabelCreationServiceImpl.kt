package infra

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import config.Config
import domain.interfaces.LabelCreationService
import domain.models.*
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.imageio.ImageIO

class LabelCreationServiceImpl : LabelCreationService {
    override suspend fun extractGTIN(barcode: Barcode): Result<GTIN> {
        return try {
            val gtin = barcode.value.substring(3, 15)
            Result.success(GTIN(gtin))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun generateGS1128Label(
        gtin: GTIN,
        dateTime: LocalDateTime,
        format: String
    ): Result<BarcodeImage> {
        return try {

            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"))
            val barcodeContent = "01${gtin}11$dateTime"

            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.MARGIN to 0
            )

            val multiFormatWriter = MultiFormatWriter()
            val bitMatrix = multiFormatWriter.encode(barcodeContent, BarcodeFormat.CODE_128, 400, 100, hints)
            val bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)

            val barcodeImage = bufferedImageToBarcodeImage(bufferedImage)
            Result.success(barcodeImage)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun generateGS1128LabelV1(gtin: GTIN, format: String): Result<GS1128Label> {
        return try {
            val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Config.datePattern))
            val barcodeContent = format
                .replace(oldValue = "{gtin}", gtin.value)
                .replace(oldValue = "{dateTime}", dateTime)

            val gs1128Label = GS1128Label(barcodeContent)

            Result.success(gs1128Label)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun bufferedImageToBarcodeImage(bufferedImage: BufferedImage): BarcodeImage {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream)
        val imageData = byteArrayOutputStream.toByteArray()
        return BarcodeImage(imageData, bufferedImage.width, bufferedImage.height)
    }
////        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
////        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyHHmm"))
////        [FNC1]01${GTIN}11${dateTime}
}

suspend fun main() {
    val lCS = LabelCreationServiceImpl()
    val gtin = "1234567890123"

    val labelResult = lCS.generateGS1128LabelV1(gtin = GTIN(gtin), format = Config.gs1128Format)
    val label = labelResult.getOrElse { throw it }
    println(label.value)
}
