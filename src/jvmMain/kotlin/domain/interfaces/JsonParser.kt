package domain.interfaces

interface JsonParser {
    fun <T> fromJson(json: String, type: Class<T>): T
    fun toJson(obj: Any): String
}