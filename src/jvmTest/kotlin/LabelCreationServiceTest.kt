import config.Config
import domain.interfaces.LabelCreationService
import domain.models.GTIN
import infra.LabelCreationServiceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LabelCreationServiceTest {

    private val labelCreationService: LabelCreationService = LabelCreationServiceImpl()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test create label`() = runTest {
        // Arrange
        val gtin = "1234567890123"
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Config.datePattern))
        val expectedLabel = "01${gtin}21$dateTime"


        // Act
        val actualLabelResult = labelCreationService.generateGS1128LabelV1(GTIN(gtin), format = Config.gs1128Format)
        val actualLabel = actualLabelResult.getOrElse { throw it }
        println(actualLabel)


        // Assert
        assertNotNull(actualLabel.value)
        assertEquals(expectedLabel, actualLabel.value)

    }
}