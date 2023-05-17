import di.Modules
import di.TestModules
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import kotlin.test.AfterTest
import kotlin.test.Test

class DITest: KoinTest {
    @Test
    fun testAllModules() {
        koinApplication {
            modules(
                Modules.viewModelsModule,
                Modules.repositoriesModule,
                Modules.networkModule,
                Modules.parserModule,
                Modules.labelCreationModule,
                Modules.printingModule,
                Modules.scannerModule,
                Modules.backgroundCoroutineScope,
                Modules.logger,
            )
        }.checkModules()
    }



    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}