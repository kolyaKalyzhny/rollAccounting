package presentation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel(
    private val onError: (Throwable) -> Unit
) {

    private val _baseErrors = MutableSharedFlow<String>()
    val baseErrors = _baseErrors.asSharedFlow()

    private val _baseSuccesses = MutableSharedFlow<String>()
    val baseSuccesses = _baseSuccesses.asSharedFlow()

    protected fun emitError(error: String) {
        viewModelScope.launch {
            _baseErrors.emit(error)
        }
    }

    protected fun emitSuccess(success: String) {
        viewModelScope.launch {
            _baseSuccesses.emit(success)
        }
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable -> onError(throwable) }

    private val context = Dispatchers.Main + SupervisorJob() + exceptionHandler

    protected val viewModelScope = CoroutineScope(context)

    fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
    }
}