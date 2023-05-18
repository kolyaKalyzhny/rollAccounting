package presentation.label

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import config.UIResources
import koin

@Composable
fun LabelScreen(
    viewModel: LabelViewModel = koin.get()
) {
    val labelState by viewModel.labelState.collectAsState()
    val scaffoldState = rememberScaffoldState()

    val dialogState = remember {
        mutableStateOf(DialogState("", false))
    }

    val successSnackbarState = remember {
        mutableStateOf(false)
    }


    LaunchedEffect(Unit) {
        viewModel.success.collect {
            successSnackbarState.value = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.outputLabelPrompt.collect {
            dialogState.value = DialogState(content = it.value, isVisible = true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errors.collect {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    fun onDismissDialog() {
        dialogState.value = dialogState.value.copy(isVisible = false)
    }

    fun onDismissSnackbar() {
        successSnackbarState.value = false
    }


    Scaffold(
        scaffoldState = scaffoldState
    ) {
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


}

@Composable
fun AppTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        color = Color.DarkGray
    )
}

@Composable
fun ConnectionStatus(isConnected: Boolean) {
    val color = if (isConnected) Color.Green else Color.Red
    val statusText = UIResources.connection_status + " " +
            if (isConnected) UIResources.connection_on else UIResources.connection_off

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.Circle,
            contentDescription = "Connection Status",
            tint = color
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = statusText,
            style = MaterialTheme.typography.body1,
            color = Color.Gray
        )
    }
}


@Composable
fun Instructions(instructions: List<String>) {

    Column {
        Text(
            text = UIResources.instructions_label,
            style = MaterialTheme.typography.body1,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        instructions.forEach { instruction ->
            Text(
                text = "- $instruction",
                style = MaterialTheme.typography.body2,
                color = Color.LightGray
            )
        }
    }
}


@Composable
fun Statistics(processedAmount: Int) {
    val totalProcessed = UIResources.total_processed + processedAmount.toString()
    Text(
        text = totalProcessed,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface,
    )
}

