package infra

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPortDataListener
import com.fazecast.jSerialComm.SerialPortEvent
import config.Messages
import domain.exceptions.ScannerServiceException
import domain.interfaces.scanner.ScannerService
import domain.models.Barcode
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import utils.Resource
import java.nio.charset.StandardCharsets

class PioneerScannerService(
    private val portDescriptor: String,
) : ScannerService {
    private var serialPort: SerialPort? = null


    override fun isConnected(): Boolean {
        val port = serialPort
        if (port != null && port.isOpen) {
            try {
                port.outputStream.write(0)
                return true
            } catch (ex: Exception) {
                serialPort?.closePort()
                return false
            }
        }
        return false
    }

    override fun connect(): Result<Unit> {
        return try {
            val ports = SerialPort.getCommPorts()
            val foundPort =
                ports.find { it.toString() == portDescriptor }
                    ?: return Result.failure(
                        ScannerServiceException(Messages.getMessage("Port not found"))
                    )
            foundPort.openPort()

            if (!foundPort.openPort()) {
                return Result.failure(ScannerServiceException("unable to connect to the comm port"))
            }

            serialPort = foundPort
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun disconnect(): Result<Unit> {
        return try {
            serialPort?.closePort()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun readBarcode(): Result<Barcode> {
        val port = serialPort

        return if (port != null && port.bytesAvailable() > 0) {
            try {
                val readBuffer = ByteArray(port.bytesAvailable())
                val numRead = port.readBytes(readBuffer, readBuffer.size.toLong())
                val barcodeData = String(readBuffer, 0, numRead, StandardCharsets.UTF_8)
                Result.success(Barcode(barcodeData))
            } catch (exception: Exception) {
                Result.failure(exception)
            }
        } else {
            Result.failure(
                ScannerServiceException(Messages.getMessage("scanner_service_reading_error"))
            )
        }
    }

    //    override fun emitBarcode(): Flow<Result<Barcode>> = callbackFlow {
    override fun emitBarcode(): Flow<Resource<Barcode>> = callbackFlow {
        val port = serialPort ?: throw IllegalStateException("Serial port not initialized. Please connect first.")

        val dataListener = object : SerialPortDataListener {

            override fun getListeningEvents(): Int {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE
            }

            override fun serialEvent(event: SerialPortEvent) {
                try {
                    if (event.eventType == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        val readBuffer = ByteArray(port.bytesAvailable())
                        val numRead = port.readBytes(readBuffer, readBuffer.size.toLong())
                        val barcodeData = String(readBuffer, 0, numRead, StandardCharsets.UTF_8)
                        val barcode = Barcode(barcodeData)
//                        trySend(Result.success(barcode))
                        trySend(Resource.Success(barcode))
                    }
                } catch (exception: Exception) {
//                    trySend(Result.failure(exception))
                    trySend(Resource.Error(exception.localizedMessage))
                }
            }

        }

        port.addDataListener(dataListener)

        awaitClose {
            port.removeDataListener()
            port.closePort()
        }
    }

    override fun monitorConnection(checkIntervalMillis: Long): Flow<Result<Unit>> = flow {
        val port = serialPort

        while (true) {
            val isConnected = port?.isOpen ?: false
            if (isConnected) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Throwable()))
            }
            delay(checkIntervalMillis)
        }
    }


}