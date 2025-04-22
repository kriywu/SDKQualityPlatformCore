package com.wurengao.android

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * Created by wurengao on 2025/4/18
 * @author wurengao@bytedance.com
 */

interface IWebSocketListener {
    fun onOpen(webSocket: WebSocket, message: String?, isAlreadyConnected: Boolean)
    fun onMessage(webSocket: WebSocket, text: String)
    fun onClosing(webSocket: WebSocket, code: Int, reason: String)
}


object WebSocketClient {
    private const val TAG = "WebSocketClient"
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()
    private var isConnected = false

    fun connect(host: String, did: String, os: String, listener: IWebSocketListener) {
        val url = "ws://$host/websocket"
        if (isConnected) {
            listener.onOpen(webSocket!!, url, true)
            return
        }
        val request: Request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket Opened")
                isConnected = true
                listener.onOpen(webSocket, url, false)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message $text")
                listener.onMessage(webSocket, text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Received message $bytes")
                super.onMessage(webSocket, bytes)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket Closing $reason")
                webSocket.close(1000, null)
                isConnected = false
                listener.onClosing(webSocket, code, reason ?: "")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d(TAG, "WebSocket onFailure: $t response: $response")
            }
        })
    }

    fun sendMessage(message: String?) {
        webSocket?.send(message!!)
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnect")
    }
}