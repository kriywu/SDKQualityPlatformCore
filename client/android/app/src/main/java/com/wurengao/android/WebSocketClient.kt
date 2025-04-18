package com.wurengao.android

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

/**
 * Created by wurengao on 2025/4/18
 * @author wurengao@bytedance.com
 */

interface IWebSocketListener {
    fun onOpen(webSocket: WebSocket, response: Response)
    fun onMessage(webSocket: WebSocket, text: String)
    fun onClosing(webSocket: WebSocket, code: Int, reason: String)
}


object WebSocketClient {
    private const val TAG = "WebSocketClient"
    private lateinit var webSocket: WebSocket
    private val client = OkHttpClient()
    private var isConnected = false

    fun connect(host: String, did: String, os: String, listener: IWebSocketListener) {
        if (isConnected) {
            Log.d(TAG, "connect: already connected")
            return
        }
        val url = "ws://$host/websocket"
        Log.d(TAG, "connect: $url")
        val request: Request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket Opened")
                isConnected = true
                listener.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: $text")
                listener.onMessage(webSocket, text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket Closing: $reason")
                webSocket.close(1000, null)
                isConnected = false
                listener.onClosing(webSocket, code, reason)
            }
        })
        client.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: String?) {
        webSocket.send(message!!)
    }

    fun disconnect() {
        webSocket.close(1000, "Disconnect")
    }
}