package infra

import com.google.gson.Gson
import domain.interfaces.JsonParser

class GSONParser (
    private val gson: Gson
): JsonParser {
    override fun <T> fromJson(json: String, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    override fun toJson(obj: Any): String {
        return gson.toJson(obj)
    }

}