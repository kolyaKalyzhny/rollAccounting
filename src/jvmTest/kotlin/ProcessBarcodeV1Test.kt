import config.Config
import data.MockBarcodeRepository
import domain.interfaces.BarcodeRepository
import domain.interfaces.LabelCreationService
import domain.models.Barcode
import domain.usecase.ProcessBarcodeV1
import infra.LabelCreationServiceImpl
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import utils.Resource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProcessBarcodeV1Test {
    private lateinit var barcodeRepository: BarcodeRepository
    private lateinit var labelCreationService: LabelCreationService
    private lateinit var processBarcode: ProcessBarcodeV1

    @BeforeTest
    fun setUp() {
        barcodeRepository = MockBarcodeRepository()
//        labelCreationService = mockk()
        labelCreationService = LabelCreationServiceImpl()
        processBarcode = ProcessBarcodeV1(barcodeRepository, labelCreationService)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should emit GS1128Label Resource Success based on input Barcode`() = runTest {
        // Arrange
        val barcode = barcodeRepository.listenToBarcodeOutput().first().data!!
        val expectedGtinResult = labelCreationService.extractGTIN(Barcode("1234567890123456789012345678901"))
        val expectedGtin = expectedGtinResult.getOrNull()!!
        val expectedLabelResult =
            labelCreationService.generateGS1128LabelV1(expectedGtin, format = Config.gs1128Format).getOrNull()!!

        // Act
        val result = processBarcode().first()

        println(result.data)
        println(expectedLabelResult)

        // Assert
        assertNotNull(result.data)
        assertEquals(Resource.Success(expectedLabelResult).data, result.data)
    }


}
