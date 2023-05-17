import di.TestModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DITestV1 : KoinTest {
    @Test
    fun testModules() {
        koinApplication {
            modules(
                TestModules.appModule
            )
        }.checkModules()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}