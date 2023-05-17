package presentation

import kotlinx.coroutines.*

abstract class BaseViewModel(
    private val onError: (Throwable) -> Unit
) {
    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onError(throwable) }

    private val context = Dispatchers.Main + SupervisorJob() + exceptionHandler

    protected val viewModelScope = CoroutineScope(context)

    fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}