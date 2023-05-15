package data.remote

import data.interfaces.NetworkService
import kotlinx.coroutines.flow.*
import utils.RetryPolicyV1
import utils.retryWithPolicy
import java.io.PrintWriter
import java.net.Socket

class TcpNetworkService(
    private val retryPolicy: RetryPolicyV1
) : NetworkService {
    override fun send(ip: String, port: Int, data: String): Flow<Result<Unit>> = flow {
        Socket(ip, port).use { socket ->
            PrintWriter(socket.getOutputStream(), true).use { out ->
                out.println(data)
            }
        }
        emit(Result.success(Unit))
    }
        .catch { exception: Throwable ->
            emit(Result.failure(exception))
        }
        .retryWithPolicy(retryPolicy)
}