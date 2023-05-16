import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.koin.core.Koin
import presentation.label.LabelScreen

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

//    MaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }

    MaterialTheme {
        LabelScreen()
    }
}

lateinit var koin: Koin

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun main() = application {
    koin = initKoin().koin
    Dispatchers.setMain(newSingleThreadContext("UI Thread"))

    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
