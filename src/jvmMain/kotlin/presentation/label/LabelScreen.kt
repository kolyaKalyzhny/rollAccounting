package presentation.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import koin

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = koin.get()
) {

//    val labelViewModel = LocalLabelViewModel.current
//    val scans by labelViewModel.labelState.collectAsState()
    val scans by viewModel.labelState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        viewModel.errors.collect {
            println(it)
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }


    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Сканер ${if (scans.isScannerConnected) "подключен" else "отключен"}")
            TextButton(onClick = { viewModel.showSnackbars() }) {
                Text(text = "Snackbars")
            }
            Text("Label Screen", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            LazyColumn {
                items(scans.scans) {
                    Text(text = it)
                }
            }
        }
    }

}