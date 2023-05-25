package di

import com.google.gson.Gson
import config.*
import data.BarcodeRepositoryImpl
import data.TestRepositoryImpl
import data.interfaces.NetworkService
import data.interfaces.RestService
import data.remote.OkHttpRestService
import data.remote.TcpNetworkService
import domain.interfaces.*
import domain.interfaces.printer.ZPLCommandCreator
import domain.interfaces.scanner.ScannerManager
import domain.interfaces.scanner.ScannerService
import domain.usecase.OutputProductLabel
import domain.usecase.ProcessBarcodeV1
import infra.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import presentation.TestViewModel
import presentation.label.LabelViewModel
import presentation.settings.SettingsViewModel
import utils.DefaultRetryPolicy

//        single<LabelCreationService> { LabelCreationServiceImpl() }
//        single<TestRepository> { TestRepositoryImpl(get()) }
//        factory<TestViewModel> { TestViewModel(get()) }
object TestModules {
    private val configManagerDefault = named("Default")
    private val configManagerFile = named("Overrides")

    val appModule = module {

        single<JsonParser> { GSONParser(Gson()) }
        single<OkHttpClient> { OkHttpClient.Builder().build() }

        single<CoroutineScope> { CoroutineScope(Job() + Dispatchers.IO) }
////        single<CustomLogger> { FileLogger() }
//
        single<RestService> { OkHttpRestService(get(), get()) }
        single<ScannerService> { PioneerScannerService(Config.scannerPortDescriptor) }
        single<ScannerManager> { ScannerManagerImpl(get(), get()) }
//
        single<ZPLCommandCreator> { DefaultZPLCommandCreator() }
        single<NetworkService> { TcpNetworkService(DefaultRetryPolicy()) }
//
        single<BarcodeRepository> { BarcodeRepositoryImpl(get(), get(), get()) }
        single<PrintingService> {
            ZPLPrintingService(
                networkService = get(),
                zplCommandCreator = get()
            )
        }
//
        single<LabelCreationService> { LabelCreationServiceImpl() }
//
        single<OutputProductLabel> { OutputProductLabel(get(), get()) }
        single<ProcessBarcodeV1> { ProcessBarcodeV1(get(), get()) }

        single<ConfigurationManager>(configManagerDefault) { DefaultConfigurationManager() }
        single<ConfigurationManager>(configManagerFile) { FileConfigurationManager() }

        single<ConfigurationService> {
            ConfigurationServiceImpl(
                defaults = get(configManagerDefault),
                overrides = get(configManagerFile)
            )
        }
//
        single<LabelViewModel> {
            LabelViewModel(
                processBarcodeV1 = get(),
                outputProductLabel = get(),
                barcodeRepository = get(),
                printingService = get()
            )
        }
        single<SettingsViewModel> {
            SettingsViewModel(configurationService = get())
        }

        factory<TestViewModel> {
            TestViewModel(get(), get(), get())
        }
    }

}

fun initKoin(
    appModule: Module = TestModules.appModule,
): KoinApplication = startKoin {
    modules(appModule)
}
