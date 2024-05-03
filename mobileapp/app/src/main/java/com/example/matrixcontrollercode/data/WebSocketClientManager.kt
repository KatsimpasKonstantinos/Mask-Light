package com.example.matrixcontrollercode.data

import kotlinx.coroutines.*
import okhttp3.*
import okio.ByteString

object WebSocketClientManager {
    private var webSocket: WebSocket? = null
    private var client: OkHttpClient? = null

    fun connect(url: String) {
        if (client == null) {
            client = OkHttpClient()
        } else {
            webSocket?.close(NORMAL_CLOSURE_STATUS, "Reconnecting")
        }
        val request = Request.Builder().url(url).build()
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                this@WebSocketClientManager.webSocket = webSocket
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(NORMAL_CLOSURE_STATUS, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            }
        }
        webSocket = client!!.newWebSocket(request, webSocketListener)
    }
    private var sendJob: Job? = null

    private var smoothInt = 2;

    fun send(binaryData: ByteArray) {
        sendJob?.cancel()
        val scope = CoroutineScope(Dispatchers.IO)
        val matrixX = binaryData[binaryData.size - 3].toInt() and 0xFF
        val matrixY = binaryData[binaryData.size - 2].toInt() and 0xFF
        val speed = binaryData[binaryData.size - 1].toInt() and 0xFF
        val frameSize = matrixX * matrixY * 3
        val frameRateMillis = 1000L / (speed * smoothInt)
        val totalFrames = (binaryData.size - 3) / frameSize

        sendJob = scope.launch {
            while (isActive) {
                for (frame in 0 until totalFrames) {
                    if (!isActive) return@launch
                    val startIndex = frame * frameSize
                    val frameChunk = binaryData.copyOfRange(startIndex, startIndex + frameSize)
                    repeat(smoothInt) { // Send each frame twice
                        withContext(Dispatchers.Main) {
                            webSocket?.send(ByteString.of(*frameChunk)) ?: println("WebSocket not initialized")
                        }
                        delay(frameRateMillis)
                    }
                }
            }
        }
    }

    fun close() {
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Goodbye !")
    }

    private const val NORMAL_CLOSURE_STATUS = 1000
}
