package data.interfaces

import kotlinx.coroutines.flow.Flow

interface NetworkService {
    //    fun send(ip: String, port: Int, data: String): Flow<Unit>
    fun send(ip: String, port: Int, data: String): Flow<Result<Unit>>
}