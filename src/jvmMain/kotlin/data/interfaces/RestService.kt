package data.interfaces

import kotlinx.coroutines.flow.Flow
import utils.Resource

interface RestService {
    fun get(url: String): String

    fun <T : Any, F: Any> post(
        url: String,
        body: T,
        headers: Map<String, String> = emptyMap(),
        responseType: Class<F>
    ): Flow<Resource<F>>


    fun delete(url: String): String
    fun update(url: String, data: String): String
}