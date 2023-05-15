package utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

interface RetryPolicyV1 {
    val numRetries: Long
    val delayMillis: Long
    val delayFactor: Long
}

data class DefaultRetryPolicy(
    override val numRetries: Long = 4,
    override val delayMillis: Long = 400,
    override val delayFactor: Long = 2
) : RetryPolicyV1

fun <T> Flow<T>.retryWithPolicy(
    retryPolicy: RetryPolicyV1
): Flow<T> {
    var currentDelay = retryPolicy.delayMillis
    val delayFactor = retryPolicy.delayFactor
    return retryWhen { cause, attempt ->
        if (cause is IOException && attempt < retryPolicy.numRetries) {
            delay(currentDelay)
            currentDelay *= delayFactor
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}