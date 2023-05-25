package presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import config.UIResources
import koin
import kotlinx.coroutines.flow.collect
import presentation.label.*
import presentation.settings.SettingsScreen
import presentation.settings.SettingsViewModel

@Composable
fun ParentContainer(
    // TODO(define viewmodels here)
    labelViewModel: LabelViewModel = koin.get(),
    settingsViewModel: SettingsViewModel = koin.get()
) {
    val scaffoldState = rememberScaffoldState()

    var showSettings by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(labelViewModel) {
        labelViewModel.baseErrors.collect {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }


    LaunchedEffect(Unit) {
        settingsViewModel.baseSuccesses.collect {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    val btnText = if (showSettings) "tick" else "tack"

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Row {
                IconButton(onClick = { showSettings = !showSettings }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = UIResources.settings
                    )
                }

//                Button(onClick = { showSettings = !showSettings }) {
//                    Text(text = btnText)
//                }
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
//            LabelComponent(labelViewModel)
            LabelScreen(labelViewModel)
            if (showSettings) {
                BoxWithConstraints {
                    SettingsScreen(
                        settingsViewModel,
                        maxWidth = constraints.maxWidth,
                        maxHeight = constraints.maxHeight
                    )
                }

            }

        }

    }
}


@Composable
fun LabelComponent(viewModel: LabelViewModel) {

    val labelState by viewModel.labelState.collectAsState()

    val dialogState = remember {
        mutableStateOf(DialogState("", false))
    }

    val successSnackbarState = remember {
        mutableStateOf(false)
    }

//    LaunchedEffect(Unit) {
//        viewModel.success.collect {
//            successSnackbarState.value = true
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.outputLabelPrompt.collect {
//            dialogState.value = DialogState(content = it.value, isVisible = true)
//        }
//    }

//    LaunchedEffect(Unit) {
//        viewModel.errors.collect {
//            scaffoldState.snackbarHostState.showSnackbar(it)
//        }
//    }

    fun onDismissDialog() {
        dialogState.value = dialogState.value.copy(isVisible = false)
    }

    fun onDismissSnackbar() {
        successSnackbarState.value = false
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (dialogState.value.isVisible) {
            ConfirmationDialog(
                onDismiss = { onDismissDialog() },
                title = UIResources.confirm_printing,
                content = dialogState.value.content
            )
        }

        AutoDismissSnackbar(showSnackBar = successSnackbarState.value, onDismiss = { onDismissSnackbar() })

        TextButton(onClick = { viewModel.emitSuccessEvent() }) {
            Text(text = "success event")
        }


        TextButton(onClick = { viewModel.printLabel() }) {
            Text(text = "Print Label")
        }

        Column {
            AppTitle(UIResources.app_title)
            Spacer(Modifier.height(8.dp))
            ConnectionStatus(labelState.isScannerConnected)
            Spacer(Modifier.height(8.dp))
            Instructions(
                UIResources.instruction_set

            )
            Spacer(Modifier.height(8.dp))
            Statistics(labelState.processedAmount)
        }

    }
}