package utils

import kotlinx.coroutines.delay

data class RetryPolicy(
    val maxAttempts: Int = 3,
    val delayMillis: Long = 1000L
)


suspend fun <T> withRetry(retryPolicy: RetryPolicy = RetryPolicy(), block: suspend () -> Result<T>): Result<T> {
    var currentAttempt = 0
    var lastException: Exception? = null

    while (currentAttempt < retryPolicy.maxAttempts) {
        try {
            return block()
        } catch (exception: Exception) {
            lastException = exception
            delay(retryPolicy.delayMillis)
            currentAttempt++
        }
    }

    return Result.failure(lastException!!)
}