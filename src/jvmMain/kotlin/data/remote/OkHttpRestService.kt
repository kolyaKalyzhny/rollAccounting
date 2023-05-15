package data.remote

import data.interfaces.RestService
import domain.interfaces.JsonParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import utils.Resource
import java.io.IOException

class OkHttpRestService (
    private val parser: JsonParser,
    private val client: OkHttpClient
): RestService {

    private val JSON = "application/json; charset=utf-8".toMediaType()

    override fun get(url: String): String {
        TODO("Not yet implemented")
    }

    override fun <T : Any, F : Any> post(
        url: String,
        body: T,
        headers: Map<String, String>,
        responseType: Class<F>
    ): Flow<Resource<F>> = flow {

        emit(Resource.Loading(true))
        val jsonString = parser.toJson(body)
        val requestBody = jsonString.toRequestBody(JSON)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            throw IOException(response.message)
        }

        val parsedResponse = parser.fromJson(responseBody, responseType)
        emit(Resource.Success(parsedResponse))
    }


    override fun delete(url: String): String {
        TODO("Not yet implemented")
    }

    override fun update(url: String, data: String): String {
        TODO("Not yet implemented")
    }
}